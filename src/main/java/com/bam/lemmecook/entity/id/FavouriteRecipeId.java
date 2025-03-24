package com.bam.lemmecook.entity.id;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavouriteRecipeId implements Serializable {
    private Integer userId;
    private Integer recipeId;
}
