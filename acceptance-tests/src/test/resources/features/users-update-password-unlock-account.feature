@users-update***REMOVED***-unlock-account
Feature: Verify users update password unlocks a locked account

  Background:
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def lawebapp_auth = callonce read('./oauth2-lawebapp.feature')

  Scenario: locked user account is unlocked after password reset
    * url authServerUrl
    * path 'oauth/token'
    * header Authorization = call read('classpath:basic-auth.js') { username: 'bb_la_web_app_id',  ***REMOVED*** }
    * form field grant_type = 'password'
    * form field clientId = 'bb_la_web_app_id'
    * form field username = 'um_locked@dft.gov.uk'
    * form field  ***REMOVED***
    * method post
    * status 400
    And match $.error_description == 'User account is locked'
    * url baseUrl
    * header Authorization = 'Bearer ' + lawebapp_auth.accessToken
    Given path 'user/password/65c5ba53-876d-4b28-831d-70d8dec875fa'
    And request { ***REMOVED***}
    When method PATCH
    Then status 200
    * url authServerUrl
    Given path 'oauth/token'
    * header Authorization = call read('classpath:basic-auth.js') { username: 'bb_la_web_app_id',  ***REMOVED*** }
    * form field grant_type = 'password'
    * form field clientId = 'bb_la_web_app_id'
    * form field username = 'um_locked@dft.gov.uk'
    * form field  ***REMOVED***
    When method post
    Then status 200

  Scenario: Inactive user account is unlocked after password reset
    * url authServerUrl
    * path 'oauth/token'
    * header Authorization = call read('classpath:basic-auth.js') { username: 'bb_la_web_app_id',  ***REMOVED*** }
    * form field grant_type = 'password'
    * form field clientId = 'bb_la_web_app_id'
    * form field username = 'um_inactive@dft.gov.uk'
    * form field  ***REMOVED***
    * method post
    * status 400
    And match $.error_description == 'User account is locked'
    * url baseUrl
    * header Authorization = 'Bearer ' + lawebapp_auth.accessToken
    Given path 'user/password/3c4f4b6c-d3c8-4627-817f-90df4fd31ff7'
    And request { ***REMOVED***}
    When method PATCH
    Then status 200
    * url authServerUrl
    Given path 'oauth/token'
    * header Authorization = call read('classpath:basic-auth.js') { username: 'bb_la_web_app_id',  ***REMOVED*** }
    * form field grant_type = 'password'
    * form field clientId = 'bb_la_web_app_id'
    * form field username = 'um_inactive@dft.gov.uk'
    * form field  ***REMOVED***
    When method post
    Then status 200
