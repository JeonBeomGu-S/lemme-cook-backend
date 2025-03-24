package com.bam.lemmecook.service;

import com.bam.lemmecook.entity.Ingredient;
import com.bam.lemmecook.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientRepository.findAll();
    }
}
