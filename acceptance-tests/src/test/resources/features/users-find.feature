@users-find
Feature: Verify find users

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2-user.feature')
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
