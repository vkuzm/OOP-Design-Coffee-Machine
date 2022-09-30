package com.example.spring.mixer;

import com.example.spring.dto.DrinkResponse;
import com.example.spring.exception.NotValidIngredientException;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Mixer {

  private CoffeeStrategy coffeeStrategy;

  public DrinkResponse make() throws NotValidIngredientException {
    return coffeeStrategy.process();
  }
}
