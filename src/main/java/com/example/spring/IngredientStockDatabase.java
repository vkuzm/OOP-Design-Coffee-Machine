package com.example.spring;

import com.example.spring.enums.IngredientType;
import com.example.spring.model.IngredientStock;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class IngredientStockDatabase {
  private final Map<IngredientType, IngredientStock> ingredientsStock = new HashMap<>();

  {
    ingredientsStock.put(IngredientType.COFFEE, new IngredientStock(IngredientType.COFFEE, 10, 0.75));
    ingredientsStock.put(IngredientType.DECAF_COFFEE, new IngredientStock(IngredientType.DECAF_COFFEE, 10, 0.75));
    ingredientsStock.put(IngredientType.SUGAR, new IngredientStock(IngredientType.SUGAR, 10, 0.25));
    ingredientsStock.put(IngredientType.CREAM, new IngredientStock(IngredientType.CREAM, 10, 0.25));
    ingredientsStock.put(IngredientType.STREAMED_MILK, new IngredientStock(IngredientType.STREAMED_MILK, 10, 0.35));
    ingredientsStock.put(IngredientType.FOAMED_MILK, new IngredientStock(IngredientType.FOAMED_MILK, 10, 0.35));
    ingredientsStock.put(IngredientType.ESPRESSO, new IngredientStock(IngredientType.ESPRESSO, 10, 1.10));
    ingredientsStock.put(IngredientType.COCOA, new IngredientStock(IngredientType.COCOA, 10, 0.90));
    ingredientsStock.put(IngredientType.WHIPPED_CREAM, new IngredientStock(IngredientType.WHIPPED_CREAM, 10, 1.00));
  }

  public Map<IngredientType, IngredientStock> getIngredientsStock() {
    return ingredientsStock;
  }
}
