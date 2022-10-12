package com.example.spring.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.spring.IngredientStockDatabase;
import com.example.spring.dto.DrinkRequest;
import com.example.spring.dto.DrinkResponse;
import com.example.spring.dto.IngredientRequest;
import com.example.spring.dto.RefillRequest;
import com.example.spring.enums.DrinkType;
import com.example.spring.enums.IngredientType;
import com.example.spring.exception.NotValidIngredientException;
import com.example.spring.mixer.AmericanoStrategy;
import com.example.spring.mixer.CoffeeStrategy;
import com.example.spring.mixer.EspressoStrategy;
import com.example.spring.mixer.Mixer;
import com.example.spring.model.IngredientStock;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class CoffeeMachineControllerTest {

  private static final String drinkNotExistMessage = "This drink does not exist";
  private static final String ingredientNotExistMessage = "This ingredient does not exist";

  @Mock
  private Mixer mixer;

  @Mock
  private IngredientStockDatabase ingredientStockDatabase;

  private ObjectMapper mapper = new ObjectMapper();
  private MockMvc mvc;
  private Map<String, CoffeeStrategy> coffeeStrategies = new HashMap<>();
  private CoffeeMachineController coffeeMachineController;

  @BeforeEach
  public void setup() {
    coffeeMachineController = new CoffeeMachineController(mixer, coffeeStrategies, ingredientStockDatabase);

    ReflectionTestUtils.setField(coffeeMachineController, "drinkNotExistMessage", drinkNotExistMessage);
    ReflectionTestUtils.setField(coffeeMachineController, "ingredientNotExistMessage", ingredientNotExistMessage);

    JacksonTester.initFields(this, mapper);
    mvc = MockMvcBuilders.standaloneSetup(coffeeMachineController).build();

    when(ingredientStockDatabase.getIngredientsStock())
        .thenReturn(getIngredientTypeIngredientStock());
  }

  @Test
  public void shouldShowErrorWhenDrinkTypeIsNoValid() throws Exception {
    var ingredientEspressoRequest = new IngredientRequest();
    ingredientEspressoRequest.setIngredientType(IngredientType.ESPRESSO);
    ingredientEspressoRequest.setQuantity(1);

    var ingredientSugarRequest = new IngredientRequest();
    ingredientSugarRequest.setIngredientType(IngredientType.SUGAR);
    ingredientSugarRequest.setQuantity(2);

    var drinkRequest = new DrinkRequest();
    drinkRequest.setDrinkType("");
    drinkRequest.setIngredients(List.of(ingredientEspressoRequest, ingredientSugarRequest));

    var response = mvc.perform(post("/api/v1/make-coffee")
            .content(mapper.writeValueAsString(drinkRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andReturn()
        .getResponse();

    assertThat(response.getContentAsString()).isEqualTo(drinkNotExistMessage);
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  public void shouldShowErrorWhenDrinkStrategyIsNoValid() throws Exception {
    var ingredientEspressoRequest = new IngredientRequest();
    ingredientEspressoRequest.setIngredientType(IngredientType.ESPRESSO);
    ingredientEspressoRequest.setQuantity(1);

    var ingredientSugarRequest = new IngredientRequest();
    ingredientSugarRequest.setIngredientType(IngredientType.SUGAR);
    ingredientSugarRequest.setQuantity(2);

    var drinkRequest = new DrinkRequest();
    drinkRequest.setDrinkType(DrinkType.ESPRESSO.name());
    drinkRequest.setIngredients(List.of(ingredientEspressoRequest, ingredientSugarRequest));

    var response = mvc.perform(post("/api/v1/make-coffee")
            .content(mapper.writeValueAsString(drinkRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andReturn()
        .getResponse();

    assertThat(response.getContentAsString()).isEqualTo(drinkNotExistMessage);
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  public void shouldShowErrorWhenIngredientIsNoValid() throws Exception {
    var ingredientEspressoRequest = new IngredientRequest();
    ingredientEspressoRequest.setIngredientType(IngredientType.STREAMED_MILK);
    ingredientEspressoRequest.setQuantity(1);

    var ingredientSugarRequest = new IngredientRequest();
    ingredientSugarRequest.setIngredientType(IngredientType.SUGAR);
    ingredientSugarRequest.setQuantity(2);

    var drinkRequest = new DrinkRequest();
    drinkRequest.setDrinkType(DrinkType.ESPRESSO.name());
    drinkRequest.setIngredients(List.of(ingredientEspressoRequest, ingredientSugarRequest));

    coffeeStrategies.put(DrinkType.ESPRESSO.name(), mock(EspressoStrategy.class));

    var response = mvc.perform(post("/api/v1/make-coffee")
            .content(mapper.writeValueAsString(drinkRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andReturn()
        .getResponse();

    assertThat(response.getContentAsString()).isEqualTo(ingredientNotExistMessage);
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  public void shouldMakeCoffeeAmericanoAndReturnResponse() throws Exception {
    var ingredientEspressoRequest = new IngredientRequest();
    ingredientEspressoRequest.setIngredientType(IngredientType.COFFEE);
    ingredientEspressoRequest.setQuantity(1);

    var ingredientSugarRequest = new IngredientRequest();
    ingredientSugarRequest.setIngredientType(IngredientType.SUGAR);
    ingredientSugarRequest.setQuantity(2);

    var drinkRequest = new DrinkRequest();
    drinkRequest.setDrinkType(DrinkType.AMERICANO.name());
    drinkRequest.setIngredients(List.of(ingredientEspressoRequest, ingredientSugarRequest));

    coffeeStrategies.put(DrinkType.AMERICANO.name(), mock(AmericanoStrategy.class));

    var drinkResponse = new DrinkResponse(DrinkType.AMERICANO, null, 0);
    when(mixer.make()).thenReturn(drinkResponse);

    mvc.perform(post("/api/v1/make-coffee")
            .content(mapper.writeValueAsString(drinkRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.drinkType").value(DrinkType.AMERICANO.name()));
  }

  @Test
  public void shouldMakeCoffeeEspressoAndReturnResponse() throws Exception {
    var ingredientEspressoRequest = new IngredientRequest();
    ingredientEspressoRequest.setIngredientType(IngredientType.ESPRESSO);
    ingredientEspressoRequest.setQuantity(1);

    var drinkRequest = new DrinkRequest();
    drinkRequest.setDrinkType(DrinkType.ESPRESSO.name());
    drinkRequest.setIngredients(List.of(ingredientEspressoRequest));

    coffeeStrategies.put(DrinkType.ESPRESSO.name(), mock(EspressoStrategy.class));

    var drinkResponse = new DrinkResponse(DrinkType.ESPRESSO, null, 0);
    when(mixer.make()).thenReturn(drinkResponse);

    mvc.perform(post("/api/v1/make-coffee")
            .content(mapper.writeValueAsString(drinkRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.drinkType").value(DrinkType.ESPRESSO.name()));
  }

  @Test
  public void shouldShowValidationErrorWhenNotValidIngredientExceptionIsThrown() throws Exception {
    var ingredientEspressoRequest = new IngredientRequest();
    ingredientEspressoRequest.setIngredientType(IngredientType.COFFEE);
    ingredientEspressoRequest.setQuantity(1);

    var drinkRequest = new DrinkRequest();
    drinkRequest.setDrinkType(DrinkType.AMERICANO.name());
    drinkRequest.setIngredients(List.of(ingredientEspressoRequest));

    coffeeStrategies.put(DrinkType.AMERICANO.name(), mock(AmericanoStrategy.class));

    when(mixer.make()).thenThrow(new NotValidIngredientException("NotValidIngredientException"));

    mvc.perform(post("/api/v1/make-coffee")
            .content(mapper.writeValueAsString(drinkRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(jsonPath("$").value("NotValidIngredientException"));
  }

  @Test
  public void shouldRefillSugar() throws Exception {
    var refillRequest = new RefillRequest();
    refillRequest.setQuantity(5);
    refillRequest.setIngredientType(IngredientType.SUGAR);

    var response = mvc.perform(post("/api/v1/refill")
            .content(mapper.writeValueAsString(refillRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andReturn()
        .getResponse();

    assertThat(ingredientStockDatabase.getIngredientsStock().get(IngredientType.SUGAR).getQuantity()).isEqualTo(5);
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
  }

  @Test
  public void shouldShowErrorWhenIngredientTypeNotFound() throws Exception {
    var refillRequest = new RefillRequest();
    refillRequest.setQuantity(5);
    refillRequest.setIngredientType(IngredientType.WHIPPED_CREAM);

    var response = mvc.perform(post("/api/v1/refill")
            .content(mapper.writeValueAsString(refillRequest))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andReturn()
        .getResponse();

    assertThat(response.getContentAsString()).isEqualTo(ingredientNotExistMessage);
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  private Map<IngredientType, IngredientStock> getIngredientTypeIngredientStock() {
    Map<IngredientType, IngredientStock> ingredientsStock = new HashMap<>();
    ingredientsStock.put(IngredientType.COFFEE, new IngredientStock(IngredientType.COFFEE, 10, 0.75));
    ingredientsStock.put(IngredientType.DECAF_COFFEE, new IngredientStock(IngredientType.DECAF_COFFEE, 10, 0.75));
    ingredientsStock.put(IngredientType.SUGAR, new IngredientStock(IngredientType.SUGAR, 10, 0.25));
    ingredientsStock.put(IngredientType.CREAM, new IngredientStock(IngredientType.CREAM, 5, 0.15));
    ingredientsStock.put(IngredientType.ESPRESSO, new IngredientStock(IngredientType.ESPRESSO, 5, 0.55));
    return ingredientsStock;
  }
}