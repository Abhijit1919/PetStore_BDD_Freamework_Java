Feature: User - Operations about users

  As a Admin User, I can create multiple user either using Array or List

  Scenario Outline: As a Admin User, I can create multiple user with Array
    Given I provide below user information to create array

      | id | userName  | firstname  | lastname  | email               | password | phoneNumber | userStatus |
      | 20 | testuser1 | firstname  | lastname  | testuser@gmail.com  | test123  | 123456789   | 0          |
      | 21 | testuser2 | firstname2 | lastname2 | testuser2@gmail.com | test2123 | 123456789   | 0          |

    When I send request to crete user with Array
    Then Create user is successful with Array
    And Allow to fetch the list of user details by "<beforUpdate>"
    And Allow to fetch the list of user details by "<afterUpdate>"

    Examples: Valid
      | beforUpdate | afterUpdate |
      | testuser1   | testuser2   |

  Scenario Outline: As a Admin User, I can create multiple user with List
    Given I provide below user information to create list

      | id | userName  | firstname  | lastname  | email               | password | phoneNumber | userStatus |
      | 22 | testuser3 | firstname3 | lastname3 | testuser3@gmail.com | test123  | 123456789   | 0          |
      | 23 | testuser4 | firstname4 | lastname4 | testuser4@gmail.com | test2123 | 123456789   | 0          |


    When I send request to crete user with list
    Then Create user is successful with list
    And Allow to fetch the list of user details by "<beforUpdate>"
    And Allow to fetch the list of user details by "<afterUpdate>"

    Examples: Valid
      | beforUpdate | afterUpdate |
      | testuser3   | testuser4   |