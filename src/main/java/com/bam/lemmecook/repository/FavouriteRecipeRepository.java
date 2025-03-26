package com.bam.lemmecook.repository;

import com.bam.lemmecook.entity.FavouriteRecipe;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FavouriteRecipeRepository extends JpaRepository<FavouriteRecipe, Integer> {
    @Query("""
    SELECT r FROM FavouriteRecipe r
    WHERE r.user.id = :userId
    """)
    Optional<List<FavouriteRecipe>> findAllByUserId(int userId);

    @Transactional
    void deleteByUserIdAndRecipeId(Integer userId, Integer recipeId);
}