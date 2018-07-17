@users-patch
Feature: Verify users update

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Update password
    Given path 'user/password/4175e31c-9c0c-41c0-9afb-40dc0a89b9c5'
    And request { ***REMOVED***}
    When method PATCH
    Then status 200

  Scenario: Inactive UUID
    Given path 'user/password/4175e31c-0000-41c0-9afb-40dc0a89b9c5'
    And request { ***REMOVED***}
    When method PATCH
    Then status 400

  Scenario: Invalid UUID
    Given path  'user/password/4175e31c-1111-41c0-9afb-40dc0a89b9c5'
    And request { ***REMOVED***}
    When method PATCH
    Then status 400

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