package com.huerta.accounts;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AccountsApplicationTests {

  @Test
  public void contextLoads() {
    assertDoesNotThrow(() -> AccountsApplication.main(new String[] {}));
  }
}
