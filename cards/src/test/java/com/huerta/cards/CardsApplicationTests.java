package com.huerta.cards;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CardsApplicationTests {

  @Test
  public void contextLoads() {
    try (ConfigurableApplicationContext context = SpringApplication.run(CardsApplication.class, "--server.port=0")) {
      assertNotNull(context);
    }
  }

  @Test
  public void main() {
    CardsApplication.main(new String[] { "--server.port=0" });
  }
}
