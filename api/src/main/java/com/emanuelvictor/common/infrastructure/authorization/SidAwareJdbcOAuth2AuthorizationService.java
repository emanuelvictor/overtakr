package com.emanuelvictor.common.infrastructure.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

// TODO rever isso
public class SidAwareJdbcOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private static final String INSERT_SQL = """
            INSERT INTO oauth2_authorization (
                id, registered_client_id, principal_name, authorization_grant_type,
                authorized_scopes, attributes, state,
                authorization_code_value, authorization_code_issued_at, authorization_code_expires_at, authorization_code_metadata,
                access_token_value, access_token_issued_at, access_token_expires_at, access_token_metadata,
                access_token_type, access_token_scopes,
                oidc_id_token_value, oidc_id_token_issued_at, oidc_id_token_expires_at, oidc_id_token_metadata,
                refresh_token_value, refresh_token_issued_at, refresh_token_expires_at, refresh_token_metadata,
                user_code_value, user_code_issued_at, user_code_expires_at, user_code_metadata,
                device_code_value, device_code_issued_at, device_code_expires_at, device_code_metadata,
                sid
            ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE oauth2_authorization SET
                registered_client_id=?, principal_name=?, authorization_grant_type=?,
                authorized_scopes=?, attributes=?, state=?,
                authorization_code_value=?, authorization_code_issued_at=?, authorization_code_expires_at=?, authorization_code_metadata=?,
                access_token_value=?, access_token_issued_at=?, access_token_expires_at=?, access_token_metadata=?,
                access_token_type=?, access_token_scopes=?,
                oidc_id_token_value=?, oidc_id_token_issued_at=?, oidc_id_token_expires_at=?, oidc_id_token_metadata=?,
                refresh_token_value=?, refresh_token_issued_at=?, refresh_token_expires_at=?, refresh_token_metadata=?,
                user_code_value=?, user_code_issued_at=?, user_code_expires_at=?, user_code_metadata=?,
                device_code_value=?, device_code_issued_at=?, device_code_expires_at=?, device_code_metadata=?,
                sid=?
            WHERE id=?
            """;

    private static final String EXISTS_SQL =
            "SELECT COUNT(*) FROM oauth2_authorization WHERE id = ?";

    private final JdbcOAuth2AuthorizationService delegate;
    private final JdbcOperations jdbcOperations;
    private final SidAwareParametersMapper parametersMapper;

    public SidAwareJdbcOAuth2AuthorizationService(JdbcOperations jdbcOperations,
                                                  RegisteredClientRepository registeredClientRepository,
                                                  ObjectMapper objectMapper) {
        this.jdbcOperations = jdbcOperations;
        this.parametersMapper = new SidAwareParametersMapper(objectMapper);

        this.delegate = new JdbcOAuth2AuthorizationService(jdbcOperations, registeredClientRepository);

        var rowMapper = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(registeredClientRepository);
        rowMapper.setObjectMapper(objectMapper);
        delegate.setAuthorizationRowMapper(rowMapper);

        // O delegate usa seu próprio parametersMapper internamente só no save —
        // nós sobrescrevemos o save, então isso aqui é só para consistência
        delegate.setAuthorizationParametersMapper(parametersMapper);
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        var requestAttrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttrs != null) {
            var session = requestAttrs.getRequest().getSession(false);
            if (session != null && authorization.getAttribute("sid") == null) { // TODO se remvoer isso, não precisa  desta classe. É possível remover isso, ou ainda vamos precisar do SID?
                authorization = OAuth2Authorization.from(authorization)               // TODO Vou precisar se quiser revogar o SID quando revogar o token.
                        .attribute("sid", session.getId())
                        .build();
            }
        }

        boolean exists = Boolean.TRUE.equals(
                jdbcOperations.queryForObject(EXISTS_SQL, Integer.class, authorization.getId()) > 0
        );

        List<SqlParameterValue> params = new ArrayList<>(parametersMapper.apply(authorization));

        if (exists) {
            // UPDATE: remove o 'id' do início e adiciona no final (WHERE id=?)
            SqlParameterValue idParam = params.remove(0);
            params.add(idParam);
            jdbcOperations.update(UPDATE_SQL, params.stream()
                    .map(p -> new org.springframework.jdbc.core.SqlParameterValue(p.getSqlType(), p.getValue()))
                    .toArray());
        } else {
            jdbcOperations.update(INSERT_SQL, params.stream()
                    .map(p -> new org.springframework.jdbc.core.SqlParameterValue(p.getSqlType(), p.getValue()))
                    .toArray());
        }
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        delegate.remove(authorization);
    }

    @Override
    public OAuth2Authorization findById(String id) {
        return delegate.findById(id);
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        return delegate.findByToken(token, tokenType);
    }

    // -------------------------------------------------------------------------
    // Parameters Mapper — 33 params padrão + sid = 34
    // -------------------------------------------------------------------------

    public static class SidAwareParametersMapper
            extends JdbcOAuth2AuthorizationService.OAuth2AuthorizationParametersMapper {

        public SidAwareParametersMapper(ObjectMapper objectMapper) {
            setObjectMapper(objectMapper);
        }

        @Override
        public List<SqlParameterValue> apply(OAuth2Authorization authorization) {
            List<SqlParameterValue> params = new ArrayList<>(super.apply(authorization));

            String sid = null;
            OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
            if (accessToken != null && accessToken.getClaims() != null) {
                sid = (String) accessToken.getClaims().get("sid");
            }

            params.add(new SqlParameterValue(Types.VARCHAR, sid));
            return params;
        }
    }
}