package com.bam.lemmecook.controller;

import com.bam.lemmecook.dto.request.RequestLoginDTO;
import com.bam.lemmecook.dto.request.RequestSignupDTO;
import com.bam.lemmecook.entity.User;
import com.bam.lemmecook.repository.UserRepository;
import com.bam.lemmecook.security.JwtToken;
import com.bam.lemmecook.util.JwtTokenUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, JwtTokenUtil jwtTokenUtil, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // Signup
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RequestSignupDTO user) {
        // encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(new User(
                null,
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                new Date(),
                new Date()
        ));
        return ResponseEntity.ok("User registered successfully");
    }

    // login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequestLoginDTO loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // check password
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                JwtToken token = jwtTokenUtil.generateToken(authentication, user);

                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.status(401).body("Invalid credentials");
            }
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }
}
