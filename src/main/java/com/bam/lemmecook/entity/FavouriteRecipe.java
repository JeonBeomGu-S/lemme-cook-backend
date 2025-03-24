package com.bam.lemmecook.entity;

import com.bam.lemmecook.entity.id.FavouriteRecipeId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favourite_recipes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavouriteRecipe {
    @EmbeddedId
    private FavouriteRecipeId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}