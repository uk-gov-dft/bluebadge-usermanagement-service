@users
Feature: Verify users

  Scenario: Verify retrieval of a specific user
    Given url 'http://localhost:8180/uk-gov-dft/service-template-api/1.0.0/authorities/2/users?name=abcnobody@dft.gov.uk'
    When method GET
    Then status 200
    And match each $.data.users contains {id:"#notnull", name:"nobody", emailAddress:"abcnobody@dft.gov.uk"}


  Scenario: Verify retrieve all users for an authority
    Given url 'http://localhost:8180/uk-gov-dft/service-template-api/1.0.0/authorities/2/users'
    When method GET
    Then status 200
    And match each $.data.users contains {id:"#notnull", name:"#notnull", emailAddress:"#notnull"}

  Scenario: Verify retrieve all users for an authority with name filter match
    Given url 'http://localhost:8180/uk-gov-dft/service-template-api/1.0.0/authorities/2/users?name=Sam'
    When method GET
    Then status 200
    And match each $.data.users contains {id:"#notnull", name:"#notnull", emailAddress:"#notnull"}

  Scenario: Verify retrieve all users for an authority with name filter match where match case insensitive
    Given url 'http://localhost:8180/uk-gov-dft/service-template-api/1.0.0/authorities/2/users?name=sAmPaTh'
    When method GET
    Then status 200
    And match each $.data.users contains {id:"#notnull", name:"#notnull", emailAddress:"#notnull"}

  Scenario: Verify retrieve all users for an authority with emailAddress filter match
    Given url 'http://localhost:8180/uk-gov-dft/service-template-api/1.0.0/authorities/2/users?name=def@'
    When method GET
    Then status 200
    And match each $.data.users contains {id:"#notnull", name:"#notnull", emailAddress:"#notnull"}

  Scenario: Verify retrieve all users for an authority with filter not matching
    Given url 'http://localhost:8180/uk-gov-dft/service-template-api/1.0.0/authorities/2/users?name=zzzzzzzzzz'
    When method GET
    Then status 200
    And match $.data.totalItems == 0

  Scenario: Create User Missing email and name as only spaces
    Given url 'http://localhost:8180/uk-gov-dft/service-template-api/1.0.0//authorities/2/users'
    And request {name: " "}
    When method POST
    Then status 400
    And match $.error.errors contains {field:"emailAddress", reason:"#notnull", message:"NotNull.user.emailAddress", location:"#null", locationType:"#null"}
    And match $.error.errors contains {field:"name", reason:"#notnull", message:"NotNull.user.name", location:"#null", locationType:"#null"}

  Scenario: Create User A bean and non bean error also zero length string
    Given url 'http://localhost:8180/uk-gov-dft/service-template-api/1.0.0//authorities/2/users'
    And request { emailAddress:"abcnobody@dft.gov.uk", name:"" }
    When method POST
    Then status 400
    And match $.error.errors contains {field:"emailAddress", reason:"#notnull", message:"AlreadyExists.user.emailAddress", location:"#null", locationType:"#null"}
    And match $.error.errors contains {field:"name", reason:"#notnull", message:"NotNull.user.name", location:"#null", locationType:"#null"}

  Scenario: Create User All valid except email already exists
    Given url 'http://localhost:8180/uk-gov-dft/service-template-api/1.0.0//authorities/2/users'
    And request { name:"asdfgh", emailAddress:"abcnobody@dft.gov.uk", localAuthorityId: 2 }
    When method POST
    Then status 400
    And match $.error.errors contains {field:"emailAddress", reason:"#notnull", message:"AlreadyExists.user.emailAddress", location:"#null", locationType:"#null"}

  Scenario: Create user invalid name format
    Given url 'http://localhost:8180/uk-gov-dft/service-template-api/1.0.0//authorities/2/users'
    And request { name:"as1dfgh", emailAddress:"@dft.gov.uk", localAuthorityId: 2 }
    When method POST
    Then status 400
    And match $.error.errors contains {field:"name", reason:"#notnull", message:"Pattern.user.name", location:"#null", locationType:"#null"}

  Scenario: Create User Invalid email format
    Given url 'http://localhost:8180/uk-gov-dft/service-template-api/1.0.0//authorities/2/users'
    And request { name:"asdfgh", emailAddress:"@dft.gov.uk", localAuthorityId: 2 }
    When method POST
    Then status 400
    And match $.error.errors contains {field:"emailAddress", reason:"#notnull", message:"Pattern.user.emailAddress", location:"#null", locationType:"#null"}

  Scenario: Create User All valid
    Given url 'http://localhost:8180/uk-gov-dft/service-template-api/1.0.0//authorities/2/users'
    And request { name:"Delete Me", emailAddress:"createuservalid@dft.gov.uk", localAuthorityId: 2, roleId: 2 }
    When method POST
    Then status 200
    And match $.data contains {id:"#notnull"}

  Scenario: Verify retrieval of a specific user by email address
    Given url 'http://localhost:8180/uk-gov-dft/service-template-api/1.0.0/users?emailAddress=Abcnobody@dft.gov.uk'
    When method GET
    Then status 200
    And match $.data.totalItems == 1

  Scenario: Verify retrieval of user by nonexists email address
    Given url 'http://localhost:8180/uk-gov-dft/service-template-api/1.0.0/users?emailAddress=JJJKKKFFFUUUZZZ@dft.gov.uk'
    When method GET
    Then status 200
    And match $.data.totalItems == 0