package com.korkeat.mobileregistration.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.beans.ConstructorProperties;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@Data
@Entity
@Table(name="login_users",
        indexes = {@Index(name="username_index", columnList = "username")}
)
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Slf4j
public class LoginUser {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name="username")
    private String username;

    @NotNull
    @Column(name="password")
    private String password;

    @Column(name="created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date createdAt;

    @Column(name="last_login")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date lastLogin;

    @ConstructorProperties({"username", "password"})
    public LoginUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void updateLastLoginTimestamp() {
        this.lastLogin = new Date(System.currentTimeMillis());
    }

    public static String hashPassword(String password) {
        try {
            byte[] passwordAsBytes = password.getBytes("UTF-8");
            String hash = DigestUtils.md5DigestAsHex(passwordAsBytes);
            log.debug("MD5: " + hash);
            return hash.toLowerCase();
        } catch(UnsupportedEncodingException err) {
            log.error(err.getMessage());
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication failed");
    }

    @PrePersist
    private void createTimestamp() {
        this.createdAt = new Date(System.currentTimeMillis());
    }
}
