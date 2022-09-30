package com.example.spring.mixer;

import com.example.spring.dto.DrinkResponse;
import com.example.spring.exception.NotValidIngredientException;
import com.example.spring.recipe.Recipe;

public interface CoffeeStrategy {

  void setRecipe(Recipe recipe);

  DrinkResponse process() throws NotValidIngredientException;
}
