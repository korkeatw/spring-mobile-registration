package com.korkeat.mobileregistration.controller;

import com.korkeat.mobileregistration.entity.MobileUser;
import com.korkeat.mobileregistration.entity.MobileUserType;
import com.korkeat.mobileregistration.repository.MobileUserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(MockitoJUnitRunner.class)
public class MobileUserControllerUnitTest {
    private MockMvc mockMvc;

    @InjectMocks
    private MobileUserController controller;

    @Mock
    private MobileUserRepository repository;

    Long id = 1l;
    String refCode = "201902173333";
    String phoneNumber = "0812223333";
    Long salary = 15000l;
    MobileUserType userType = MobileUserType.SILVER;
    MobileUser user;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        user = new MobileUser();
        user.setId(id);
        user.setPhoneNumber(phoneNumber);
        user.setSalary(salary);
        user.setMobileUserType(userType);
        user.setRefCode(refCode);
    }

    @Test
    public void getUser_whenSpecifyExistingId_returnUser() throws Exception {
        when(repository.getOne(id)).thenReturn(user);

        mockMvc
                .perform(get("/mobile-users/id/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone_number", is(phoneNumber)))
                .andExpect(jsonPath("$.ref_code", is(refCode)));
    }

    @Test
    public void getUser_whenSpecifyNonExistingId_returnUserNotFoundError() throws Exception {
        when(repository.getOne(id)).thenReturn(null);

        mockMvc
                .perform(get("/mobile-users/id/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void findAllByRefCode_whenUserExists_returnListOfUsers() throws Exception {
        when(repository.findAllByRefCode(refCode)).thenReturn(Arrays.asList(user));

        mockMvc
                .perform(get("/mobile-users/ref-code/" + refCode)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].phone_number", is(phoneNumber)))
                .andExpect(jsonPath("$[0].ref_code", is(refCode)));
    }

    @Test
    public void findAllByRefCode_whenUserNotExist_returnNotFoundError() throws Exception {
        when(repository.findAllByRefCode(refCode)).thenReturn(Arrays.asList());

        mockMvc
                .perform(get("/mobile-users/ref-code/" + refCode)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}
