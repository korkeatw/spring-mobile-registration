package com.korkeat.mobileregistration.entity;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class MobileUserUnitTest {
    @Test
    public void MobileUser_normalizePhoneNumberCorrectly() throws Exception {
        String expectedPhoneNumber = "0812223333";
        MobileUser user;

        user = new MobileUser("081-222-3333", 50001l);
        assertThat(user.getPhoneNumber()).isEqualTo(expectedPhoneNumber);

        user = new MobileUser("081 222 3333", 50001l);
        assertThat(user.getPhoneNumber()).isEqualTo(expectedPhoneNumber);
    }

    @Test
    public void MobileUser_returnMemberTypeCorrectly() throws Exception {
        String phoneNumber = "081-222-3333";
        MobileUser user;

        user = new MobileUser(phoneNumber, 50001l);
        assertThat(user.getMobileUserType()).isEqualTo(MobileUserType.PLATINUM);

        user = new MobileUser(phoneNumber, 50000l);
        assertThat(user.getMobileUserType()).isEqualTo(MobileUserType.PLATINUM);

        user = new MobileUser(phoneNumber, 49999l);
        assertThat(user.getMobileUserType()).isEqualTo(MobileUserType.GOLD);

        user = new MobileUser(phoneNumber, 30000l);
        assertThat(user.getMobileUserType()).isEqualTo(MobileUserType.GOLD);

        user = new MobileUser(phoneNumber, 29999l);
        assertThat(user.getMobileUserType()).isEqualTo(MobileUserType.SILVER);

        user = new MobileUser(phoneNumber, 15000l);
        assertThat(user.getMobileUserType()).isEqualTo(MobileUserType.SILVER);

        Throwable thrown = catchThrowable(() -> {
            new MobileUser(phoneNumber, 14999l);
        });
        assertThat(thrown).hasMessageContaining("Salary under minimum requirement");
    }
}
