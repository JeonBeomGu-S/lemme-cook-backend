package com.bam.lemmecook.entity;

import com.bam.lemmecook.entity.id.FollowUserId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "follow_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowUser {
    @EmbeddedId
    private FollowUserId followUserId;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("followUserId")
    @JoinColumn(name = "follow_user_id")
    private User followUser;
}
