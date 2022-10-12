package com.example.spring.mixer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;

import com.example.spring.dto.DrinkResponse;
import com.example.spring.enums.DrinkType;
import com.example.spring.enums.IngredientType;
import com.example.spring.exception.NotValidIngredientException;
import com.example.spring.model.Ingredient;
import com.example.spring.recipe.CoffeeRecipe;
import com.example.spring.recipe.Recipe;
import com.example.spring.validator.IngredientValidator;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AmericanoStrategyTest {

  @Mock
  private IngredientValidator ingredientValidator;

  @InjectMocks
  private AmericanoStrategy americanoStrategy;

  @BeforeEach
  public void setUp() {
    Ingredient ingredientCoffee = Ingredient.builder()
        .ingredientType(IngredientType.COFFEE)
        .quantity(1)
        .price(2)
        .build();

    Ingredient ingredientSugar = Ingredient.builder()
        .ingredientType(IngredientType.SUGAR)
        .quantity(1)
        .price(1)
        .build();

    Recipe coffeeRecipe = new CoffeeRecipe();
    coffeeRecipe.addIngredient(ingredientCoffee);
    coffeeRecipe.addIngredient(ingredientSugar);

    americanoStrategy.setRecipe(coffeeRecipe);
  }

  @Test
  @DisplayName("Should process drink")
  public void shouldProcessDrink() throws NotValidIngredientException {
    DrinkResponse drinkResponse = americanoStrategy.process();

    assertThat(drinkResponse.getDrinkType()).isEqualTo(DrinkType.AMERICANO);
    assertThat(drinkResponse.getPrice()).isEqualTo(3);
    assertThat(drinkResponse.getIngredients().size()).isEqualTo(2);
  }

  @Test
  @DisplayName("Should process drink with valid ingredients")
  public void shouldProcessDrinkWithValidIngredients() throws NotValidIngredientException {
    Set<IngredientType> validIngredients = Set.of(
        IngredientType.COFFEE,
        IngredientType.DECAF_COFFEE,
        IngredientType.SUGAR
    );

    americanoStrategy.process();

    verify(ingredientValidator).validate(eq(DrinkType.AMERICANO), eq(validIngredients), any(Recipe.class));
  }

  @Test
  @DisplayName("Should throw NotValidIngredientException while process drink")
  public void shouldThrowNotValidIngredientExceptionWhileProcessDrink() throws NotValidIngredientException {
    doThrow(NotValidIngredientException.class)
            .when(ingredientValidator).validate(any(DrinkType.class), anySet(), any(Recipe.class));

    assertThrows(NotValidIngredientException.class, () -> americanoStrategy.process());
  }
}