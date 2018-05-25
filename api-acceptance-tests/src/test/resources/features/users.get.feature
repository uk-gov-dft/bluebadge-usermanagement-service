@users
Feature: Verify users retrieval

  Background:
  * url baseUrl

  Scenario: Verify retrieve all users for an authority
    Given path 'authorities/2/users'
    When method GET
    Then status 200
    And match each $.data.users contains {id:"#notnull", name:"#notnull", emailAddress:"#notnull"}

  Scenario: Verify retrieve all users for an authority with name filter match
    Given path 'authorities/2/users'
    And param name = 'Sam'
    When method GET
    Then status 200
    And match each $.data.users contains {id:"#notnull", name:"#notnull", emailAddress:"#notnull"}

  Scenario: Verify retrieve all users for an authority with name filter match where match case insensitive
    Given path 'authorities/2/users'
    And param name = 'sAmPaTh'
    When method GET
    Then status 200
    And match each $.data.users contains {id:"#notnull", name:"#notnull", emailAddress:"#notnull"}

  Scenario: Verify retrieve all users for an authority with emailAddress filter match
    Given path 'authorities/2/users'
    And param name = 'def@'
    When method GET
    Then status 200
    And match each $.data.users contains {id:"#notnull", name:"#notnull", emailAddress:"#notnull"}

  Scenario: Verify retrieve all users for an authority with filter not matching
    Given path 'authorities/2/users'
    And param name = 'zzzzzzzzzz'
    When method GET
    Then status 200
    And match $.data.totalItems == 0

  Scenario: Verify retrieval of a specific user by email address
    Given path 'users'
    And param emailAddress = 'Abcnobody@dft.gov.uk'
    When method GET
    Then status 200
    And match $.data.totalItems == 1

  Scenario: Verify retrieval of user by nonexists email address
    Given path 'users'
    And param emailAddress = 'JJJKKKFFFUUUZZZ@dft.gov.uk'
    When method GET
    Then status 200
    And match $.data.totalItems == 0