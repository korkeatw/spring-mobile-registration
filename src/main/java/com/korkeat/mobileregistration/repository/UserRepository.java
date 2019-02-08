package com.korkeat.mobileregistration.repository;

import com.korkeat.mobileregistration.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value="SELECT * FROM users WHERE ref_code = :refCode", nativeQuery=true)
    List<User> findAllByRefCode(@Param("refCode") String refCode);
}
