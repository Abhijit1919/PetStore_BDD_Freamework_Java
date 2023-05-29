Feature: User - Operations about users

  Scenario Outline: As a Admin User, I can create new user, view, update, delete the existing user
    Given I provide below login information

      | id | userName | firstname | lastname | email              | password | phoneNumber | userStatus |
      | 20 | testuser | firstname | lastname | testuser@gmail.com | test123  | 123456789   | 0          |

    When I send request to crete user
    Then Create user is successful
    And Allow to fetch the user details by "<userName>"
    And Allow to updated the First name "<firstname>" where Username "<userName>"
    And Allow to updated the Last name "<lastname>" where Username "<userName>"
    And Allow to updated the Email "<email>" where Username "<userName>"
    And Allow to updated password "<changepassword>" where Username "<userName>"
    And Allow to updated phone number "<phoneNumber>" where Username "<userName>"
    And Delete the user with username "<userName>"

    Examples: Valid
      | firstname | lastname | email              | changepassword | phoneNumber | userName |
      | pet       | store    | petstore@gmail.com | test1234       | 987654321   | testuser |