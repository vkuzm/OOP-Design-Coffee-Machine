package com.example.spring.recipe;

import com.example.spring.model.Ingredient;
import java.util.List;

public interface Recipe {

  double getPrice();

  void addIngredient(Ingredient ingredient);

  void removeIngredient(Ingredient ingredient);

  List<Ingredient> getIngredients();
}