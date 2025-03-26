package com.bam.lemmecook.repository;

import com.bam.lemmecook.dto.response.ResponseStoreDTO;
import com.bam.lemmecook.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {
    @Query("SELECT new com.bam.lemmecook.dto.response.ResponseStoreDTO(s.id, s.name, s.address, s.latitude, s.longitude, s.distance, i.id, i.name, si.price, si.stock) " +
            "FROM Store s " +
            "JOIN StoreInventory si ON s.id = si.store.id " +
            "JOIN Ingredient i ON si.ingredient.id = i.id " +
            "WHERE si.ingredient.id IN :ingredientIds")
    List<ResponseStoreDTO> findStoresByIngredientIds(@Param("ingredientIds") List<Integer> ingredientIds);
}