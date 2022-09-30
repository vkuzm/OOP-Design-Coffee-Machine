package com.example.spring.dto;

import com.example.spring.enums.DrinkType;
import com.example.spring.model.Ingredient;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DrinkResponse {

  private DrinkType drinkType;
  private List<Ingredient> ingredients;
  private double price;
}
