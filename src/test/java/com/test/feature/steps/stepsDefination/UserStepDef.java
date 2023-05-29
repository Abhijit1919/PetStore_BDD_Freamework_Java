package com.test.feature.steps.stepsDefination;


import com.test.feature.pojo.users.UserInfo;
import com.test.feature.steps.genric.LoginAPISteps;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import org.assertj.core.api.SoftAssertions;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;

@RunWith(SerenityRunner.class)
public class UserStepDef {

    public Response res = null; // Response
    public JsonPath jp = null; // JsonPath
    public RequestSpecification requestSpec = null;
    public UserInfo newUser = null;
    private SoftAssertions softAssertion = null;
    @Before
    public void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
        this.softAssertion = new SoftAssertions();
    }

    @After
    public void tearDown() {
        this.softAssertion.assertAll();
        RestAssured.reset();
    }
    @Steps
    LoginAPISteps loginAPI;

    @Given("^I provide below login information$")
    public void i_provide_login_information(List<Map<String, String>> listOfUserInfo) {
        Map<String, String> mapUserInfo = listOfUserInfo.get(0);
        this.newUser = loginAPI.createUserClass(mapUserInfo);
    }

    @When("^I send request to crete user$")
    public void i_send_request_to_crete_user() {
        res = loginAPI.createAndPostUserRequest(this.newUser);
    }

    @Then("^Create user is successful$")
    public void create_user_is_successful() {
        loginAPI.validateCreateUserResponse(res);
    }

    @And("^Allow to fetch the user details by \"([^\"]*)\"$")
    public void allow_to_fetch_the_user_details_by(String userName) {
        UserInfo actualUser = loginAPI.fetchUserInfoByUserName(userName);
        //Failure: userID is not matching
        this.softAssertion.assertThat(newUser.getId()).isEqualTo(actualUser.getId());
        //Assert.assertEquals("Validate user id", newUser.getId(), actualUser.getId());
        loginAPI.compareUserInfo(softAssertion, newUser,actualUser);

    }

    @And("^Allow to updated the First name \"([^\"]*)\" where Username \"([^\"]*)\"$")
    public void allow_to_updated_the_First_name_where_Username(String firstName,String userName) {
        UserInfo expectedUser = loginAPI.fetchUserInfoByUserName(userName);
        expectedUser.setFirstName(firstName);
        res = loginAPI.UpdateUserRequest(expectedUser);
        loginAPI.validateUpdateUserResponse(res);
        UserInfo actualUser = loginAPI.fetchUserInfoByUserName(userName);
        loginAPI.compareUserInfo(this.softAssertion, expectedUser,actualUser);
    }

    @And("^Allow to updated the Last name \"([^\"]*)\" where Username \"([^\"]*)\"$")
    public void allow_to_updated_the_Last_name_where_Username(String lastName,String userName) {
        UserInfo expectedUser = loginAPI.fetchUserInfoByUserName(userName);
        expectedUser.setLastName(lastName);
        res = loginAPI.UpdateUserRequest(expectedUser);
        loginAPI.validateUpdateUserResponse(res);
        UserInfo actualUser = loginAPI.fetchUserInfoByUserName(userName);
        loginAPI.compareUserInfo(this.softAssertion,expectedUser,actualUser);
    }

    @And("^Allow to updated the Email \"([^\"]*)\" where Username \"([^\"]*)\"$")
    public void allow_to_updated_the_Email_where_Username(String email,String userName) {
        UserInfo expectedUser = loginAPI.fetchUserInfoByUserName(userName);
        expectedUser.setEmail(email);
        res = loginAPI.UpdateUserRequest(expectedUser);
        loginAPI.validateUpdateUserResponse(res);
        UserInfo actualUser = loginAPI.fetchUserInfoByUserName(userName);
        loginAPI.compareUserInfo(this.softAssertion,expectedUser,actualUser);
    }

    @And("^Allow to updated password \"([^\"]*)\" where Username \"([^\"]*)\"$")
    public void allow_to_updated_password_where_Username(String password,String userName) {
        UserInfo expectedUser = loginAPI.fetchUserInfoByUserName(userName);
        expectedUser.setPassword(password);
        res = loginAPI.UpdateUserRequest(expectedUser);
        loginAPI.validateUpdateUserResponse(res);
        UserInfo actualUser = loginAPI.fetchUserInfoByUserName(userName);
        loginAPI.compareUserInfo(this.softAssertion,expectedUser,actualUser);
    }

    @And("^Allow to updated phone number \"([^\"]*)\" where Username \"([^\"]*)\"$")
    public void allow_to_updated_phone_number_where_Username(String phoneNumber,String userName) {
        UserInfo expectedUser = loginAPI.fetchUserInfoByUserName(userName);
        expectedUser.setPhone(phoneNumber);
        res = loginAPI.UpdateUserRequest(expectedUser);
        loginAPI.validateUpdateUserResponse(res);
        UserInfo actualUser = loginAPI.fetchUserInfoByUserName(userName);
        loginAPI.compareUserInfo(this.softAssertion,expectedUser,actualUser);
    }

    @And("^Delete the user with username \"([^\"]*)\"$")
    public void delete_the_user_with_username(String userName) {
        loginAPI.deleteUserByUserName(userName);
    }

}
