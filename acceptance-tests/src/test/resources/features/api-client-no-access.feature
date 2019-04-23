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
    * header Accept = jsonVersionHeader

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
    Given path 'users/34c40459-5b73-402c-96e1-94235b178771'
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
    Given path 'users/34c40459-5b73-402c-96e1-94235b178771'
    When method DELETE
    Then status 403

  Scenario: Update invalid user denied
    Given path 'users/blah'
    And request {uuid: "608ad765-9404-42c2-b56b-7bf1544220d1", name:"blah", emailAddress:"um_blah@blah.com", localAuthorityShortCode: "ABERD", roleId: 1 }
    When method PUT
    Then status 403
  Scenario: Update unknown user denied
    Given path 'users/bc6b7d6b-6cf2-454c-832c-763a93bf46ad'
    And request {uuid: "608ad765-9404-42c2-b56b-7bf1544220d1", name:"blah", emailAddress:"um_blah@blah.com", localAuthorityShortCode: "ABERD", roleId: 1 }
    When method PUT
    Then status 403
  Scenario: Update user denied
    Given path 'users/e64a4715-6d52-47fa-a563-2ec134478317'
    And request {uuid: "e64a4715-6d52-47fa-a563-2ec134478317", name:"blah", emailAddress:"um_blah@blah.com", localAuthorityShortCode: "ABERD", roleId: 1 }
    When method PUT
    Then status 403

  Scenario: Verify all other end points are blocked
    Given path 'something'
    When method GET
    Then status 403
