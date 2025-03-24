package com.bam.lemmecook.repository;

import com.bam.lemmecook.entity.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Integer> {
    void deleteByRecipeId(int recipeId);
}

