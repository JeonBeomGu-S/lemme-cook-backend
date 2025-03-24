package com.bam.lemmecook.controller;

import com.bam.lemmecook.dto.response.ResponseRecipeDTO;
import com.bam.lemmecook.entity.Recipe;
import com.bam.lemmecook.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recommend")
    public ResponseEntity<List<ResponseRecipeDTO>> getRecipesByIngredients(@RequestParam List<Integer> ingredientIds) {
        List<Recipe> recipes = recipeService.getRecipesByIngredients(ingredientIds);
        List<ResponseRecipeDTO> recipeDTOs = recipes.stream()
                .map(recipe -> new ResponseRecipeDTO(
                        recipe.getId(),
                        recipe.getUserId(),
                        recipe.getName(),
                        recipe.getDescription(),
                        recipe.getInstructions(),
                        recipe.getCreatedAt(),
                        recipe.getUpdatedAt()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(recipeDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseRecipeDTO> getRecipeById(@PathVariable Integer id) {
        Recipe recipe = recipeService.getRecipeById(id);

        ResponseRecipeDTO recipeDTO = new ResponseRecipeDTO(
                recipe.getId(),
                recipe.getUserId(),
                recipe.getName(),
                recipe.getDescription(),
                recipe.getInstructions(),
                recipe.getCreatedAt(),
                recipe.getUpdatedAt()
        );

        return ResponseEntity.ok(recipeDTO);
    }
}