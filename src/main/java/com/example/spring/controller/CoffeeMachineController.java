package com.example.spring.controller;

import com.example.spring.IngredientStockDatabase;
import com.example.spring.dto.DrinkRequest;
import com.example.spring.dto.IngredientRequest;
import com.example.spring.dto.RefillRequest;
import com.example.spring.enums.DrinkType;
import com.example.spring.enums.IngredientType;
import com.example.spring.exception.NotValidIngredientException;
import com.example.spring.mixer.CoffeeStrategy;
import com.example.spring.mixer.Mixer;
import com.example.spring.model.Ingredient;
import com.example.spring.model.IngredientStock;
import com.example.spring.recipe.CoffeeRecipe;
import com.example.spring.recipe.Recipe;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CoffeeMachineController {

  private final Mixer mixer;
  private final Map<String, CoffeeStrategy> coffeeStrategies;
  private final IngredientStockDatabase ingredientStockDatabase;

  @Value("${error_messages.drink_not_exist}")
  private String drinkNotExistMessage;

  @Value("${error_messages.ingredient_not_exist}")
  private String ingredientNotExistMessage;

  @PostMapping("/make-coffee")
  public ResponseEntity<?> makeCoffee(@RequestBody DrinkRequest drinkRequest) {
    Map<IngredientType, IngredientStock> ingredientsStock = ingredientStockDatabase.getIngredientsStock();

    if (!DrinkType.isValid(drinkRequest.getDrinkType())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(drinkNotExistMessage);
    }

    DrinkType drinkType = DrinkType.valueOf(drinkRequest.getDrinkType());

    if (!coffeeStrategies.containsKey(drinkType.name())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(drinkNotExistMessage);
    }

    Recipe coffeeRecipe = new CoffeeRecipe();

    for (IngredientRequest ingredientReq : drinkRequest.getIngredients()) {
      IngredientType ingredientType = ingredientReq.getIngredientType();

      if (!ingredientsStock.containsKey(ingredientType)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ingredientNotExistMessage);
      }

      IngredientStock ingredientStock = ingredientsStock.get(ingredientType);
      Ingredient ingredient = Ingredient.builder()
          .ingredientType(ingredientStock.getIngredientType())
          .quantity(ingredientReq.getQuantity())
          .price(ingredientStock.getPrice())
          .build();

      coffeeRecipe.addIngredient(ingredient);
    }

    CoffeeStrategy coffeeStrategy = coffeeStrategies.get(drinkType.name());
    coffeeStrategy.setRecipe(coffeeRecipe);

    mixer.setCoffeeStrategy(coffeeStrategy);

    try {
      return ResponseEntity.ok(mixer.make());

    } catch (NotValidIngredientException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @PostMapping("/refill")
  public ResponseEntity<String> refill(@RequestBody RefillRequest refillRequest) {
    Map<IngredientType, IngredientStock> ingredientsStock = ingredientStockDatabase.getIngredientsStock();
    IngredientType ingredientType = refillRequest.getIngredientType();

    if (!ingredientsStock.containsKey(ingredientType)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ingredientNotExistMessage);
    }

    ingredientsStock.get(ingredientType).setQuantity(refillRequest.getQuantity());
    return ResponseEntity.ok().build();
  }
}