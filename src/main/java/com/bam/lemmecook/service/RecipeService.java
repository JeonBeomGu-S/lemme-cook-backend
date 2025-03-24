package com.bam.lemmecook.service;

import com.bam.lemmecook.entity.Recipe;
import com.bam.lemmecook.repository.RecipeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> getRecipesByIngredients(List<Integer> ingredientIds) {
        return recipeRepository.findRecipesByIngredientIds(ingredientIds);
    }

    public Recipe getRecipeById(Integer id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
    }
}