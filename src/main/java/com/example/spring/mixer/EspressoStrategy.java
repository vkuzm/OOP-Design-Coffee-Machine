package com.example.spring.mixer;

import com.example.spring.dto.DrinkResponse;
import com.example.spring.enums.DrinkType;
import com.example.spring.enums.IngredientType;
import com.example.spring.exception.NotValidIngredientException;
import com.example.spring.model.Espresso;
import com.example.spring.recipe.Recipe;
import com.example.spring.validator.IngredientValidator;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("ESPRESSO")
@RequiredArgsConstructor
public class EspressoStrategy implements CoffeeStrategy {
  private Recipe recipe;
  private final IngredientValidator ingredientValidator;
  private final Set<IngredientType> validIngredients = Set.of(
      IngredientType.COFFEE,
      IngredientType.ESPRESSO,
      IngredientType.SUGAR
  );

  @Override
  public void setRecipe(Recipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public DrinkResponse process() throws NotValidIngredientException {
    ingredientValidator.validate(DrinkType.ESPRESSO, validIngredients, recipe);

    Espresso espresso = new Espresso();
    espresso.setRecipe(recipe);

    return new DrinkResponse(espresso.getDrinkType(), recipe.getIngredients(), recipe.getPrice());
  }
}

