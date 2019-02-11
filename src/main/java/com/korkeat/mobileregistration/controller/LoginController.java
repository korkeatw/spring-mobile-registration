package com.korkeat.mobileregistration.controller;

import com.korkeat.mobileregistration.entity.AuthenticationRequest;
import com.korkeat.mobileregistration.entity.AuthenticationResponse;
import com.korkeat.mobileregistration.entity.LoginUser;
import com.korkeat.mobileregistration.repository.LoginUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@Slf4j
public class LoginController {
    private final LoginUserRepository loginUserRepository;

    public LoginController(LoginUserRepository loginUserRepository) {
        this.loginUserRepository = loginUserRepository;
    }

    @PostMapping("/auth/login")
    AuthenticationResponse login(@Valid @RequestBody AuthenticationRequest authReq) {
        try {
            String username = authReq.getUsername();
            String password = authReq.getPassword();
            LoginUser foundUser = loginUserRepository.getUserByUsernameAndPassword(username, LoginUser.hashPassword(password));
            if (foundUser == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User or password is invalid");
            }
            log.debug("Authentication successful");
            foundUser.updateLastLoginTimestamp();
            loginUserRepository.save(foundUser);
            AuthenticationResponse authRes = new AuthenticationResponse();
            authRes.setUsername(foundUser.getUsername());
            authRes.setToken("");
            return authRes;
        } catch(AuthenticationException err) {
            log.debug("Authentication failed");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is invalid", err);
        }
    }
}
