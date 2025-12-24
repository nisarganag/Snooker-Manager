package com.alertix.snooker.service;

import java.util.List;
import java.util.Optional;

import com.alertix.snooker.domain.User;
import com.alertix.snooker.payload.request.UserUpdateRequest;

public interface UserService {
    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    User updateUser(Long id, UserUpdateRequest request);

    void deleteUser(Long id);
    // Profile photo methods moved/kept here logically, but controller currently
    // handles them.
    // Ideally we move them too, but let's focus on the new requirements first.
}
