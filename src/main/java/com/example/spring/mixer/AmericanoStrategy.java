package com.example.spring.mixer;

import com.example.spring.dto.DrinkResponse;
import com.example.spring.enums.DrinkType;
import com.example.spring.enums.IngredientType;
import com.example.spring.exception.NotValidIngredientException;
import com.example.spring.model.Americano;
import com.example.spring.recipe.Recipe;
import com.example.spring.validator.IngredientValidator;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("AMERICANO")
@RequiredArgsConstructor
public class AmericanoStrategy implements CoffeeStrategy {

  private Recipe recipe;
  private final IngredientValidator ingredientValidator;
  private final Set<IngredientType> validIngredients = Set.of(
      IngredientType.COFFEE,
      IngredientType.DECAF_COFFEE,
      IngredientType.SUGAR
  );

  @Override
  public void setRecipe(Recipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public DrinkResponse process() throws NotValidIngredientException {
    ingredientValidator.validate(DrinkType.AMERICANO, validIngredients, recipe);

    Americano americano = new Americano();
    americano.setRecipe(recipe);

    return new DrinkResponse(americano.getDrinkType(), recipe.getIngredients(), recipe.getPrice());
  }
}
