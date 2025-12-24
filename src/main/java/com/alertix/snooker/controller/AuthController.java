package com.alertix.snooker.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alertix.snooker.domain.ERole;
import com.alertix.snooker.domain.PasswordResetToken;
import com.alertix.snooker.domain.User;
import com.alertix.snooker.payload.request.ForgotPasswordRequest;
import com.alertix.snooker.payload.request.LoginRequest;
import com.alertix.snooker.payload.request.ResetPasswordRequest;
import com.alertix.snooker.payload.request.SignupRequest;
import com.alertix.snooker.payload.response.JwtResponse;
import com.alertix.snooker.payload.response.MessageResponse;
import com.alertix.snooker.repository.PasswordResetTokenRepository;
import com.alertix.snooker.repository.UserRepository;
import com.alertix.snooker.security.jwt.JwtUtils;
import com.alertix.snooker.security.services.UserDetailsImpl;
import com.alertix.snooker.service.EmailService;
import com.alertix.snooker.utils.EmailTemplateUtils;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    EmailService emailService;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Value("${frontend.reset-password-url}")
    private String frontendResetUrl;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isEmpty()) {
            // Do not reveal if user exists
            return ResponseEntity.ok(new MessageResponse("If the email exists, a reset link has been sent."));
        }

        User user = userOptional.get();
        // Remove existing token if any
        Optional<PasswordResetToken> existingToken = passwordResetTokenRepository.findByUser(user);
        existingToken.ifPresent(passwordResetTokenRepository::delete);

        PasswordResetToken token = new PasswordResetToken(user);
        passwordResetTokenRepository.save(token);

        String link = (frontendResetUrl != null && !frontendResetUrl.isEmpty() ? frontendResetUrl
                : "http://localhost:3000/reset-password")
                + "?token=" + token.getToken();

        String content = EmailTemplateUtils.getPasswordResetEmail(link);

        emailService.sendSupportEmail(user.getEmail(), "Password Reset Request", content);

        return ResponseEntity.ok(new MessageResponse("If the email exists, a reset link has been sent."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        Optional<PasswordResetToken> tokenOptional = passwordResetTokenRepository.findByToken(request.getToken());

        if (tokenOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid token!"));
        }

        PasswordResetToken token = tokenOptional.get();
        if (token.isExpired()) {
            passwordResetTokenRepository.delete(token);
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Token expired!"));
        }

        User user = token.getUser();
        user.setPassword(encoder.encode(request.getNewPassword()));
        userRepository.save(user);
        passwordResetTokenRepository.delete(token);

        return ResponseEntity.ok(new MessageResponse("Password changed successfully!"));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<ERole> roles = new HashSet<>();

        if (strRoles == null) {
            roles.add(ERole.ROLE_USER);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> roles.add(ERole.ROLE_ADMIN);
                    case "referee" -> roles.add(ERole.ROLE_REFEREE);
                    default -> roles.add(ERole.ROLE_USER);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        String subject = "Welcome to Alertix Snooker!";
        String content = EmailTemplateUtils.getWelcomeEmail(user.getUsername());

        emailService.sendInfoEmail(user.getEmail(), subject, content);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
