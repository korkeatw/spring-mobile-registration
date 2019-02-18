package com.korkeat.mobileregistration.controller;

import com.korkeat.mobileregistration.entity.LoginUser;
import com.korkeat.mobileregistration.repository.LoginUserRepository;
import com.korkeat.mobileregistration.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(MockitoJUnitRunner.class)
public class AuthControllerUnitTest {
    MockMvc mockMvc;

    @InjectMocks
    AuthController controller;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @Mock
    LoginUserRepository loginUserRepository;

    String username = "test@abc.com";
    String password = "1234";
    String encodedPassword = LoginUser.hashPassword(password);
    LoginUser loginUser;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        loginUser = new LoginUser();
        loginUser.setUsername(username);
        loginUser.setPassword(encodedPassword);
    }

    @Test
    public void login_throwAnError_ifUsernameOrPasswordDoesNotMatch() throws Exception {
        when(loginUserRepository.getUserByUsernameAndPassword(username, encodedPassword)).thenReturn(null);

        mockMvc
                .perform(post("/auth/login")
                        .content("{\"username\":\""+ username +"\",\"password\":"+ password +"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void login_returnAuthenticationWithToken_ifAuthenticationSuccess() throws Exception {
        String token = "xxx.yyy.zzz";
        Claims claims = new DefaultClaims();
        claims.setSubject(username);

        when(loginUserRepository.getUserByUsernameAndPassword(username, encodedPassword)).thenReturn(loginUser);
        when(jwtTokenProvider.createToken(username)).thenReturn(token);
        when(jwtTokenProvider.getClaims(token)).thenReturn(claims);

        mockMvc
                .perform(post("/auth/login")
                        .content("{\"username\":\""+ username +"\",\"password\":"+ password +"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(token)))
                .andExpect(jsonPath("$.username", is(username)));
    }
}
