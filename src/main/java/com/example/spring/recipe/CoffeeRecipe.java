package com.example.spring.recipe;

import com.example.spring.model.Ingredient;
import java.util.ArrayList;
import java.util.List;

public class CoffeeRecipe implements Recipe {
  private final List<Ingredient> ingredients = new ArrayList<>();
  private double totalPrice;

  @Override
  public double getPrice() {
    return totalPrice;
  }

  @Override
  public void addIngredient(Ingredient ingredient) {
    ingredients.add(ingredient);
    totalPrice += ingredient.getPrice();
  }

  @Override
  public void removeIngredient(Ingredient ingredient) {
    ingredients.remove(ingredient);
    totalPrice -= ingredient.getPrice();
  }

  @Override
  public List<Ingredient> getIngredients() {
    return ingredients;
  }

  @Override
  public String toString() {
    return ingredients.toString();
  }
}