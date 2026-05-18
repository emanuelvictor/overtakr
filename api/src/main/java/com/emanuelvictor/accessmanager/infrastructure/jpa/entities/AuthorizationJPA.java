package com.emanuelvictor.accessmanager.infrastructure.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import static com.emanuelvictor.Main.DEFAULT_TENANT_IDENTIFICATION;

/**
 * @author Emanuel Victor
 * @version 1.0.0
 * @since 1.0.0, 10/09/2019
 */
@Data
@Entity
@Table(schema = DEFAULT_TENANT_IDENTIFICATION, name = "oauth2_authorization")
public class AuthorizationJPA {

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "principal_name")
    private String principalName;

    private String sid;
    private String accessTokenValue;
    private String refreshTokenValue;
}