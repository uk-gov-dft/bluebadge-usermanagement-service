@users-update
Feature: Verify users update

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2.feature')
    * header Authorization = 'Bearer ' + result.accessToken

  Scenario: Update User Missing email and name as only spaces
    Given path 'users/cc4fbb98-3963-41ef-9c75-a9651b2fe27c'
    And request {uuid: "cc4fbb98-3963-41ef-9c75-a9651b2fe27c", name: " "}
    When method PUT
    Then status 400
    And match $.error.errors contains {field:"emailAddress", reason:"#notnull", message:"NotNull.user.emailAddress", location:"#null", locationType:"#null"}
    And match $.error.errors contains {field:"name", reason:"#notnull", message:"NotNull.user.name", location:"#null", locationType:"#null"}

  Scenario: Update User Get a bad response
    Given path 'users/cc4fbb98-3963-41ef-9c75-a9651b2fe27c'
    And request {uuid: "cc4fbb98-3963-41ef-9c75-a9651b2fe27c", emailAddress:"abcnobody@dft.gov.uk", name:"" }
    When method PUT
    Then status 400
    And match $.error.errors contains {field:"name", reason:"#notnull", message:"NotNull.user.name", location:"#null", locationType:"#null"}

  Scenario: Update User All valid except email already exists
    Given path 'users/cc4fbb98-3963-41ef-9c75-a9651b2fe27c'
    And request {uuid: "cc4fbb98-3963-41ef-9c75-a9651b2fe27c", name:"asdfgh", emailAddress:"abcnobody@dft.gov.uk", localAuthorityShortCode: "ABERD", roleId: 2 }
    When method PUT
    Then status 400
    And match $.error.errors contains {field:"emailAddress", reason:"#notnull", message:"AlreadyExists.user.emailAddress", location:"#null", locationType:"#null"}

  Scenario: Update User All valid except local authority different from current user's
    Given path 'users/cc4fbb98-3963-41ef-9c75-a9651b2fe27c'
    And request {uuid: "cc4fbb98-3963-41ef-9c75-a9651b2fe27c", name:"asdfgh", emailAddress:"abcnobodydifferentlocalauthority@dft.gov.uk", localAuthorityShortCode: "MANC", roleId: 2 }
    When method PUT
    Then status 400
    And match $.error.errors contains {field:"localAuthority", reason:"#notnull", message:"NotSameAsCurrentUsers.user.localAuthority", location:"#null", locationType:"#null"}

  Scenario: Update user invalid name format
    Given path 'users/cc4fbb98-3963-41ef-9c75-a9651b2fe27c'
    And request {uuid: "cc4fbb98-3963-41ef-9c75-a9651b2fe27c", name:"as1dfgh", emailAddress:"@dft.gov.uk", localAuthorityShortCode: "ABERD" }
    When method PUT
    Then status 400
    And match $.error.errors contains {field:"name", reason:"#notnull", message:"Pattern.user.name", location:"#null", locationType:"#null"}

  Scenario: Update User Invalid email format
    Given path 'users/cc4fbb98-3963-41ef-9c75-a9651b2fe27c'
    And request {uuid: "cc4fbb98-3963-41ef-9c75-a9651b2fe27c", name:"asdfgh", emailAddress:"@dft.gov.uk", localAuthorityShortCode: "ABERD" }
    When method PUT
    Then status 400
    And match $.error.errors contains {field:"emailAddress", reason:"#notnull", message:"Pattern.user.emailAddress", location:"#null", locationType:"#null"}

  Scenario: Update User All valid change role email address unchanged so only exists in this record
    Given path 'users/9cb7a2f9-9e03-4277-9725-67fabb21847f'
    And request {uuid: "9cb7a2f9-9e03-4277-9725-67fabb21847f", name:"Delete Me", emailAddress:"updateme@dft.gov.uk", localAuthorityShortCode: "ABERD", roleId: 1 }
    When method PUT
    Then status 200
    And match $.data contains {uuid:"#notnull"}

  Scenario: Update User does not exist
    Given path 'users/2bcb82f8-3eba-4184-8753-ce011b39b5b7'
    And request {uuid: "2bcb82f8-3eba-4184-8753-ce011b39b5b7", name:"Delete Me", emailAddress:"updatemeNewemail@dft.gov.uk", localAuthorityShortCode: "ABERD", roleId: 1 }
    When method PUT
    Then status 404

  Scenario: Update User invalid uuid in param
    Given path 'users/2bcb82f8-3eba-4184-8753-ce011b39b5b7888'
    And request {uuid: "2bcb82f8-3eba-4184-8753-ce011b39b5b7", name:"Delete Me", emailAddress:"updatemeNewemail@dft.gov.uk", localAuthorityShortCode: "ABERD", roleId: 1 }
    When method PUT
    Then status 404

  Scenario: Update User invalid uuid in body
    Given path 'users/9cb7a2f9-9e03-4277-9725-67fabb21847f'
    And request {uuid: "2bcb82f8-3eba-4184-8753-ce011b39b5b7888", name:"Delete Me", emailAddress:"updatemeNewemail@dft.gov.uk", localAuthorityShortCode: "ABERD", roleId: 1 }
    When method PUT
    Then status 400