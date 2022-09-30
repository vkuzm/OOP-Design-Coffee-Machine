package com.example.spring.enums;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum DrinkType {
  CAPPUCCINO, ESPRESSO, AMERICANO;

  public static boolean isValid(String drink) {
    return Arrays.stream(DrinkType.values())
        .map(DrinkType::name)
        .collect(Collectors.toSet())
        .contains(drink);
  }
}