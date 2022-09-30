package com.example.spring.dto;

import com.example.spring.enums.IngredientType;
import lombok.Data;

@Data
public class RefillRequest {
  private IngredientType ingredientType;
  private int quantity;
}
