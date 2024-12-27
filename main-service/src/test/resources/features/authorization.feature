Feature: User Authorization
  As a user I want to register as a trainer or trainee and log in to the system

  Scenario: Successful trainer registration
    When a user registers as a trainer with first name "John" and last name "Doe"
    Then the response status should be 200

  Scenario: Successful trainee registration
    When a user registers as a trainee with first name "Jane" and last name "Smith"
    Then the response status should be 200

  Scenario: Unsuccessful user authorization
    When a user login with username "Unexisting.User" and password "1234567890"
    Then the response status should be 401

  Scenario: Successful user authorization
    When a user registers as a trainer with first name "Jason" and last name "Ivanov"
    Then the response status should be 200
    When a user login with registered credentials
    Then the response status should be 200
    And the response should contain a valid token

