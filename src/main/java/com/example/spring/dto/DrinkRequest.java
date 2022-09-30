package com.example.spring.dto;

import java.util.List;
import lombok.Data;

@Data
public class DrinkRequest {
  private String drinkType;
  private List<IngredientRequest> ingredients;
}
