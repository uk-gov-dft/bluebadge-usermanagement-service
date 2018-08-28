@users-retrieve
Feature: Verify users retrieval

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
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
