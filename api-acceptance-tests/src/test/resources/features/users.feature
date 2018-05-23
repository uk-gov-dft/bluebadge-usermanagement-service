@users
Feature: Verify users

  Scenario: Verify a valid user in an authority
    Given url 'http://localhost:8180/uk-gov-dft/service-template-api/1.0.0/users?emailAddress=abc@dft.gov.uk'
    When method GET
    Then status 200
    And match $ == "true"

  Scenario: Verify an invalid user in an authority
    Given url 'http://localhost:8180/uk-gov-dft/service-template-api/1.0.0/users?emailAddress=nouser@dft.co.uk'
    When method GET
    Then status 200
    And match $ == "false"

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