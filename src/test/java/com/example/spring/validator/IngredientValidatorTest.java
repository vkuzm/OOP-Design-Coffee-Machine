package com.example.spring.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.spring.IngredientStockDatabase;
import com.example.spring.enums.DrinkType;
import com.example.spring.enums.IngredientType;
import com.example.spring.exception.NotValidIngredientException;
import com.example.spring.model.Ingredient;
import com.example.spring.model.IngredientStock;
import com.example.spring.recipe.CoffeeRecipe;
import com.example.spring.recipe.Recipe;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class IngredientValidatorTest {

  private static final String ingredientLowQuantityMessage = "This ingredient is low quantity. Only %s left.";
  private static final String ingredientNotValidMessage = "Not valid ingredient (%s) for %s type of drink";

  @Mock
  private IngredientStockDatabase ingredientStockDatabase;

  @InjectMocks
  private IngredientValidator ingredientValidator;

  @BeforeEach
  public void setUp() {
    ReflectionTestUtils.setField(ingredientValidator, "ingredientLowQuantityMessage", ingredientLowQuantityMessage);
    ReflectionTestUtils.setField(ingredientValidator, "ingredientNotValidMessage", ingredientNotValidMessage);
  }

  @Test
  @DisplayName("Should validate and update quantity")
  public void shouldValidateAndUpdateQuantity() throws NotValidIngredientException {
    Map<IngredientType, IngredientStock> ingredientsStock = getIngredientTypeIngredientStock();

    Set<IngredientType> validIngredients = Set.of(
        IngredientType.COFFEE,
        IngredientType.DECAF_COFFEE,
        IngredientType.SUGAR
    );

    Recipe coffeeRecipe = new CoffeeRecipe();

    IngredientStock ingredientStockCoffee = ingredientsStock.get(IngredientType.COFFEE);
    Ingredient ingredientCoffee = Ingredient.builder()
        .ingredientType(ingredientStockCoffee.getIngredientType())
        .quantity(5)
        .price(ingredientStockCoffee.getPrice())
        .build();

    IngredientStock ingredientStockSugar = ingredientsStock.get(IngredientType.SUGAR);
    Ingredient ingredientSugar = Ingredient.builder()
        .ingredientType(ingredientStockSugar.getIngredientType())
        .quantity(2)
        .price(ingredientStockSugar.getPrice())
        .build();

    coffeeRecipe.addIngredient(ingredientCoffee);
    coffeeRecipe.addIngredient(ingredientSugar);

    Mockito.when(ingredientStockDatabase.getIngredientsStock())
        .thenReturn(ingredientsStock);

    ingredientValidator.validate(DrinkType.AMERICANO, validIngredients, coffeeRecipe);

    assertThat(ingredientsStock.get(IngredientType.COFFEE).getQuantity()).isEqualTo(5);
    assertThat(ingredientsStock.get(IngredientType.SUGAR).getQuantity()).isEqualTo(8);
  }

  @Test
  @DisplayName("Should throw NotValidIngredientException when not enough quantity")
  public void shouldThrowNotValidIngredientExceptionWhenNotEnoughQuantity() {
    Map<IngredientType, IngredientStock> ingredientsStock = getIngredientTypeIngredientStock();
    ingredientsStock.put(IngredientType.ESPRESSO,
        new IngredientStock(IngredientType.ESPRESSO, 2, 0.75));

    Set<IngredientType> validIngredients = Set.of(
        IngredientType.ESPRESSO,
        IngredientType.SUGAR
    );

    Recipe coffeeRecipe = new CoffeeRecipe();

    IngredientStock ingredientStockEspresso = ingredientsStock.get(IngredientType.ESPRESSO);
    Ingredient ingredientEspresso = Ingredient.builder()
        .ingredientType(ingredientStockEspresso.getIngredientType())
        .quantity(4)
        .price(ingredientStockEspresso.getPrice())
        .build();

    IngredientStock ingredientStockSugar = ingredientsStock.get(IngredientType.SUGAR);
    Ingredient ingredientSugar = Ingredient.builder()
        .ingredientType(ingredientStockSugar.getIngredientType())
        .quantity(2)
        .price(ingredientStockSugar.getPrice())
        .build();

    coffeeRecipe.addIngredient(ingredientEspresso);
    coffeeRecipe.addIngredient(ingredientSugar);

    Mockito.when(ingredientStockDatabase.getIngredientsStock())
        .thenReturn(ingredientsStock);

    NotValidIngredientException exception = assertThrows(
        NotValidIngredientException.class,
        () -> ingredientValidator.validate(DrinkType.ESPRESSO, validIngredients, coffeeRecipe)
    );

    assertThat(exception.getMessage()).isEqualTo(String.format(ingredientLowQuantityMessage, 2));
  }

  @Test
  @DisplayName("Should throw NotValidIngredientException when ingredient not valid")
  public void shouldThrowNotValidIngredientExceptionWhenIngredientNotValid() {
    Map<IngredientType, IngredientStock> ingredientsStock = getIngredientTypeIngredientStock();

    Set<IngredientType> validIngredients = Set.of(
        IngredientType.COFFEE,
        IngredientType.SUGAR
    );

    Recipe coffeeRecipe = new CoffeeRecipe();

    IngredientStock ingredientStockEspresso = ingredientsStock.get(IngredientType.COFFEE);
    Ingredient ingredientEspresso = Ingredient.builder()
        .ingredientType(ingredientStockEspresso.getIngredientType())
        .quantity(1)
        .price(ingredientStockEspresso.getPrice())
        .build();

    IngredientStock ingredientStockCream = ingredientsStock.get(IngredientType.CREAM);
    Ingredient ingredientCream = Ingredient.builder()
        .ingredientType(ingredientStockCream.getIngredientType())
        .quantity(1)
        .price(ingredientStockCream.getPrice())
        .build();

    coffeeRecipe.addIngredient(ingredientEspresso);
    coffeeRecipe.addIngredient(ingredientCream);

    Mockito.when(ingredientStockDatabase.getIngredientsStock())
        .thenReturn(ingredientsStock);

    NotValidIngredientException exception = assertThrows(
        NotValidIngredientException.class,
        () -> ingredientValidator.validate(DrinkType.ESPRESSO, validIngredients, coffeeRecipe)
    );

    assertThat(exception.getMessage()).isEqualTo(String.format(ingredientNotValidMessage, IngredientType.CREAM.getName(), DrinkType.ESPRESSO));
  }

  private Map<IngredientType, IngredientStock> getIngredientTypeIngredientStock() {
    Map<IngredientType, IngredientStock> ingredientsStock = new HashMap<>();
    ingredientsStock.put(IngredientType.COFFEE, new IngredientStock(IngredientType.COFFEE, 10, 0.75));
    ingredientsStock.put(IngredientType.DECAF_COFFEE, new IngredientStock(IngredientType.DECAF_COFFEE, 10, 0.75));
    ingredientsStock.put(IngredientType.SUGAR, new IngredientStock(IngredientType.SUGAR, 10, 0.25));
    ingredientsStock.put(IngredientType.CREAM, new IngredientStock(IngredientType.CREAM, 5, 0.15));
    return ingredientsStock;
  }
}