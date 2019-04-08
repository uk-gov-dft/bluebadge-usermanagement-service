@users-request***REMOVED***-reset
Feature: Verify create password reset

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2-user.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader

  #passing request {uuid: "3bfe600b-4425-40cd-ad81-d75bbe16ee13"} only to satisfy karate framework
  Scenario: Verify ok request password email
    Given path 'users/3bfe600b-4425-40cd-ad81-d75bbe16ee13/passwordReset'
    And request {uuid: "3bfe600b-4425-40cd-ad81-d75bbe16ee13"}
    When method POST
    Then status 200
    
  #passing request {uuid: "cc4fbb98-3963-41ef-9c75-a9651b2f000"} only to satisfy karate framework
  Scenario: Verify user not exists password reset
    Given path 'users/cc4fbb98-3963-41ef-9c75-a9651b2f0000/passwordReset'
    And request {uuid: "cc4fbb98-3963-41ef-9c75-a9651b2f000"}
    When method POST
    Then status 404

  #passing request {uuid: "dcf8f6f5-f424-4caf-a415-4476bc264909"} only to satisfy karate framework
  Scenario: Verify user exists but in another local authority different from current user's, password reset
    Given path 'users/dcf8f6f5-f424-4caf-a415-4476bc264909/passwordReset'
    And request {uuid: "dcf8f6f5-f424-4caf-a415-4476bc264909"}
    When method POST
    Then status 403
