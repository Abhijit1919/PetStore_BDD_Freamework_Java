package com.test.feature.steps.stepsDefination;


import com.test.feature.pojo.users.UserInfo;
import com.test.feature.steps.genric.APIRequestBuilder;
import com.test.feature.steps.genric.LoginAPISteps;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import org.assertj.core.api.SoftAssertions;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SerenityRunner.class)
public class CreateMultiUserStepDef {
    private Response res = null; // Response
    private RequestSpecification requestSpec = null;
    private List<UserInfo> userList = null;
    private UserInfo userArray[] = null;

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

    @Given("^I provide below user information to create array$")
    public void i_provide_user_information_and_to_create_array(List<Map<String, String>> listOfUserInfo){
        this.userArray = new UserInfo[listOfUserInfo.size()];
        this.userList = new ArrayList<UserInfo>();
        for (Map<String, String> map : listOfUserInfo) {
            this.userList.add(loginAPI.createUserClass(map));
        }
        this.userArray = this.userList.toArray(this.userArray);
        APIRequestBuilder apiRequestBuilder = new APIRequestBuilder("/user/createWithArray","application/json",this.userArray);
        this.requestSpec = apiRequestBuilder.getRequestSpecification();
        this.requestSpec = RestAssured.given().spec(this.requestSpec);

    }


    @When("^I send request to crete user with Array$")
    public void i_send_request_to_crete_user_with_Array_list() {
        this.res = this.requestSpec.when().post();

    }

    @Then("^Create user is successful with Array$")
    public void create_user_is_successful_with_Array_list() {
        loginAPI.validateCreateUserResponse(this.res);
    }

    @And("^Allow to fetch the list of user details by \"([^\"]*)\"$")
    public void allow_to_fetch_the_list_of_user_details_by(String userName) throws Exception {
        UserInfo actualUserData = loginAPI.fetchUserInfoByUserName(userName);
        UserInfo expectedUserData = null;
        for (UserInfo userInfo : this.userList) {
            if(userName.equals(userInfo.getUsername())) {
                expectedUserData = userInfo;
            }
        }
        loginAPI.compareUserInfo(softAssertion, expectedUserData,actualUserData);
    }

    @Given("^I provide below user information to create list$")
    public void i_provide_user_information_and_to_create_list(List<Map<String, String>> listOfUserInfo){
        this.userList = new ArrayList<UserInfo>();
        for (Map<String, String> map : listOfUserInfo) {
            this.userList.add(loginAPI.createUserClass(map));
        }

        APIRequestBuilder apiRequestBuilder = new APIRequestBuilder("/user/createWithArray","application/json",this.userList);
        this.requestSpec = apiRequestBuilder.getRequestSpecification();
        this.requestSpec = RestAssured.given().spec(this.requestSpec);
    }

    @When("^I send request to crete user with list$")
    public void i_send_request_to_crete_user_with_list(){
        this.res = this.requestSpec.when().post();
    }


    @Then("^Create user is successful with list$")
    public void create_user_is_successful_with_list() {
        loginAPI.validateCreateUserResponse(this.res);
    }

}
