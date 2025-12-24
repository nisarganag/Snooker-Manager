package com.alertix.snooker.controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alertix.snooker.domain.User;
import com.alertix.snooker.payload.request.IdRequest;
import com.alertix.snooker.payload.request.UserUpdateRequest;
import com.alertix.snooker.payload.response.MessageResponse;
import com.alertix.snooker.repository.UserRepository;
import com.alertix.snooker.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @PostMapping("/{id}/photo")
    public ResponseEntity<?> uploadProfilePhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: File is empty!"));
        }

        if (file.getSize() > 2 * 1024 * 1024) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: File size exceeds 2MB!"));
        }

        Optional<User> userOptional = userRepository.findById(Objects.requireNonNull(id));
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found!"));
        }

        User user = userOptional.get();
        try {
            user.setProfilePhoto(file.getBytes());
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("Profile photo uploaded successfully!"));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(new MessageResponse("Error: Failed to upload photo!"));
        }
    }

    @GetMapping(value = "/{id}/photo", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getProfilePhoto(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(Objects.requireNonNull(id));
        if (userOptional.isEmpty() || userOptional.get().getProfilePhoto() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().contentType(Objects.requireNonNull(MediaType.IMAGE_JPEG))
                .body(userOptional.get().getProfilePhoto());
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest request) {
        userService.updateUser(request.getId(), request);
        return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody IdRequest request) {
        userService.deleteUser(request.getId());
        return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
    }

    @PostMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/get")
    public ResponseEntity<User> getUserById(@RequestBody IdRequest request) {
        return userService.getUserById(request.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
