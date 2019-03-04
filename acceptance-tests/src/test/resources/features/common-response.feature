@users-update
Feature: Verify bad requests return a common response wrapping errors

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2-user.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Verify invalid bean validation value in body
    Given path 'users'
    And request { emailAddress:"abcnobody@dft.gov.uk", name:"" }
    When method POST
    Then status 400
    And match $.error.errors contains {field:"name", reason:"#notnull", message:"NotNull.user.name", location:"#null", locationType:"#null"}

  Scenario: Verify cannot parse body
    Given path 'users'
    And request { name:"Delete Me", emailAddress:"um_createuservalid@dft.gov.uk", localAuthorityShortCode: "ABERD", roleId: "I cant deserialise" }
    When method POST
    Then status 400
    And match $.error.errors contains {field:"roleId", reason:"`I cant deserialise` is not a valid Integer.", message:"InvalidFormat.roleId", location:"#null", locationType:"#null"}

