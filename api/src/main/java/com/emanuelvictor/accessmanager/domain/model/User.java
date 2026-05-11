package com.emanuelvictor.accessmanager.domain.model;

import com.emanuelvictor.common.infrastructure.jpa.PersistentEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Pattern.Flag;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

import static com.emanuelvictor.Main.DEFAULT_TENANT_IDENTIFICATION;

/**
 * @author Emanuel Victor
 * @version 1.0.0
 * @since 1.0.0, 10/09/2019
 */
@Data
@Entity
@JsonIgnoreProperties({"authorities"})
@lombok.EqualsAndHashCode(callSuper = true)
@Table(schema = DEFAULT_TENANT_IDENTIFICATION, name = "user")
public class User extends PersistentEntity implements UserDetails {

    public static final String DEFAULT_PASSWORD = "!UserDefaultPassword0*";

    @Column(nullable = false, length = 150, unique = true)
    private String username;

    @NotBlank
    @Column(nullable = false, length = 100)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@!%*#?&+,./])[A-Za-z\\d$@!%*#?&+,./]{8,}$", flags = Flag.UNICODE_CASE,
            message = "A senha deve conter ao menos 8 caracteres com letras, números e um caractere especial.")
    private String password;

    @NotNull
    @Column(nullable = false)
    private Boolean enabled = true;

    @NotNull
    @Column(nullable = false)
    private boolean locked;

    @Column(nullable = false, length = 250)
    private String name;

    @ManyToOne(optional = false)
    private Group group;

    @ManyToOne
    private Tenant tenant;

    public User() {
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public boolean isEnabled() {
        return this.enabled;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Transient
    private Set<Permission> authorities;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Long getGroupId() {
        return group.getId();
    }

    @Override
    public void beforeInsert() {
        super.beforeInsert();
        this.username = this.username.replaceAll(" ", "_").trim();
    }

    @Override
    public void beforeUpdate() {
        super.beforeUpdate();
        this.username = this.username.replaceAll(" ", "_").trim();
    }
}