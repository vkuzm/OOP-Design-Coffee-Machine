package com.example.spring.validator;

import com.example.spring.IngredientStockDatabase;
import com.example.spring.enums.DrinkType;
import com.example.spring.enums.IngredientType;
import com.example.spring.exception.NotValidIngredientException;
import com.example.spring.model.Ingredient;
import com.example.spring.model.IngredientStock;
import com.example.spring.recipe.Recipe;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IngredientValidator {

  private final IngredientStockDatabase ingredientStockDatabase;

  @Value("${error_messages.ingredient_not_valid}")
  private String ingredientNotValidMessage;

  @Value("${error_messages.ingredient_low_quantity}")
  private String ingredientLowQuantityMessage;

  public void validate(DrinkType drinkType, Set<IngredientType> validIngredients, Recipe recipe)
      throws NotValidIngredientException {
    Map<IngredientType, IngredientStock> ingredientsStock = ingredientStockDatabase.getIngredientsStock();

    for (Ingredient ingredient : recipe.getIngredients()) {
      IngredientStock ingredientStock = ingredientsStock.get(ingredient.getIngredientType());

      if (ingredientStock.getQuantity() < ingredient.getQuantity()) {
        throw new NotValidIngredientException(getLowQuantityMessage(ingredientStock));

      } else if (!validIngredients.contains(ingredient.getIngredientType())) {
        throw new NotValidIngredientException(getIngredientTypeNotValidMessage(drinkType, ingredient));
      }

      ingredientStock.updateQuantity(ingredient.getQuantity());
    }
  }

  private String getLowQuantityMessage(IngredientStock ingredientStock) {
    return String.format(ingredientLowQuantityMessage, ingredientStock.getQuantity());
  }

  private String getIngredientTypeNotValidMessage(DrinkType drinkType, Ingredient ingredient) {
    return String.format(ingredientNotValidMessage, ingredient.getIngredientType().getName(), drinkType.name());
  }
}
