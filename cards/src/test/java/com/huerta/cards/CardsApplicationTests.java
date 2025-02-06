package com.huerta.cards;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class CardsApplicationTests {

  @Test
  public void contextLoads() {
    try (ConfigurableApplicationContext context = SpringApplication.run(CardsApplication.class)) {
      assertNotNull(context);
    }
  }

  @Test
  public void main() {
    CardsApplication.main(new String[] {});
  }
}
