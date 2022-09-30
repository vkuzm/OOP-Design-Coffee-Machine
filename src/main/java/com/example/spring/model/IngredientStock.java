package com.example.spring.model;

import com.example.spring.enums.IngredientType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IngredientStock {

  private final IngredientType ingredientType;
  private int quantity;
  private final double price;

  public void updateQuantity(int quantity) {
    this.quantity -= quantity;
  }

  @Override
  public String toString() {
    return "Ingredient{" +
        "name='" + ingredientType.getName() + '\'' +
        ", quantity=" + quantity +
        ", price=" + price +
        '}';
  }
}