package com.bam.lemmecook.entity;


import com.bam.lemmecook.entity.id.StoreInventoryId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
}