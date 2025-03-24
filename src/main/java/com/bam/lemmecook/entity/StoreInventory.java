package com.bam.lemmecook.entity;


import com.bam.lemmecook.entity.id.StoreInventoryId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "store_inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreInventory {
    @EmbeddedId
    private StoreInventoryId id;
    private double price;
    private int stock;

    @ManyToOne
    @MapsId("storeId")
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;
}