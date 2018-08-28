@users-delete
Feature: Verify users Delete

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify delete not exists
    Given path 'users/5ec6e1f7-fa22-47e4-9034-8f4a7eb773a5'
    When method DELETE
    Then status 404

  Scenario: Verify delete OK.
    Given path 'users/e9ec670a-1c2d-449a-be92-4493cbf4838e'
    When method DELETE
    Then status 200

  Scenario: Verify delete user in different local authority than current user's
    Given path 'users/9bdc58aa-7026-4a7c-9d57-805c3d96cecb'
    When method DELETE
    Then status 404
