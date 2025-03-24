package com.bam.lemmecook.entity;

import com.bam.lemmecook.entity.id.FavouriteRecipeId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "favourite_recipes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavouriteRecipe {
    @EmbeddedId
    private FavouriteRecipeId id;
}