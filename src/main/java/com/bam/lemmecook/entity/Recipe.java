package com.bam.lemmecook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "recipes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int userId;
    private String name;
    private String description;
    private String instructions;
    private String imageUrl;
    private int prepTime;
    private int servings;
    private Date createdAt;
    private Date updatedAt;
}
