package com.alertix.snooker.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alertix.snooker.domain.ERole;
import com.alertix.snooker.domain.User;
import com.alertix.snooker.payload.request.UserUpdateRequest;
import com.alertix.snooker.repository.UserRepository;
import com.alertix.snooker.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(Objects.requireNonNull(id));
    }

    @Override
    public User updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            user.setEmail(request.getEmail());
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            Set<ERole> roles = new HashSet<>();
            request.getRoles().forEach(role -> {
                switch (role) {
                    case "admin" -> roles.add(ERole.ROLE_ADMIN);
                    case "referee" -> roles.add(ERole.ROLE_REFEREE);
                    default -> roles.add(ERole.ROLE_USER);
                }
            });
            user.setRoles(roles);
        }

        User savedUser = userRepository.save(user);
        return savedUser;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(Objects.requireNonNull(id));
    }
}
