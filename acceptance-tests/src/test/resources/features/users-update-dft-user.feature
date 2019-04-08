@users-update
Feature: Verify users update

  Background:
    * url baseUrl
    * def dbConfig = { username: 'developer',  ***REMOVED*** }
    * def DbUtils = Java.type('uk.gov.service.bluebadge.test.utils.DbUtils')
    * def db = new DbUtils(dbConfig)
    * def setup = callonce db.runScript('acceptance-test-data.sql')
    * def result = callonce read('./oauth2-dft-user.feature')
    * header Authorization = 'Bearer ' + result.accessToken
    * header Accept = jsonVersionHeader

  Scenario: Update User Missing email and name as only spaces
    Given path 'users/3bfe600b-4425-40cd-ad81-d75bbe16ee13'
    And request {uuid: "3bfe600b-4425-40cd-ad81-d75bbe16ee13", name: " "}
    When method PUT
    Then status 400
    And match $.error.errors contains {field:"emailAddress", reason:"#notnull", message:"NotNull.user.emailAddress", location:"#null", locationType:"#null"}
    And match $.error.errors contains {field:"name", reason:"#notnull", message:"NotNull.user.name", location:"#null", locationType:"#null"}

  Scenario: Update User Get a bad response
    Given path 'users/3bfe600b-4425-40cd-ad81-d75bbe16ee13'
    And request {uuid: "3bfe600b-4425-40cd-ad81-d75bbe16ee13", emailAddress:"abcnobody@dft.gov.uk", name:"" }
    When method PUT
    Then status 400
    And match $.error.errors contains {field:"name", reason:"#notnull", message:"NotNull.user.name", location:"#null", locationType:"#null"}

  Scenario: Update User All valid except email already exists
    Given path 'users/3bfe600b-4425-40cd-ad81-d75bbe16ee13'
    And request {uuid: "3bfe600b-4425-40cd-ad81-d75bbe16ee13", name:"asdfgh", emailAddress:"um_abcnobody@dft.gov.uk", localAuthorityShortCode: "ABERD", roleId: 2 }
    When method PUT
    Then status 400
    And match $.error.errors contains {field:"emailAddress", reason:"#notnull", message:"AlreadyExists.user.emailAddress", location:"#null", locationType:"#null"}

  Scenario: Update user invalid name format
    Given path 'users/3bfe600b-4425-40cd-ad81-d75bbe16ee13'
    And request {uuid: "3bfe600b-4425-40cd-ad81-d75bbe16ee13", name:"as1dfgh", emailAddress:"@dft.gov.uk", localAuthorityShortCode: "ABERD" }
    When method PUT
    Then status 400
    And match $.error.errors contains {field:"name", reason:"#notnull", message:"Pattern.user.name", location:"#null", locationType:"#null"}

  Scenario: Update User Invalid email format
    Given path 'users/3bfe600b-4425-40cd-ad81-d75bbe16ee13'
    And request {uuid: "3bfe600b-4425-40cd-ad81-d75bbe16ee13", name:"asdfgh", emailAddress:"@dft.gov.uk", localAuthorityShortCode: "ABERD" }
    When method PUT
    Then status 400
    And match $.error.errors contains {field:"emailAddress", reason:"#notnull", message:"Pattern.user.emailAddress", location:"#null", locationType:"#null"}

  Scenario: Update User change local authority different
    Given path 'users/3bfe600b-4425-40cd-ad81-d75bbe16ee13'
    And request {uuid: "3bfe600b-4425-40cd-ad81-d75bbe16ee13", name:"asdfgh", emailAddress:"um_abc@dft.gov.uk", localAuthorityShortCode: "MANC", roleId: 2 }
    When method PUT
    Then status 200
    And match $.data contains {uuid:"#notnull"}

  Scenario: Update User All valid change role email address unchanged so only exists in this record
    Given path 'users/9619e6a0-1e9e-4217-92b1-21f33b4b4762'
    And request {uuid: "9619e6a0-1e9e-4217-92b1-21f33b4b4762", name:"Delete Me", emailAddress:"um_updateme@dft.gov.uk", localAuthorityShortCode: "ABERD", roleId: 3 }
    When method PUT
    Then status 200
    And match $.data contains {uuid:"#notnull"}

  Scenario: Update User to Dft User
    Given path 'users/9619e6a0-1e9e-4217-92b1-21f33b4b4762'
    And request {uuid: "9619e6a0-1e9e-4217-92b1-21f33b4b4762", name:"Delete Me", emailAddress:"um_updateme@dft.gov.uk", roleId: 1 }
    When method PUT
    Then status 200
    And match $.data contains {uuid:"#notnull"}

  Scenario: Update User does not exist
    Given path 'users/2bcb82f8-3eba-4184-8753-ce011b39b5b7'
    And request {uuid: "2bcb82f8-3eba-4184-8753-ce011b39b5b7", name:"Delete Me", emailAddress:"um_updatemeNewemail@dft.gov.uk", localAuthorityShortCode: "ABERD", roleId: 1 }
    When method PUT
    Then status 404

  Scenario: Update User invalid uuid in param
    Given path 'users/2bcb82f8-3eba-4184-8753-ce011b39b5b7888'
    And request {uuid: "2bcb82f8-3eba-4184-8753-ce011b39b5b7", name:"Delete Me", emailAddress:"updatemeNewemail@dft.gov.uk", localAuthorityShortCode: "ABERD", roleId: 1 }
    When method PUT
    Then status 404

  Scenario: Update User invalid uuid in body
    Given path 'users/9619e6a0-1e9e-4217-92b1-21f33b4b4762'
    And request {uuid: "2bcb82f8-3eba-4184-8753-ce011b39b5b7888", name:"Delete Me", emailAddress:"um_updatemeNewemail@dft.gov.uk", localAuthorityShortCode: "ABERD", roleId: 1 }
    When method PUT
    Then status 400

  Scenario: Update User different valid uuids in param and body
    Given path 'users/b3f15ef4-3b71-47a8-9a5d-133de079ec4c'
    And request {uuid: "1dd704ed-4538-45e4-af10-e00fab8e27f1", name:"Delete Me", emailAddress:"updatemeNewemail@dft.gov.uk", localAuthorityShortCode: "ABERD", roleId: 1 }
    When method PUT
    Then status 400
