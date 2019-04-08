@users-get
Feature: Verify current users retrieval, using the access token

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2-user.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader

  Scenario: Verify retrieved user is the current authenticated user
    Given path 'users/me'
    When method GET
    Then status 200
    And match $.data contains {uuid:"3bfe600b-4425-40cd-ad81-d75bbe16ee13", name:"Bruce Wayne", emailAddress:"um_abc@dft.gov.uk", roleId:2, roleName:"#notnull"}

  Scenario: Verify if use client credential access token then 400
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    Given path 'users/me'
    When method GET
    Then status 400

