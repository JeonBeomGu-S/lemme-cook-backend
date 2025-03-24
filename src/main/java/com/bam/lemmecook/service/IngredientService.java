package com.bam.lemmecook.service;

import com.bam.lemmecook.dto.response.ResponseStoreDTO;
import com.bam.lemmecook.entity.Ingredient;
import com.bam.lemmecook.repository.IngredientRepository;
import com.bam.lemmecook.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final StoreRepository storeRepository;

    public IngredientService(IngredientRepository ingredientRepository, StoreRepository storeRepository) {
        this.ingredientRepository = ingredientRepository;
        this.storeRepository = storeRepository;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientRepository.findAll();
    }

    public List<ResponseStoreDTO> getStoresSellingIngredients(List<Integer> missingIngredientIds) {
        return storeRepository.findStoresByIngredientIds(missingIngredientIds);
    }
}
