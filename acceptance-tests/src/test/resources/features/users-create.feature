@users-create
Feature: Verify users create

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2-user.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader

  Scenario: Create User Missing email and name as only spaces
    Given path 'users'
    And request {name: " "}
    When method POST
    Then status 400
    And match $.error.errors contains {field:"emailAddress", reason:"#notnull", message:"NotNull.user.emailAddress", location:"#null", locationType:"#null"}
    And match $.error.errors contains {field:"name", reason:"#notnull", message:"NotNull.user.name", location:"#null", locationType:"#null"}

  Scenario: Create User Get a bad request due to bean validation
    Given path 'users'
    And request { emailAddress:"abcnobody@dft.gov.uk", name:"" }
    When method POST
    Then status 400
    And match $.error.errors contains {field:"name", reason:"#notnull", message:"NotNull.user.name", location:"#null", locationType:"#null"}

  Scenario: Create User All valid except email already exists
    Given path 'users'
    And request { name:"asdfgh", emailAddress:"um_abcnobody@dft.gov.uk", localAuthorityShortCode: "ABERD", roleId: 2 }
    When method POST
    Then status 400
    And match $.error.errors contains {field:"emailAddress", reason:"#notnull", message:"AlreadyExists.user.emailAddress", location:"#null", locationType:"#null"}

  Scenario: Create User All valid except local authority id different from current users's
    Given path 'users'
    And request { name:"asdfgh", emailAddress:"abcnobodydifferentlocalauthority@dft.gov.uk", localAuthorityShortCode: "MANC", roleId: 2 }
    When method POST
    Then status 403

  Scenario: Create user invalid name format
    Given path 'users'
    And request { name:"as1dfgh", emailAddress:"@dft.gov.uk", localAuthorityShortCode: "ABERD" }
    When method POST
    Then status 400
    And match $.error.errors contains {field:"name", reason:"#notnull", message:"Pattern.user.name", location:"#null", locationType:"#null"}

  Scenario: Create User Invalid email format
    Given path 'users'
    And request { name:"asdfgh", emailAddress:"@dft.gov.uk", localAuthorityShortCode: "ABERD" }
    When method POST
    Then status 400
    And match $.error.errors contains {field:"emailAddress", reason:"#notnull", message:"Pattern.user.emailAddress", location:"#null", locationType:"#null"}

  Scenario: Create User All valid
    Given path 'users'
    And request { name:"Delete Me", emailAddress:"um_createuservalid@dft.gov.uk", localAuthorityShortCode: "ABERD", roleId: 2 }
    When method POST
    Then status 200
    And match $.data contains {uuid:"#notnull"}
