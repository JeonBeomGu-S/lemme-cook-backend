package com.bam.lemmecook.repository;

import com.bam.lemmecook.entity.FollowUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowUserRepository extends JpaRepository<FollowUser, Integer> {
    @Query("""
    SELECT u.followUser.id FROM FollowUser u WHERE u.user.id = :userId
    """)
    List<Integer> findFollowListByUserId(Integer userId);

    @Query("""
    SELECT u.user.id FROM FollowUser u WHERE u.followUser.id = :userId
    """)
    List<Integer> findFollowerListByUserId(Integer userId);
}
