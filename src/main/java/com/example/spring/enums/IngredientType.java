package com.example.spring.enums;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum IngredientType {
  COFFEE("Coffee"),
  DECAF_COFFEE("Decaf Coffee"),
  SUGAR("Sugar"),
  CREAM("Cream"),
  STREAMED_MILK("Streamed Milk"),
  FOAMED_MILK("Foamed Milk"),
  ESPRESSO("Espresso"),
  COCOA("Cocoa"),
  WHIPPED_CREAM("Whipped Cream");

  private final String name;

  IngredientType(String name) {
    this.name = name;
  }

  public static IngredientType getValue(String type) {
    return Arrays.stream(IngredientType.values())
        .filter(ingredientType -> ingredientType.name().equals(type))
        .findFirst()
        .orElse(null);
  }
}