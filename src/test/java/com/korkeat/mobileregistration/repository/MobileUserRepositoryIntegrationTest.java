package com.korkeat.mobileregistration.repository;

import com.korkeat.mobileregistration.entity.MobileUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MobileUserRepositoryIntegrationTest {
    @Autowired
    private MobileUserRepository repository;

    @Test
    public void findAllByRefCode_returnEmptyList_ifNotFound() {
        MobileUser user = new MobileUser();
        user.setPhoneNumber("0811111111");
        user.setSalary(15000);
        user.setRefCode("201902191111");

        repository.save(user);

        List<MobileUser> users = repository.findAllByRefCode("201902193333");

        assertThat(users).isEmpty();
    }

    @Test
    public void findAllByRefCode_returnUsersInTheList() {
        MobileUser user1 = new MobileUser();
        MobileUser user2 = new MobileUser();
        String refCode = "201902193333";

        user1.setPhoneNumber("0811113333");
        user1.setRefCode(refCode);
        user1.setSalary(15000);

        user2.setPhoneNumber("0821113333");
        user2.setRefCode(refCode);
        user2.setSalary(15000);

        repository.save(user1);
        repository.save(user2);

        List<MobileUser> users = repository.findAllByRefCode(refCode);

        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(2);
    }
}
