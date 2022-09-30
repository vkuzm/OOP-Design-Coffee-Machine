package com.example.spring.model;

import com.example.spring.enums.DrinkType;
import com.example.spring.recipe.Recipe;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public abstract class Drink {
  private final String name;
  private final DrinkType drinkType;
  private Recipe recipe;

  @Override
  public String toString() {
    return "Drink{" +
        "name='" + name + '\'' +
        ", drinkType=" + drinkType +
        ", recipe=" + recipe +
        '}';
  }
}
