package com.example.spring.exception;

public class NotValidIngredientException extends Exception {

  public NotValidIngredientException(String message) {
    super(message);
  }
}
