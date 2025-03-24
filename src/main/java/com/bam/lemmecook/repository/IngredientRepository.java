package com.bam.lemmecook.repository;

import com.bam.lemmecook.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    @Query("""
        SELECT DISTINCT i FROM Ingredient i 
        JOIN RecipeIngredient ri ON i.id = ri.ingredient.id
        WHERE ri.id.recipeId = :recipeId
    """)
    List<Ingredient> findIngredientsByRecipeId(@Param("recipeId") Integer recipeId);
}