package com.bam.lemmecook.controller;

import com.bam.lemmecook.dto.response.ResponseIngredientDTO;
import com.bam.lemmecook.dto.response.ResponseStoreDTO;
import com.bam.lemmecook.service.IngredientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {
    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public ResponseEntity<List<ResponseIngredientDTO>> getIngredients() {
        List<ResponseIngredientDTO> ingredientDTOs = ingredientService.getIngredientList().stream()
                .map(ingredient -> new ResponseIngredientDTO(
                        ingredient.getId(),
                        ingredient.getName(),
                        ingredient.getImageUrl()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(ingredientDTOs);
    }

    @GetMapping("/store")
    public ResponseEntity<List<ResponseStoreDTO>> getStoresSellingIngredients(
            @RequestParam List<Integer> missingIngredientList) {

        List<ResponseStoreDTO> responseStoreDTOs = ingredientService.getStoresSellingIngredients(missingIngredientList);

        return ResponseEntity.ok(responseStoreDTOs);
    }
}
