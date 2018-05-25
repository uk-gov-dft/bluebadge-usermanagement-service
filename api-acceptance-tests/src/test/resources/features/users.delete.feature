@users-delete
Feature: Verify users Delete

  Background:
    * url baseUrl

  Scenario: Verify delete not exists
    Given path 'authorities/2/users/-100000'
    When method DELETE
    Then status 200
    And match $.data.deleted == 0

  Scenario: Verify delete OK.
    Given path 'authorities/2/users/-5'
    When method DELETE
    Then status 200
    And match $.data.deleted == 1