package com.korkeat.mobileregistration.repository;

import com.korkeat.mobileregistration.entity.MobileUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MobileUserRepository extends JpaRepository<MobileUser, Long> {
    @Query(value="SELECT * FROM mobile_users WHERE ref_code = :refCode", nativeQuery=true)
    List<MobileUser> findAllByRefCode(@Param("refCode") String refCode);
}
