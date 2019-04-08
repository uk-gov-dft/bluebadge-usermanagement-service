@users-update***REMOVED***
Feature: Verify users update password

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2-lawebapp.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader

  Scenario: Update password
    Given path 'user/password/4175e31c-9c0c-41c0-9afb-40dc0a89b9c5'
    And request { ***REMOVED***}
    When method PATCH
    Then status 200
    * def emailLink = db.readRow('SELECT * FROM usermanagement.email_link where uuid = \'4175e31c-9c0c-41c0-9afb-40dc0a89b9c5\'')
    * match emailLink contains { user_id: -1, is_active: false }

  Scenario: Inactive email link UUID
    Given path 'user/password/4175e31c-0000-41c0-9afb-40dc0a89b9c5'
    And request { ***REMOVED***}
    When method PATCH
    Then status 400
    And match $.error.errors contains {field:" ***REMOVED***}

  Scenario: Invalid email link UUID
    Given path  'user/password/4175e31c-1111-41c0-9afb-40dc0a89b9c5'
    And request { ***REMOVED***}
    When method PATCH
    Then status 400
    And match $.error.errors contains {field:" ***REMOVED***}

  Scenario: Expired email link UUID
    Given path  'user/password/b81aa7df-5d2e-48d9-9740-08b661884c2f'
    And request { ***REMOVED***}
    When method PATCH
    Then status 400
    And match $.error.errors contains {field:" ***REMOVED***}

  Scenario: Invalid password
    Given path 'user/password/4175e31c-9c0c-41c0-9afb-40dc0a89b9c5'
    And request { ***REMOVED***}
    When method PATCH
    Then status 400

  Scenario: Invalid password confirm
    Given path 'user/password/4175e31c-9c0c-41c0-9afb-40dc0a89b9c5'
    And request { ***REMOVED***}
    When method PATCH
    Then status 400

  Scenario: Password does not match
    Given path 'user/password/4175e31c-9c0c-41c0-9afb-40dc0a89b9c5'
    And request { ***REMOVED***}
    When method PATCH
    Then status 400
    
  Scenario: Password blacklisted
    Given path 'user/password/4175e31c-9c0c-41c0-9afb-40dc0a89b9c5'
    And request { ***REMOVED***}
    When method PATCH
    Then status 400    