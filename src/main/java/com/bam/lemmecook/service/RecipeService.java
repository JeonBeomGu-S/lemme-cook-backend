package com.bam.lemmecook.service;

import com.bam.lemmecook.dto.request.RequestRecipeDTO;
import com.bam.lemmecook.entity.*;
import com.bam.lemmecook.entity.id.FavouriteRecipeId;
import com.bam.lemmecook.entity.id.RecipeIngredientId;
import com.bam.lemmecook.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final FavouriteRecipeRepository favouriteRecipeRepository;
    private final UserRepository userRepository;

    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository,
                         RecipeIngredientRepository recipeIngredientRepository,
                         FavouriteRecipeRepository favouriteRecipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.favouriteRecipeRepository = favouriteRecipeRepository;
        this.userRepository = userRepository;
    }

    public List<Recipe> getRecipesByIngredients(List<Integer> ingredientIds) {
        return recipeRepository.findRecipesByIngredientIds(ingredientIds);
    }

    public Recipe getRecipeById(Integer id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
    }

    public List<Ingredient> getMissingIngredients(Integer recipeId, List<Integer> selectedIngredientIds) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));

        List<Ingredient> requiredIngredients = ingredientRepository.findIngredientsByRecipeId(recipeId);

        return requiredIngredients.stream()
                .filter(ingredient -> !selectedIngredientIds.contains(ingredient.getId()))
                .collect(Collectors.toList());
    }

    public List<Ingredient> getIngredientsByRecipeId(Integer recipeId) {
        return ingredientRepository.findIngredientsByRecipeId(recipeId);
    }

    public List<Recipe> getRecipesByKeyword(String keyword) {
        return recipeRepository.findByKeyword(keyword);
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @Transactional
    public void createRecipe(RequestRecipeDTO requestRecipeDTO, int userId) {
        // create a new recipe and save it
        Recipe recipe = recipeRepository.save(
                new Recipe(
                        null,
                        userId,
                        requestRecipeDTO.getName(),
                        requestRecipeDTO.getDescription(),
                        requestRecipeDTO.getInstructions(),
                        requestRecipeDTO.getImageUrl(),
                        requestRecipeDTO.getPrepTime(),
                        requestRecipeDTO.getServings(),
                        new Date(),
                        new Date()
                )
        );

        // get generated id
        int recipeId = recipe.getId();

        // create a required ingredient list
        List<RecipeIngredient> recipeIngredients = requestRecipeDTO.getRequiredIngredients()
                .stream()
                .map(requiredIngredient -> {
                            int ingredientId = requiredIngredient.getId();
                            Optional<Ingredient> ingredient = ingredientRepository.findById(ingredientId);
                            if (ingredient.isEmpty()) {
                                throw new EntityNotFoundException("Ingredient not found with id " + ingredientId);
                            }

                            return new RecipeIngredient(
                                    new RecipeIngredientId(recipeId, ingredientId),
                                    requiredIngredient.getQuantity(),
                                    recipe,
                                    ingredient.get()
                            );
                        }
                )
                .toList();

        // save the required ingredient list
        List<RecipeIngredient> savedRecipeIngredients = recipeIngredientRepository.saveAll(recipeIngredients);
        if (savedRecipeIngredients.size() != recipeIngredients.size()) {
            throw new RuntimeException("Failed to save all Recipe Ingredients");
        }
    }

    @Transactional
    public void updateRecipe(Integer recipeId, RequestRecipeDTO requestRecipeDTO, int userId) {
        // find the recipe to update
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
        if (optionalRecipe.isEmpty()) {
            throw new EntityNotFoundException("Recipe not found with id " + recipeId);
        }

        Recipe recipe = optionalRecipe.get();

        // throw error if author and the person editing are different
        if (recipe.getUserId() != userId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        // set values to update
        recipe.setUserId(userId);
        recipe.setName(requestRecipeDTO.getName());
        recipe.setDescription(requestRecipeDTO.getDescription());
        recipe.setInstructions(requestRecipeDTO.getInstructions());
        recipe.setImageUrl(requestRecipeDTO.getImageUrl());
        recipe.setPrepTime(requestRecipeDTO.getPrepTime());
        recipe.setServings(requestRecipeDTO.getServings());

        // save the recipe
        recipeRepository.save(recipe);
        // delete existing ingredient from the recipe_ingredient table
        recipeIngredientRepository.deleteByRecipeId(recipeId);

        // create new required ingredient list
        List<RecipeIngredient> recipeIngredients = requestRecipeDTO.getRequiredIngredients()
                .stream()
                .map(requiredIngredient -> {
                            int ingredientId = requiredIngredient.getId();
                            Optional<Ingredient> ingredient = ingredientRepository.findById(ingredientId);

                            if (ingredient.isEmpty()) {
                                throw new EntityNotFoundException("Ingredient not found with id " + ingredientId);
                            }

                            return new RecipeIngredient(
                                    new RecipeIngredientId(recipeId, ingredientId),
                                    requiredIngredient.getQuantity(),
                                    recipe,
                                    ingredient.get()
                            );
                        }
                )
                .toList();

        // save new required ingredient list
        List<RecipeIngredient> savedRecipeIngredients = recipeIngredientRepository.saveAll(recipeIngredients);
        if (savedRecipeIngredients.size() != recipeIngredients.size()) {
            throw new RuntimeException("Failed to save all Recipe Ingredients");
        }
    }

    @Transactional
    public void deleteRecipe(Integer recipeId, int userId) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
        if (optionalRecipe.isEmpty()) {
            throw new EntityNotFoundException("Recipe not found with id " + recipeId);
        }
        Recipe recipe = optionalRecipe.get();

        // throw error if author and the person editing are different
        if (recipe.getUserId() != userId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        recipeIngredientRepository.deleteByRecipeId(recipeId);
        recipeRepository.delete(recipe);
    }

    public void addFavouriteRecipe(Integer userId, Integer recipeId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("User not found with id " + userId);
        }

        if (optionalRecipe.isEmpty()) {
            throw new EntityNotFoundException("Recipe not found with id " + recipeId);
        }

        favouriteRecipeRepository.save(new FavouriteRecipe(
                new FavouriteRecipeId(userId, recipeId),
                optionalUser.get(),
                optionalRecipe.get()
        ));
    }

    @Transactional
    public List<Recipe> getFavouriteRecipes(Integer userId) {
        Optional<List<FavouriteRecipe>> optionalFavouriteRecipes = favouriteRecipeRepository.findAllByUserId(userId);
        if (optionalFavouriteRecipes.isEmpty()) {
            throw new EntityNotFoundException("No favourite recipes found");
        }

        List<FavouriteRecipe> favouriteRecipes = optionalFavouriteRecipes.get();

        return favouriteRecipes.stream().map(favouriteRecipe -> {
            Optional<Recipe> recipe = recipeRepository.findById(favouriteRecipe.getId().getRecipeId());
            if (recipe.isEmpty()) {
                throw new EntityNotFoundException("Recipe not found with id " + favouriteRecipe.getId());
            }
            return recipe.get();
        }).toList();
    }
}