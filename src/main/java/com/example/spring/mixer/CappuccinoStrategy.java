package com.example.spring.mixer;

import com.example.spring.dto.DrinkResponse;
import com.example.spring.enums.DrinkType;
import com.example.spring.enums.IngredientType;
import com.example.spring.exception.NotValidIngredientException;
import com.example.spring.model.Cappuccino;
import com.example.spring.recipe.Recipe;
import com.example.spring.validator.IngredientValidator;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("CAPPUCCINO")
@RequiredArgsConstructor
public class CappuccinoStrategy implements CoffeeStrategy {
  private Recipe recipe;
  private final IngredientValidator ingredientValidator;
  private final Set<IngredientType> validIngredients = Set.of(
      IngredientType.COFFEE,
      IngredientType.DECAF_COFFEE,
      IngredientType.SUGAR,
      IngredientType.CREAM,
      IngredientType.STREAMED_MILK,
      IngredientType.WHIPPED_CREAM
  );

  @Override
  public void setRecipe(Recipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public DrinkResponse process() throws NotValidIngredientException {
    ingredientValidator.validate(DrinkType.CAPPUCCINO, validIngredients, recipe);

    Cappuccino cappuccino = new Cappuccino();
    cappuccino.setRecipe(recipe);

    return new DrinkResponse(cappuccino.getDrinkType(), recipe.getIngredients(), recipe.getPrice());
  }
}
