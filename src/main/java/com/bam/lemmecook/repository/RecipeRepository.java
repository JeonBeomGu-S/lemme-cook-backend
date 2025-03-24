package com.bam.lemmecook.repository;

import com.bam.lemmecook.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    @Query("""
        SELECT DISTINCT r FROM Recipe r 
        JOIN RecipeIngredient ri ON r.id = ri.recipe.id
        WHERE ri.id.ingredientId IN :ingredientIds
    """)
    List<Recipe> findRecipesByIngredientIds(@Param("ingredientIds") List<Integer> ingredientIds);
}