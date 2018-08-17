@users-get
Feature: Verify current users retrieval, using the access token

  Background:
    * url baseUrl
    * def result = callonce read('./oauth2-user.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify retrieved user is the current authenticated user
    Given path 'users/me'
    When method GET
    Then status 200
    And match $.data contains {uuid:"cc4fbb98-3963-41ef-9c75-a9651b2fe27c", name:"Bruce Wayne", emailAddress:"abc@DFT.gov.uk", roleId:2, roleName:"#notnull"}

  Scenario: Verify if use client credential access token then 400
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    Given path 'users/me'
    When method GET
    Then status 400

