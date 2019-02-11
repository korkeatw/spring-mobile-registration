package com.korkeat.mobileregistration.repository;

import com.korkeat.mobileregistration.entity.LoginUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface LoginUserRepository extends JpaRepository<LoginUser, Long> {
    @Query(value="SELECT * FROM login_users WHERE username = :username AND password = :password", nativeQuery=true)
    LoginUser getUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}
