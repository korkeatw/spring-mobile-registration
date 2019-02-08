package com.korkeat.mobileregistration.controller;

import com.korkeat.mobileregistration.entity.MobileUser;
import com.korkeat.mobileregistration.repository.MobileUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
public class MobileUserController {
    private final MobileUserRepository mobileUserRepository;

    public MobileUserController(MobileUserRepository mobileUserRepository) {
        this.mobileUserRepository = mobileUserRepository;
    }

    @PostMapping("/mobile-users")
    MobileUser createNewUser(@Valid @RequestBody MobileUser mobileUser) {
        return mobileUserRepository.save(mobileUser);
    }

    @PutMapping("/mobile-users")
    MobileUser updateUser(@Valid Long id, @Valid @RequestBody MobileUser updatedMobileUser) {
        return mobileUserRepository.findById(id)
                .map(mobileUser -> {
                    mobileUser.setPhoneNumber(updatedMobileUser.getPhoneNumber());
                    mobileUser.setSalary(updatedMobileUser.getSalary());
                    return mobileUserRepository.save(mobileUser);
                })
                .orElseGet(() -> {
                    updatedMobileUser.setId(id);
                    return mobileUserRepository.save(updatedMobileUser);
                });
    }

    @GetMapping("/mobile-users/ref-code/{refCode}")
    List<MobileUser> findAllByRefCode(@Valid @PathVariable String refCode) {
        List<MobileUser> mobileUsers = mobileUserRepository.findAllByRefCode(refCode);
        if(mobileUsers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exists for the refCode");
        }
        return mobileUsers;
    }

    @GetMapping("/mobile-users/id/{id}")
    MobileUser getUser(@Valid @PathVariable Long id) {
        MobileUser mobileUser = mobileUserRepository.getOne(id);
        if(mobileUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exists for the id");
        }
        return mobileUser;
    }

    @DeleteMapping("/mobile-users/id/{id}")
    void deleteUser(@Valid @PathVariable Long id) {
        mobileUserRepository.deleteById(id);
    }
}
