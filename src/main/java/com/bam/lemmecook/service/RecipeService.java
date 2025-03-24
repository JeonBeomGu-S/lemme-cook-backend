package com.bam.lemmecook.service;

import com.bam.lemmecook.entity.Ingredient;
import com.bam.lemmecook.entity.Recipe;
import com.bam.lemmecook.repository.IngredientRepository;
import com.bam.lemmecook.repository.RecipeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<Recipe> getRecipesByIngredients(List<Integer> ingredientIds) {
        return recipeRepository.findRecipesByIngredientIds(ingredientIds);
    }

    public Recipe getRecipeById(Integer id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
    }

    public List<Ingredient> getMissingIngredients(Integer recipeId, List<Integer> selectedIngredientIds) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));

        List<Ingredient> requiredIngredients = ingredientRepository.findIngredientsByRecipeId(recipeId);

        return requiredIngredients.stream()
                .filter(ingredient -> !selectedIngredientIds.contains(ingredient.getId()))
                .collect(Collectors.toList());
    }

    public List<Ingredient> getIngredientsByRecipeId(Integer recipeId) {
        return ingredientRepository.findIngredientsByRecipeId(recipeId);
    }

    public List<Recipe> getRecipesByKeyword(String keyword) {
        return recipeRepository.findByKeyword(keyword);
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }
}