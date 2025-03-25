package com.bam.lemmecook.controller;

import com.bam.lemmecook.dto.request.RequestRecipeDTO;
import com.bam.lemmecook.dto.response.ResponseGeminiAnswerDTO;
import com.bam.lemmecook.dto.response.ResponseIngredientDTO;
import com.bam.lemmecook.dto.response.ResponseRecipeDTO;
import com.bam.lemmecook.entity.Ingredient;
import com.bam.lemmecook.entity.Recipe;
import com.bam.lemmecook.security.UserDetails;
import com.bam.lemmecook.service.GeminiService;
import com.bam.lemmecook.service.RecipeService;
import com.bam.lemmecook.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
                .map(ingredient -> new ResponseIngredientDTO(
                        ingredient.getId(),
                        ingredient.getName(),
                        ingredient.getImageUrl()))
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

    @PostMapping
    public ResponseEntity<?> createRecipe(@RequestBody RequestRecipeDTO recipeDTO) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId = userDetails.getId();

        recipeService.createRecipe(recipeDTO, userId);
        return ResponseEntity.ok("Created successfully!");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateRecipe(@PathVariable Integer id, @RequestBody RequestRecipeDTO recipeDTO) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId = userDetails.getId();

        recipeService.updateRecipe(id, recipeDTO, userId);
        return ResponseEntity.ok("Updated successfully!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Integer id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId = userDetails.getId();

        recipeService.deleteRecipe(id, userId);
        return ResponseEntity.ok("Deleted successfully!");
    }

    @PostMapping("/{id}/favourites")
    public ResponseEntity<?> addFavouriteRecipe(@PathVariable Integer id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId = userDetails.getId();

        recipeService.addFavouriteRecipe(userId, id);
        return ResponseEntity.ok("Added a favourite recipe successfully!");
    }

    @GetMapping("/favourites")
    public ResponseEntity<List<ResponseRecipeDTO>> favouriteRecipes() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId = userDetails.getId();

        List<Recipe> favouriteRecipeList = recipeService.getFavouriteRecipes(userId);
        List<ResponseRecipeDTO> responseRecipeDTOs = favouriteRecipeList.stream().map(recipe -> new ResponseRecipeDTO(
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
        )).toList();
        return ResponseEntity.ok(responseRecipeDTOs);
    }
}