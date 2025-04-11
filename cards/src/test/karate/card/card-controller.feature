Feature: Card Controller API

Scenario: Get all cards
  Given url 'http://localhost:8082/cards'
  When method get
  Then status 200
  And match response == []

Scenario: Create a new card
  Given url 'http://localhost:8082/cards'
  And request { "cardNumber": "1234-5678-9876-5432", "cardHolderName": "John Doe" }
  When method post
  Then status 201
  And match response.cardNumber == '1234-5678-9876-5432'
  And match response.cardHolderName == 'John Doe'