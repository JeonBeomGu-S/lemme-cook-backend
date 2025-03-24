package com.bam.lemmecook.entity.id;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreInventoryId {
    private Integer storeId;
    private Integer ingredientId;
}