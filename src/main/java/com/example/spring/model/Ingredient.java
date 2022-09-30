package com.example.spring.model;

import com.example.spring.enums.IngredientType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Ingredient {

  private final IngredientType ingredientType;
  private final int quantity;
  private final double price;

  @Override
  public String toString() {
    return "Ingredient{" +
        "name='" + ingredientType.getName() + '\'' +
        ", quantity=" + quantity +
        ", price=" + price +
        '}';
  }
}