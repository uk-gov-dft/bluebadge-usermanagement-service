@users-delete
Feature: Verify users Delete

  Background:
    * url baseUrl

  Scenario: Verify delete not exists
    Given path 'users/-100000'
    When method DELETE
    Then status 404

  Scenario: Verify delete OK.
    Given path 'users/-5'
    When method DELETE
    Then status 200