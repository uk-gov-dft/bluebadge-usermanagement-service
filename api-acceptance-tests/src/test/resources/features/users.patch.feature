@users-patch
Feature: Verify users update

  Background:
    * url baseUrl

  Scenario: Update password
    Given path 'authorities/2/users/-1/password'
    And request { ***REMOVED***}
    When method PATCH
    Then status 200

  Scenario: Update password missing required field
    Given path 'authorities/2/users/-1/password'
    And request { ***REMOVED***}
    When method PATCH
    Then status 400
    And match $.error.errors contains {field:"uuid", reason:"#notnull", message:"NotNull. ***REMOVED***}


  Scenario: Inactive UUID
    Given path 'authorities/2/users/-1/password'
    And request { ***REMOVED***}
    When method PATCH
    Then status 400

  Scenario: Invalid UUID
    Given path 'authorities/2/users/-1/password'
    And request { ***REMOVED***}
    When method PATCH
    Then status 400

  Scenario: Invalid password
    Given path 'authorities/2/users/-1/password'
    And request { ***REMOVED***}
    When method PATCH
    Then status 400

  Scenario: Invalid password confirm
    Given path 'authorities/2/users/-1/password'
    And request { ***REMOVED***}
    When method PATCH
    Then status 400

  Scenario: Password does not match
    Given path 'authorities/2/users/-1/password'
    And request { ***REMOVED***}
    When method PATCH
    Then status 400