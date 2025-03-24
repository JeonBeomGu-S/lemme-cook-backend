package com.bam.lemmecook.controller;

import com.bam.lemmecook.dto.response.ResponseIngredientDTO;
import com.bam.lemmecook.dto.response.ResponseRecipeDTO;
import com.bam.lemmecook.entity.Ingredient;
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
                        recipe.getUpdatedAt(),
                        recipeService.getIngredientsByRecipeId(recipe.getId())
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
                recipe.getUpdatedAt(),
                recipeService.getIngredientsByRecipeId(recipe.getId())
        );

        return ResponseEntity.ok(recipeDTO);
    }

    @GetMapping("/{id}/missing-ingredients")
    public ResponseEntity<List<ResponseIngredientDTO>> getMissingIngredients(
            @PathVariable Integer id,
            @RequestParam List<Integer> selectedIngredientList) {

        List<Ingredient> missingIngredients = recipeService.getMissingIngredients(id, selectedIngredientList);
        List<ResponseIngredientDTO> ingredientDTOs = missingIngredients.stream()
                .map(ingredient -> new ResponseIngredientDTO(ingredient.getId(), ingredient.getName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(ingredientDTOs);
    }
}