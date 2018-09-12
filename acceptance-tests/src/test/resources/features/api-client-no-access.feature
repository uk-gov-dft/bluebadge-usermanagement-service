@api-client
Feature: Verify API users do not have access to any user management services

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Create User denied
    Given path 'users'
    And request {name: " "}
    When method POST
    Then status 403

  Scenario: Retrieve current user is a bad request as api client does not have an email address
    Given path 'users/me'
    When method GET
    Then status 400

  Scenario: Retrieve invalid user denied
    Given path 'users/blah'
    When method GET
    Then status 403
  Scenario: Retrieve unknown user denied
    Given path 'users/bc6b7d6b-6cf2-454c-832c-763a93bf46ad'
    When method GET
    Then status 403
  Scenario: Retrieve user denied
    Given path 'users/e9ec670a-1c2d-449a-be92-4493cbf4838e'
    When method GET
    Then status 403

  Scenario: Delete invalid user denied
    Given path 'users/blah'
    When method DELETE
    Then status 403
  Scenario: Delete unknown user denied
    Given path 'users/bc6b7d6b-6cf2-454c-832c-763a93bf46ad'
    When method DELETE
    Then status 403
  Scenario: Delete user denied
    Given path 'users/e9ec670a-1c2d-449a-be92-4493cbf4838e'
    When method DELETE
    Then status 403