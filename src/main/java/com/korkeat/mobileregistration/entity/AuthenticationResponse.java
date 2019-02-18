package com.korkeat.mobileregistration.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String username;
    private String token;

    @JsonProperty("issued_at")
    private Date issuedAt;

    @JsonProperty("expired_at")
    private Date expiredAt;
}
