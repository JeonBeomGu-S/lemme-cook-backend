package com.bam.lemmecook.dto.response;

import com.bam.lemmecook.entity.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseRecipeDTO {
    private int id;
    private int userId;
    private String userName;
    private String name;
    private String description;
    private String instructions;
    private String imageUrl;
    private int prepTime;
    private int servings;
    private Date createdAt;
    private Date updatedAt;
    private List<Ingredient> requiredIngredients;
}
