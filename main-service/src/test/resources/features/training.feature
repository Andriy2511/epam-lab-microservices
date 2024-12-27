Feature: Training functionality
  As a trainer I want to interact with training functionality

  Scenario: Add training for trainer and trainee
    When a user registers as a trainee with first name "Martin" and last name "Luther"
    Then the response status should be 200
    When a user registers as a trainer with first name "Anthony" and last name "Joshua"
    Then the response status should be 200
    When a user login with registered credentials
    Then the response status should be 200
    And the response should contain a valid token
    When a trainer with username "Anthony.Joshua" want to add training with duration 60 for trainee with username "Martin.Luther"
    Then the response status should be 200
    And I wait for 1 second
    When a trainer requests his workload with current year and month
    Then the response status should be 200
    And the totalDuration should be 60

  Scenario: Get training time when added two trainings
    When a user registers as a trainee with first name "Abraham" and last name "Lincoln"
    And a user registers as a trainee with first name "Charles" and last name "Martel"
    Then the response status should be 200
    When a user registers as a trainer with first name "Jefferson" and last name "Davis"
    Then the response status should be 200
    When a user login with registered credentials
    Then the response status should be 200
    And the response should contain a valid token
    When a trainer with username "Jefferson.Davis" want to add training with duration 60 for trainee with username "Abraham.Lincoln"
    And a trainer with username "Jefferson.Davis" want to add training with duration 60 for trainee with username "Charles.Martel"
    Then the response status should be 200
    And I wait for 1 second
    When a trainer requests his workload with current year and month
    Then the totalDuration should be 120
    And the response status should be 200