package com.emanuelvictor.api.functional.accessmanager.application.aspect;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolation;
import java.util.logging.Logger;

/**
 * Class RestResponseEntityExceptionHandler, serve para capturar os erros da aplicação e trata-los
 *
 * @author Emanuel Victor
 * @version 1.0.0
 * @since 1.0.0, 10/09/2019
 */
@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     *
     */
    private static final Logger LOGGER = Logger.getLogger(RestResponseEntityExceptionHandler.class.getName());

    /**
     *
     */
    private final MessageSource messageSource;

    /**
     * @param messageSource {MessageSource}
     */
    @Autowired
    public RestResponseEntityExceptionHandler(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Trata exceções de Constraint geradas pelo PostgreSQL
     *
     * @param exception {DataIntegrityViolationException}
     * @param request   {WebRequest}
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleException(final DataIntegrityViolationException exception, final WebRequest request) {
        String message = this.messageSource.getMessage("repository.dataIntegrityViolation", null, LocaleContextHolder.getLocale());

        if (exception.getCause() instanceof ConstraintViolationException) {
            final ConstraintViolationException cause = (ConstraintViolationException) exception.getCause();
            final PSQLException sqlException = (PSQLException) cause.getSQLException();

            final String detail = sqlException.getServerErrorMessage().getDetail();

            final String constraintName;

            String key;
            //Verifica o código do erro gerado pelo PostgreSQL
            switch (cause.getSQLState()) {
                case "23503": //violação de exclusão quando chave primaria de registro é referenciado por outro
                    key = detail.substring(detail.indexOf('"') + 1, detail.indexOf('.') - 1);

                    constraintName = this.messageSource.getMessage(snakeCaseToHumanReadable(key), null, LocaleContextHolder.getLocale());

                    message = this.messageSource.getMessage("repository.foreignKeyViolation", new String[]{constraintName}, LocaleContextHolder.getLocale());
                    break;
                case "23505":  //violação de unicidade
                    key = detail.substring(detail.indexOf('(') + 1, detail.indexOf(')'));

                    if (key.startsWith("lower(")) {
                        key = key.replace("lower(", "");
                        key = key.replace("::text", "");
                    }

                    constraintName = this.messageSource.getMessage(snakeCaseToHumanReadable(key), null, LocaleContextHolder.getLocale());

                    message = this.messageSource.getMessage("repository.uniqueViolation", new String[]{constraintName}, LocaleContextHolder.getLocale());
                    break;
                case "23502": //violação de nulidade
                    constraintName = this.messageSource.getMessage(cause.getConstraintName(), null, LocaleContextHolder.getLocale());
                    message = this.messageSource.getMessage("repository.fieldMustbeSet", new String[]{constraintName}, LocaleContextHolder.getLocale());
                    break;
                default:
                    constraintName = this.messageSource.getMessage(cause.getConstraintName(), null, LocaleContextHolder.getLocale());

                    message = this.messageSource.getMessage("repository.uniqueViolation", new String[]{constraintName}, LocaleContextHolder.getLocale());
                    break;
            }
        }

        return handleExceptionInternal(exception, new Error(message), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Trata exceções geradas pelo Hibernate antes de enviar para o banco
     */
    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<Object> handleException(final jakarta.validation.ConstraintViolationException exception, final WebRequest request) {
        final StringBuilder message = new StringBuilder();
        //for (ConstraintViolation<?> constraint : exception.getConstraintViolations()) {
        ConstraintViolation<?> constraint = exception.getConstraintViolations().iterator().next();
        final String annotationType = constraint.getConstraintDescriptor().getAnnotation().annotationType().getName();

        /*
         * Dependendo da versão do spring a validação pode estar no pacote
         * "jakarta.validation.constraints.*" ou "org.hibernate.validator.constraints.*"
         */
        if (annotationType.equals("jakarta.validation.constraints.NotNull")
                || annotationType.equals("jakarta.validation.constraints.NotEmpty")
                || annotationType.equals("org.hibernate.validator.constraints.NotEmpty")
                || annotationType.equals("org.hibernate.validator.constraints.NotBlank")
                || annotationType.equals("jakarta.validation.constraints.NotBlank")) {

            //converte camel case para legivel
            message.append("\nO campo ").append(camelCaseToHumanReadable(constraint.getPropertyPath().toString())).append(" deve ser informado.");
        } else if (annotationType.equals("org.hibernate.validator.constraints.Length")) {
            message.append("\n").append("Campo ").append(camelCaseToHumanReadable(constraint.getPropertyPath().toString())).append(": ").append(constraint.getMessage());
        } else {
            message.append("\n").append(constraint.getMessage());
        }

        //}

        return handleExceptionInternal(new Exception(message.toString()), new Error(message.toString()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     *
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Object> handleException(final DuplicateKeyException exception, final WebRequest request) {
        final String message = this.messageSource.getMessage("repository.duplicatedKey", null, LocaleContextHolder.getLocale());
        return handleExceptionInternal(exception, new Error(message), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * @param exception Result
     * @param request   WebRequest
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(org.springframework.dao.EmptyResultDataAccessException.class)
    public ResponseEntity<Object> handleException(final org.springframework.dao.EmptyResultDataAccessException exception, final WebRequest request) {
        final String message = messageSource.getMessage("repository.emptyResult", null, LocaleContextHolder.getLocale());
        return handleExceptionInternal(exception, new Error(message), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Trata exceções de acesso negado
     */
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<Object> handleException(final org.springframework.security.access.AccessDeniedException exception, final WebRequest request) {
        return handleExceptionInternal(exception, new Error(this.messageSource.getMessage("security.accessDenied", null, LocaleContextHolder.getLocale())), new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    /**
     * @param ex      Exception
     * @param body    Error
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return ResponseEntity<Object>
     */
    private ResponseEntity<Object> handleExceptionInternal(final Exception ex, final @Nullable Error body,
                                                           final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        LOGGER.warning("Aspecto capturado: " + ex.getLocalizedMessage());

        return super.handleExceptionInternal(ex, body, headers, status, request);

    }

    /**
     * Converte de camel case para legível/literal
     */
    private String camelCaseToHumanReadable(String phrase) {
        final String[] words = StringUtils.splitByCharacterTypeCamelCase(phrase);

        return (words.length == 1) ? phrase : StringUtils.capitalize(StringUtils.join(words, " "));
    }

    /**
     * Converte de snake case para legível/literal
     */
    private String snakeCaseToHumanReadable(String phrase) {
        final String[] words = StringUtils.splitByWholeSeparator(phrase, "_");

        return (words.length == 1) ? phrase : StringUtils.capitalize(StringUtils.join(words, " "));
    }

    /**
     * Entidade auxiliar utilizada para serializar os erros para o front-end
     */
    public static class Error {

        /**
         *
         */
        private String message;

        /**
         *
         */
        public Error() {
        }

        /**
         * @param message String
         */
        public Error(final String message) {
            this.message = message;
        }

        /**
         * @return String
         */
        public String getMessage() {
            return message;
        }

        /**
         * @param message String
         */
        public void setMessage(final String message) {
            this.message = message;
        }
    }
}
