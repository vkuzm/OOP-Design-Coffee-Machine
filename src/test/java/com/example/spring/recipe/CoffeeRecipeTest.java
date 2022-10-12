package com.example.spring.recipe;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.spring.enums.IngredientType;
import com.example.spring.model.Ingredient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CoffeeRecipeTest {

  @Test
  @DisplayName("Should add new ingredients and calculate total price")
  public void shouldAddNewIngredientsAndCalculateTotalPrice() {
    Recipe coffeeRecipe = new CoffeeRecipe();

    Ingredient ingredientCoffee = Ingredient.builder()
        .ingredientType(IngredientType.COFFEE)
        .quantity(5)
        .price(0.5)
        .build();

    Ingredient ingredientSugar = Ingredient.builder()
        .ingredientType(IngredientType.SUGAR)
        .quantity(2)
        .price(0.3)
        .build();

    coffeeRecipe.addIngredient(ingredientCoffee);
    coffeeRecipe.addIngredient(ingredientSugar);

    assertThat(coffeeRecipe.getPrice()).isEqualTo(0.8);
  }

  @Test
  @DisplayName("Should add new ingredients then remove some and calculate total price")
  public void shouldAddNewIngredientsThenRemoveSomeAndCalculateTotalPrice() {
    Recipe coffeeRecipe = new CoffeeRecipe();

    Ingredient ingredientCoffee = Ingredient.builder()
        .ingredientType(IngredientType.COFFEE)
        .quantity(1)
        .price(2)
        .build();

    Ingredient ingredientSugar = Ingredient.builder()
        .ingredientType(IngredientType.SUGAR)
        .quantity(1)
        .price(1)
        .build();

    Ingredient ingredientMilk = Ingredient.builder()
        .ingredientType(IngredientType.STREAMED_MILK)
        .quantity(1)
        .price(1)
        .build();

    coffeeRecipe.addIngredient(ingredientCoffee);
    coffeeRecipe.addIngredient(ingredientSugar);
    coffeeRecipe.addIngredient(ingredientMilk);
    coffeeRecipe.removeIngredient(ingredientSugar);

    assertThat(coffeeRecipe.getPrice()).isEqualTo(3);
  }

  @Test
  @DisplayName("Should add new ingredients and get total ingredients")
  public void shouldAddNewIngredientsAndGetTotalIngredients() {
    Recipe coffeeRecipe = new CoffeeRecipe();

    Ingredient ingredientCoffee = Ingredient.builder()
        .ingredientType(IngredientType.COFFEE)
        .quantity(5)
        .price(0.5)
        .build();

    Ingredient ingredientSugar = Ingredient.builder()
        .ingredientType(IngredientType.SUGAR)
        .quantity(2)
        .price(0.3)
        .build();

    coffeeRecipe.addIngredient(ingredientCoffee);
    coffeeRecipe.addIngredient(ingredientSugar);

    assertThat(coffeeRecipe.getIngredients().size()).isEqualTo(2);
  }
}