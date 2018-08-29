@users-find
Feature: Verify users retrieval

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify retrieve all users for an authority
    Given path 'users'
    When method GET
    Then status 200
    And match each $.data contains {uuid:"#notnull", name:"#notnull", emailAddress:"#notnull", localAuthorityShortCode:"ABERD"}

  Scenario: Verify retrieve all users for an authority with name filter match
    Given path 'users'
    And param name = 'Sam'
    When method GET
    Then status 200
    And match each $.data contains {uuid:"#notnull", name:"#notnull", emailAddress:"#notnull"}

  Scenario: Verify retrieve all users for an authority with name filter match where match case insensitive
    Given path 'users'
    And param name = 'sAmPaTh'
    When method GET
    Then status 200
    And match each $.data contains {uuid:"#notnull", name:"#notnull", emailAddress:"#notnull"}

  Scenario: Verify retrieve all users for an authority with emailAddress filter match
    Given path 'users'
    And param name = 'def@'
    When method GET
    Then status 200
    And match each $.data contains {uuid:"#notnull", name:"#notnull", emailAddress:"#notnull"}

  Scenario: Verify retrieve all users for an authority with filter not matching
    Given path 'users'
    And param name = 'zzzzzzzzzz'
    When method GET
    Then status 200

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
