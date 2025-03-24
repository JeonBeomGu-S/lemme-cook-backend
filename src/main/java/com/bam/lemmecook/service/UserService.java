package com.bam.lemmecook.service;

import com.bam.lemmecook.entity.User;
import com.bam.lemmecook.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getUserNameById(Integer id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(User::getUsername).orElse(null);
    }
}
