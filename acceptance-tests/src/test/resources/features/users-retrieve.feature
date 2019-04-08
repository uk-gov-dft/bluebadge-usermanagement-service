@users-retrieve
Feature: Verify users retrieval

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2-user.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader

  Scenario: Verify retrieve one user by user id
    Given path 'users/cf541b4c-ec03-4820-9bff-f3b0017f06c3'
    When method GET
    Then status 200
    And match $.data contains {uuid:"cf541b4c-ec03-4820-9bff-f3b0017f06c3", name:"get test", emailAddress:"um_gettest@dft.gov.uk", roleId:2, roleName:"#notnull"}

  Scenario: Verify retrieval of non existing user
    Given path 'users/bc6b7d6b-6cf2-454c-832c-763a93bf46ad'
    When method GET
    Then status 404

  Scenario: Verify retrieve one user by user id when local authority is different from current users's
    Given path 'users/dcf8f6f5-f424-4caf-a415-4476bc264909'
    When method GET
    Then status 403

  Scenario: Verify retrieve of a dft admin user
    Given path 'users/78800473-857b-4a08-b01b-d72957517969'
    When method GET
    Then status 403
