@users-get
Feature: Verify users retrieval

  Background:
    * url baseUrl

  Scenario: Verify retrieve all users for an authority
    Given path 'users'
    And param authorityId = 2
    When method GET
    Then status 200
    And match each $.data contains {id:"#notnull", name:"#notnull", emailAddress:"#notnull"}

  Scenario: Verify retrieve all users for an authority with name filter match
    Given path 'users'
    And param name = 'Sam'
    And param authorityId = 2
    When method GET
    Then status 200
    And match each $.data contains {id:"#notnull", name:"#notnull", emailAddress:"#notnull"}

  Scenario: Verify retrieve all users for an authority with name filter match where match case insensitive
    Given path 'users'
    And param name = 'sAmPaTh'
    And param authorityId = 2
    When method GET
    Then status 200
    And match each $.data contains {id:"#notnull", name:"#notnull", emailAddress:"#notnull"}

  Scenario: Verify retrieve all users for an authority with emailAddress filter match
    Given path 'users'
    And param name = 'def@'
    And param authorityId = 2
    When method GET
    Then status 200
    And match each $.data contains {id:"#notnull", name:"#notnull", emailAddress:"#notnull"}

  Scenario: Verify retrieve all users for an authority with filter not matching
    Given path 'users'
    And param name = 'zzzzzzzzzz'
    And param authorityId = 2
    When method GET
    Then status 200
    #And match $.data.totalItems == 0

  Scenario: Verify ok request password email.
    Given path 'users/-1/passwordReset'
    When method GET
    Then status 200

  Scenario: Verify user not exists password reset.
    Given path 'users/-9999/passwordReset'
    When method GET
    Then status 404
