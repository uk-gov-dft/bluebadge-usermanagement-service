@users-get
Feature: Verify users retrieval

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify retrieve one user by user id
    Given path 'users/c16fd9da-f823-4d37-a289-6b0d411c111c'
    When method GET
    Then status 200
    And match $.data contains {uuid:"c16fd9da-f823-4d37-a289-6b0d411c111c", name:"get test", emailAddress:"gettest@dft.gov.uk", roleId:2, roleName:"#notnull"}

  Scenario: Verify retrieve one user by user id when local authority is different from current users's
    Given path 'users/9bdc58aa-7026-4a7c-9d57-805c3d96cecb'
    When method GET
    Then status 404

  Scenario: Verify retrieve all users for an authority
    Given path 'users'
    And param authorityShortCode = 'ABERD'
    When method GET
    Then status 200
    And match each $.data contains {uuid:"#notnull", name:"#notnull", emailAddress:"#notnull"}

  Scenario: Verify retrieve all users for an authority with name filter match
    Given path 'users'
    And param name = 'Sam'
    And param authorityShortCode = 'ABERD'
    When method GET
    Then status 200
    And match each $.data contains {uuid:"#notnull", name:"#notnull", emailAddress:"#notnull"}

  Scenario: Verify retrieve all users for an authority with name filter match where match case insensitive
    Given path 'users'
    And param name = 'sAmPaTh'
    And param authorityShortCode = 'ABERD'
    When method GET
    Then status 200
    And match each $.data contains {uuid:"#notnull", name:"#notnull", emailAddress:"#notnull"}

  Scenario: Verify retrieve all users for an authority with emailAddress filter match
    Given path 'users'
    And param name = 'def@'
    And param authorityShortCode = 'ABERD'
    When method GET
    Then status 200
    And match each $.data contains {uuid:"#notnull", name:"#notnull", emailAddress:"#notnull"}

  Scenario: Verify retrieve all users for an authority with filter not matching
    Given path 'users'
    And param name = 'zzzzzzzzzz'
    And param authorityShortCode = 'ABERD'
    When method GET
    Then status 200
    #And match $.data.totalItems == 0

  Scenario: Verify ok request password email.
    Given path 'users/cc4fbb98-3963-41ef-9c75-a9651b2fe27c/passwordReset'
    When method GET
    Then status 200

  Scenario: Verify user not exists password reset.
    Given path 'users/cc4fbb98-3963-41ef-9c75-a9651b2f0000/passwordReset'
    When method GET
    Then status 404

  Scenario: Verify user exists but in another local authority different from current user's, password reset.
    Given path 'users/9bdc58aa-7026-4a7c-9d57-805c3d96cecb/passwordReset'
    When method GET
    Then status 404
