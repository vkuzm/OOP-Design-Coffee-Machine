package com.example.spring.mixer;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.spring.dto.DrinkResponse;
import com.example.spring.enums.DrinkType;
import com.example.spring.enums.IngredientType;
import com.example.spring.exception.NotValidIngredientException;
import com.example.spring.model.Ingredient;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MixerTest {

  @Mock
  private CoffeeStrategy coffeeStrategy;

  @InjectMocks
  private Mixer mixer;

  @Test
  @DisplayName("Should make drink")
  public void shouldMakeDrink() throws NotValidIngredientException {
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

    List<Ingredient> ingredients = List.of(ingredientCoffee, ingredientSugar);

    Mockito.when(coffeeStrategy.process()).thenReturn(new DrinkResponse(DrinkType.ESPRESSO, ingredients, 10));

    assertThat(mixer.make().getDrinkType()).isEqualTo(DrinkType.ESPRESSO);
  }
}