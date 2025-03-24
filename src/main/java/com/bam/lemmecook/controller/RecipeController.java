package com.bam.lemmecook.controller;

import com.bam.lemmecook.dto.response.ResponseGeminiAnswerDTO;
import com.bam.lemmecook.dto.response.ResponseIngredientDTO;
import com.bam.lemmecook.dto.response.ResponseRecipeDTO;
import com.bam.lemmecook.entity.Ingredient;
import com.bam.lemmecook.entity.Recipe;
import com.bam.lemmecook.service.GeminiService;
import com.bam.lemmecook.service.RecipeService;
import com.bam.lemmecook.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeService recipeService;
    private final GeminiService geminiService;
    private final UserService userService;

    public RecipeController(RecipeService recipeService, GeminiService geminiService, UserService userService) {
        this.recipeService = recipeService;
        this.geminiService = geminiService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<ResponseRecipeDTO>> getAllRecipes() {
        List<Recipe> recipes = recipeService.getAllRecipes();
        List<ResponseRecipeDTO> recipeDTOs = recipes.stream()
                .map(recipe -> new ResponseRecipeDTO(
                        recipe.getId(),
                        recipe.getUserId(),
                        userService.getUserNameById(recipe.getUserId()),
                        recipe.getName(),
                        recipe.getDescription(),
                        recipe.getInstructions(),
                        recipe.getImageUrl(),
                        recipe.getPrepTime(),
                        recipe.getServings(),
                        recipe.getCreatedAt(),
                        recipe.getUpdatedAt(),
                        recipeService.getIngredientsByRecipeId(recipe.getId())
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(recipeDTOs);
    }

    @GetMapping("/recommend")
    public ResponseEntity<List<ResponseRecipeDTO>> getRecipesByIngredients(@RequestParam List<Integer> ingredientIds) {
        List<Recipe> recipes = recipeService.getRecipesByIngredients(ingredientIds);
        List<ResponseRecipeDTO> recipeDTOs = recipes.stream()
                .map(recipe -> new ResponseRecipeDTO(
                        recipe.getId(),
                        recipe.getUserId(),
                        userService.getUserNameById(recipe.getUserId()),
                        recipe.getName(),
                        recipe.getDescription(),
                        recipe.getInstructions(),
                        recipe.getImageUrl(),
                        recipe.getPrepTime(),
                        recipe.getServings(),
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
                userService.getUserNameById(recipe.getUserId()),
                recipe.getName(),
                recipe.getDescription(),
                recipe.getInstructions(),
                recipe.getImageUrl(),
                recipe.getPrepTime(),
                recipe.getServings(),
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

    @GetMapping("/search")
    public ResponseEntity<List<ResponseRecipeDTO>> searchRecipes(@RequestParam String keyword) {
        List<Recipe> recipes = recipeService.getRecipesByKeyword(keyword);
        List<ResponseRecipeDTO> responseRecipeDTOs = recipes.stream()
                .map(recipe -> new ResponseRecipeDTO(
                        recipe.getId(),
                        recipe.getUserId(),
                        userService.getUserNameById(recipe.getUserId()),
                        recipe.getName(),
                        recipe.getDescription(),
                        recipe.getInstructions(),
                        recipe.getImageUrl(),
                        recipe.getPrepTime(),
                        recipe.getServings(),
                        recipe.getCreatedAt(),
                        recipe.getUpdatedAt(),
                        recipeService.getIngredientsByRecipeId(recipe.getId()))
                )
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseRecipeDTOs);
    }

    @GetMapping("/generate")
    public ResponseEntity<ResponseGeminiAnswerDTO> generateResponse(@RequestParam String prompt) {
        return ResponseEntity.ok(geminiService.getGeminiResponse(prompt));
    }
}