@users-delete
Feature: Verify users Delete

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify delete not exists
    Given path 'users/-100000'
    When method DELETE
    Then status 404

  Scenario: Verify delete OK.
    Given path 'users/-5'
    When method DELETE
    Then status 200