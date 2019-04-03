@users-delete
Feature: Verify users Delete

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2-user.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify delete not exists
    Given path 'users/5ec6e1f7-fa22-47e4-9034-8f4a7eb773a5'
    When method DELETE
    Then status 404

  Scenario: Verify delete success.
    Given path 'users/34c40459-5b73-402c-96e1-94235b178771'
    When method DELETE
    Then status 200

  Scenario: Verify delete user in different local authority than current user's
    Given path 'users/dcf8f6f5-f424-4caf-a415-4476bc264909'
    When method DELETE
    Then status 403
