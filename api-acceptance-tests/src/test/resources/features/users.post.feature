@users-post
Feature: Verify users create

  Background:
    * url baseUrl

  Scenario: Create User Missing email and name as only spaces
    Given path 'authorities/2/users'
    And request {name: " "}
    When method POST
    Then status 400
    And match $.error.errors contains {field:"emailAddress", reason:"#notnull", message:"NotNull.user.emailAddress", location:"#null", locationType:"#null"}
    And match $.error.errors contains {field:"name", reason:"#notnull", message:"NotNull.user.name", location:"#null", locationType:"#null"}

  Scenario: Create User Get a bad request due to bean validation
    Given path 'authorities/2/users'
    And request { emailAddress:"abcnobody@dft.gov.uk", name:"" }
    When method POST
    Then status 400
    And match $.error.errors contains {field:"name", reason:"#notnull", message:"NotNull.user.name", location:"#null", locationType:"#null"}

  Scenario: Create User All valid except email already exists
    Given path 'authorities/2/users'
    And request { name:"asdfgh", emailAddress:"abcnobody@dft.gov.uk", localAuthorityId: 2, roleId: 2 }
    When method POST
    Then status 400
    And match $.error.errors contains {field:"emailAddress", reason:"#notnull", message:"AlreadyExists.user.emailAddress", location:"#null", locationType:"#null"}

  Scenario: Create user invalid name format
    Given path 'authorities/2/users'
    And request { name:"as1dfgh", emailAddress:"@dft.gov.uk", localAuthorityId: 2 }
    When method POST
    Then status 400
    And match $.error.errors contains {field:"name", reason:"#notnull", message:"Pattern.user.name", location:"#null", locationType:"#null"}

  Scenario: Create User Invalid email format
    Given path 'authorities/2/users'
    And request { name:"asdfgh", emailAddress:"@dft.gov.uk", localAuthorityId: 2 }
    When method POST
    Then status 400
    And match $.error.errors contains {field:"emailAddress", reason:"#notnull", message:"Pattern.user.emailAddress", location:"#null", locationType:"#null"}

  Scenario: Create User All valid
    Given path 'authorities/2/users'
    And request { name:"Delete Me", emailAddress:"createuservalid@dft.gov.uk", localAuthorityId: 2, roleId: 2 }
    When method POST
    Then status 200
    And match $.data contains {id:"#notnull"}
