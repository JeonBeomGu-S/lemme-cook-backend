package com.bam.lemmecook.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestRecipeDTO {
    private String name;
    private String description;
    private String instructions;
    private String imageUrl;
    private int prepTime;
    private int servings;
    private List<RequestRequiredIngredientDTO> requiredIngredients;
}
