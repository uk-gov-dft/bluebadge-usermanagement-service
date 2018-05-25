@users-put
Feature: Verify users update

  Background:
    * url baseUrl

  Scenario: Update User Missing email and name as only spaces
    Given path 'authorities/2/users/-1'
    And request {id: -1, name: " "}
    When method PUT
    Then status 400
    And match $.error.errors contains {field:"emailAddress", reason:"#notnull", message:"NotNull.user.emailAddress", location:"#null", locationType:"#null"}
    And match $.error.errors contains {field:"name", reason:"#notnull", message:"NotNull.user.name", location:"#null", locationType:"#null"}

  Scenario: Update User A bean and non bean error also zero length string
    Given path 'authorities/2/users/-1'
    And request {id: -1, emailAddress:"abcnobody@dft.gov.uk", name:"" }
    When method PUT
    Then status 400
    And match $.error.errors contains {field:"emailAddress", reason:"#notnull", message:"AlreadyExists.user.emailAddress", location:"#null", locationType:"#null"}
    And match $.error.errors contains {field:"name", reason:"#notnull", message:"NotNull.user.name", location:"#null", locationType:"#null"}

  Scenario: Update User All valid except email already exists
    Given path 'authorities/2/users/-1'
    And request {id: -1, name:"asdfgh", emailAddress:"abcnobody@dft.gov.uk", localAuthorityId: 2 }
    When method PUT
    Then status 400
    And match $.error.errors contains {field:"emailAddress", reason:"#notnull", message:"AlreadyExists.user.emailAddress", location:"#null", locationType:"#null"}

  Scenario: Update user invalid name format
    Given path 'authorities/2/users/-1'
    And request {id: -1, name:"as1dfgh", emailAddress:"@dft.gov.uk", localAuthorityId: 2 }
    When method PUT
    Then status 400
    And match $.error.errors contains {field:"name", reason:"#notnull", message:"Pattern.user.name", location:"#null", locationType:"#null"}

  Scenario: Update User Invalid email format
    Given path 'authorities/2/users/-1'
    And request {id: -1, name:"asdfgh", emailAddress:"@dft.gov.uk", localAuthorityId: 2 }
    When method PUT
    Then status 400
    And match $.error.errors contains {field:"emailAddress", reason:"#notnull", message:"Pattern.user.emailAddress", location:"#null", locationType:"#null"}

  Scenario: Update User All valid change role email address unchanged so only exists in this record
    Given path 'authorities/2/users/-4'
    And request {id: -4, name:"Delete Me", emailAddress:"updateme@dft.gov.uk", localAuthorityId: 2, roleId: 1 }
    When method PUT
    Then status 200
    And match $.data contains {id:"#notnull"}
