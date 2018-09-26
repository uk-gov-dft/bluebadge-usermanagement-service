@users-retrieve
Feature: Verify users retrieval for a DFT Admin user

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2-dft-user.feature')
    * header Authorization = 'Bearer ' + result.accessToken

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
    Given path 'users/dca49e62-c879-49df-bec8-889ce34ae9ad'
    When method GET
    Then status 200
    And match $.data contains {uuid:"dca49e62-c879-49df-bec8-889ce34ae9ad", name:"Jack Napier", emailAddress:"um_angl_admin@dft.gov.uk", roleId:2, roleName:"#notnull"}

  Scenario: Verify retrieve of a dft admin user
    Given path 'users/78800473-857b-4a08-b01b-d72957517969'
    When method GET
    Then status 200
    And match $.data contains {uuid:"78800473-857b-4a08-b01b-d72957517969", name:"Dr. Pamela Lillian Isley", emailAddress:"um_dft_test_user@dft.gov.uk", roleId:1, roleName:"#notnull"}
