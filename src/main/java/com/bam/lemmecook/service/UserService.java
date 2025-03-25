package com.bam.lemmecook.service;

import com.bam.lemmecook.entity.FollowUser;
import com.bam.lemmecook.entity.User;
import com.bam.lemmecook.entity.id.FollowUserId;
import com.bam.lemmecook.repository.FollowUserRepository;
import com.bam.lemmecook.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final FollowUserRepository followUserRepository;
    public UserService(UserRepository userRepository, FollowUserRepository followUserRepository) {
        this.userRepository = userRepository;
        this.followUserRepository = followUserRepository;
    }

    public String getUserNameById(Integer id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(User::getUsername).orElse(null);
    }

    public void follow(Integer userId, Integer targetId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<User> targetUser = userRepository.findById(targetId);
        if (user.isPresent() && targetUser.isPresent()) {
            followUserRepository.save(new FollowUser(
                    new FollowUserId(userId, targetId),
                    user.get(),
                    targetUser.get()
            ));
        }
    }

    public void unfollow(Integer userId, Integer targetId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<User> targetUser = userRepository.findById(targetId);
        if (user.isPresent() && targetUser.isPresent()) {
            followUserRepository.delete(new FollowUser(
                    new FollowUserId(userId, targetId),
                    user.get(),
                    targetUser.get()
            ));
        }
    }

    public List<User> getFollowingList(Integer userId) {
        List<Integer> followUserIdList = followUserRepository.findFollowListByUserId(userId);
        return followUserIdList
                .stream()
                .map(followUserId -> userRepository.findById(followUserId).get())
                .toList();
    }

    public List<User> getFollowerList(Integer userId) {
        List<Integer> followerUserIdList = followUserRepository.findFollowerListByUserId(userId);
        return followerUserIdList
                .stream()
                .map(followUserId -> userRepository.findById(followUserId).get())
                .toList();
    }
}
