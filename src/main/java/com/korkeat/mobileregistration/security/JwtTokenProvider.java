package com.korkeat.mobileregistration.security;

import com.korkeat.mobileregistration.entity.LoginUser;
import com.korkeat.mobileregistration.repository.LoginUserRepository;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key:THIS_IS_SECRET}")
    private String secretKey;

    // Default is 1hr
    @Value("${security.jwt.token.expiration-millisecs:3600000}")
    private long expirationMilliseconds;

    private LoginUserRepository loginUserRepository;

    public JwtTokenProvider(LoginUserRepository loginUserRepository) {
        this.loginUserRepository = loginUserRepository;
    }

    public String createToken(String username) {
        Claims claims = Jwts.claims().setSubject(username);
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        String authPrefix = "Bearer ";
        if (bearerToken == null || !bearerToken.startsWith(authPrefix)) {
            throw new JwtException("Missing authentication token");
        }
        return StringUtils.substringAfter(bearerToken, authPrefix);
    }

    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public void validateToken(String token) {
        Jws<Claims> claims = null;

        try {
            claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        } catch(JwtException | IllegalArgumentException err) {
            throw new JwtException("Malformed token");
        }

        if (claims.getBody().getExpiration().before(new Date())) {
            throw new JwtException("Token expired");
        }
    }

    public String resolveUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Authentication getAuthentication(String token) {
        String username = resolveUsernameFromToken(token);
        LoginUser loginUser = loginUserRepository.getUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(loginUser, null, null);
    }
}
