package com.example.spring.dto;

import com.example.spring.enums.IngredientType;
import lombok.Data;

@Data
public class IngredientRequest {
  private IngredientType ingredientType;
  private int quantity;
}
