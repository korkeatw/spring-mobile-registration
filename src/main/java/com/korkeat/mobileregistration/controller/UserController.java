package com.korkeat.mobileregistration.controller;

import com.korkeat.mobileregistration.repository.UserRepository;
import com.korkeat.mobileregistration.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/users")
    User createNewUser(@Valid @RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/users")
    User updateUser(@Valid Long id, @Valid @RequestBody User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setPhoneNumber(updatedUser.getPhoneNumber());
                    user.setSalary(updatedUser.getSalary());
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    updatedUser.setId(id);
                    return userRepository.save(updatedUser);
                });
    }

    @GetMapping("/users/ref/{refCode}")
    List<User> findAllByRefCode(@Valid @PathVariable String refCode) {
        List<User> users = userRepository.findAllByRefCode(refCode);
        if(users.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exists for the refCode");
        }
        return users;
    }

    @GetMapping("/users/id/{id}")
    User getUser(@Valid @PathVariable Long id) {
        User user = userRepository.getOne(id);
        if(user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exists for the id");
        }
        return user;
    }

    @DeleteMapping("/users/id/{id}")
    void deleteUser(@Valid @PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
