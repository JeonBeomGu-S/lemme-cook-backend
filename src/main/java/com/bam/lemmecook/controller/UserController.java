package com.bam.lemmecook.controller;

import com.bam.lemmecook.dto.request.RequestLoginDTO;
import com.bam.lemmecook.dto.request.RequestSignupDTO;
import com.bam.lemmecook.dto.response.ResponseDTO;
import com.bam.lemmecook.dto.response.ResponseUserDTO;
import com.bam.lemmecook.entity.User;
import com.bam.lemmecook.repository.UserRepository;
import com.bam.lemmecook.security.JwtToken;
import com.bam.lemmecook.security.UserDetails;
import com.bam.lemmecook.service.UserService;
import com.bam.lemmecook.util.JwtTokenUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;

    public UserController(UserRepository userRepository, JwtTokenUtil jwtTokenUtil, BCryptPasswordEncoder passwordEncoder, UserService userService) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    // Signup
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RequestSignupDTO user) {
        // encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userRepository.save(new User(
                    null,
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getRole(),
                    new Date(),
                    new Date()
            ));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(
                    ResponseDTO
                            .builder()
                            .status(400)
                            .message("The email is a duplicate.")
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ResponseDTO
                            .builder()
                            .status(500)
                            .message(e.getMessage())
                            .build()
            );
        }

        return ResponseEntity.ok(
                ResponseDTO
                        .builder()
                        .status(200)
                        .message("User registered successfully")
                        .build()
        );
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
                return ResponseEntity.status(401).body(
                        ResponseDTO
                                .builder()
                                .status(401)
                                .message("Invalid credentials")
                                .build()
                );
            }
        } else {
            return ResponseEntity.status(404).body(
                    ResponseDTO
                            .builder()
                            .status(401)
                            .message("User not found")
                            .build()
            );
        }
    }

    @PostMapping("/follow/{targetId}")
    public ResponseEntity<?> follow(@PathVariable Integer targetId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId = userDetails.getId();

        userService.follow(userId, targetId);

        return ResponseEntity.ok(
                ResponseDTO
                        .builder()
                        .status(200)
                        .message("User followed")
                        .build()
        );
    }

    @DeleteMapping("/follow/{targetId}")
    public ResponseEntity<?> unfollow(@PathVariable Integer targetId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId = userDetails.getId();

        userService.unfollow(userId, targetId);
        return ResponseEntity.ok(
                ResponseDTO
                        .builder()
                        .status(200)
                        .message("User unfollowed")
                        .build()
        );
    }

    @GetMapping("/following")
    public ResponseEntity<?> getFollowList() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId = userDetails.getId();

        List<User> users = userService.getFollowingList(userId);
        List<ResponseUserDTO> userDTOs = users
                .stream()
                .map(user -> new ResponseUserDTO(user.getId(), user.getUsername()))
                .toList();

        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/follower")
    public ResponseEntity<?> getFollowerList() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId = userDetails.getId();

        List<User> users = userService.getFollowerList(userId);
        List<ResponseUserDTO> userDTOs = users
                .stream()
                .map(user -> new ResponseUserDTO(user.getId(), user.getUsername()))
                .toList();

        return ResponseEntity.ok(userDTOs);
    }
}
