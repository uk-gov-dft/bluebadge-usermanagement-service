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

  Scenario: Verify retrieved user is the current authenticated user
    Given path 'users/me'
    When method GET
    Then status 200
    And match $.data contains {uuid:"cc4fbb98-3963-41ef-9c75-a9651b2fe27c", name:"Bruce Wayne", emailAddress:"abc@dft.gov.uk", roleId:2, roleName:"#notnull"}

  Scenario: Verify if use client credential access token then 400
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    Given path 'users/me'
    When method GET
    Then status 400

