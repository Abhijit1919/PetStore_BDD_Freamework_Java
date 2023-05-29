package com.test.feature.steps.stepsDefination;

import com.test.feature.pojo.users.UserAPIResponse;
import com.test.feature.steps.genric.LoginAPISteps;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import org.junit.Assert;
import org.junit.runner.RunWith;

@RunWith(SerenityRunner.class)
public class LoginStepDef {
    private Response res = null; //Response
    private RequestSpecification requestSpec = null;
    @Before
    public void setup()
    {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @After
    public void tearDown()
    {
        RestAssured.reset();
    }

    @Steps
    LoginAPISteps loginAPI;

    @Given("^I provide login credentials \"([^\"]*)\" and \"([^\"]*)\"$")
    public void i_provide_login_credentials_and(String username, String password){
        //GET URL Path
        String urlPath = "/user/login?username=" + username + "&password=" + password;
        //Add user name and password into session variable
        Serenity.setSessionVariable("username").to(username);
        Serenity.setSessionVariable("password").to(password);
        //This method call APIRequestBuilder and
        this.requestSpec = loginAPI.givenUserDetails(urlPath, username,password);
    }


    @When("^I send request to login$")
    public void i_send_request_to_login() {
        //Call Post method
        this.res = loginAPI.postLoginRequest(this.requestSpec);
    }

    @Then("^login failed$")
    public void login_failed() {
        //Fetch user name and password from session variable
        String username = Serenity.sessionVariableCalled("username").toString();
        String password = Serenity.sessionVariableCalled("password").toString();
        //Verify login failure
        loginAPI.verifyLoginFailure(this.res, username,password);
    }

    @Then("^login is successful$")
    public void login_is_successful() {
        //Fetch user name and password from session variable
        String username = Serenity.sessionVariableCalled("username").toString();
        String password = Serenity.sessionVariableCalled("password").toString();
        //Verify login success
        loginAPI.verifyLoginSuccess(res, username,password);
    }

    @Then("^logout is successful$")
    public void logout_is_successful() {
        //GET URL Path
        String urlPath = "/user/logout";
        //Verify logout successful
        UserAPIResponse expectedResponse= loginAPI.verifyLogoutSuccess(urlPath);
        Assert.assertEquals("Status Check Passed!", "200", expectedResponse.getCode().toString());
        Assert.assertNotNull("type field in response is not empty", expectedResponse.getType());
        Assert.assertEquals("Message return id", "ok", expectedResponse.getMessage());
    }

}
