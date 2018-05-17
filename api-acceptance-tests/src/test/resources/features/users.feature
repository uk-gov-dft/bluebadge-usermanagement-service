@users
Feature: Verify users

  Scenario: Verify a valid user in an authority
    Given url 'http://localhost:8180/uk-gov-dft/service-template-api/1.0.0/users?emailAddress=sampath@dft.co.uk'
    When method GET
    Then status 200
    And match $ == "true"

  Scenario: Verify an invalid user in an authority
    Given url 'http://localhost:8180/uk-gov-dft/service-template-api/1.0.0/users?emailAddress=nouser@dft.co.uk'
    When method GET
    Then status 200
    And match $ == "false"