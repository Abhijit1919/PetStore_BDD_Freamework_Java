package com.test.feature.steps.genric;


import com.test.feature.pojo.users.UserAPIResponse;
import com.test.feature.pojo.users.UserInfo;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.thucydides.core.annotations.Step;
import org.assertj.core.api.SoftAssertions;
import org.junit.Assert;

import java.util.Map;

public class LoginAPISteps {
	
	@Step
	public RequestSpecification givenUserDetails(String urlPath, String username, String password) {
		
		APIRequestBuilder apiRequestBuilder = new APIRequestBuilder(urlPath,"application/json",null);
		return RestAssured.given().spec(apiRequestBuilder.getRequestSpecification());

	}
	
	public Response postLoginRequest(RequestSpecification requestSpec) {
		Response res = requestSpec.when().get();
		return res;
	}

	@Step
	public String verifyLoginSuccess(Response res, String username, String password) {
		JsonPath jp = res.jsonPath();
		Assert.assertEquals("Status Check Failed!", 200, res.getStatusCode());
		Assert.assertTrue(jp.get("message").toString().contains("logged in user session:"));
		return "Status check passed > Response "+ res.getStatusCode();
	}
	
	@Step
	public UserAPIResponse verifyLogoutSuccess(String urlPath) {
		APIRequestBuilder apiRequestBuilder = new APIRequestBuilder(urlPath,"application/json",null);
		RequestSpecification requestSpec = RestAssured.given().spec(apiRequestBuilder.getRequestSpecification());
		Response res =  requestSpec.when().get();
		UserAPIResponse response =res.as(UserAPIResponse.class);
		return response;
	}

	@Step
	public void verifyLoginFailure(Response res, String username, String password) {
		JsonPath jp = res.jsonPath();
		Assert.assertEquals("Invalid user credentials allowed", 401, res.getStatusCode());
	}

	@Step
	public UserInfo createUserClass(Map<String, String> userInfo) {
		UserInfo newUser = new UserInfo(Integer.parseInt(userInfo.get("id")), userInfo.get("userName"), userInfo.get("firstname"),
				userInfo.get("lastname"), userInfo.get("email"), userInfo.get("password"), userInfo.get("phoneNumber"),
				Integer.parseInt(userInfo.get("userStatus")));
		return newUser;
		
	}
	
	@Step
	public Response createAndPostUserRequest(UserInfo newUser) {
		APIRequestBuilder apiRequestBuilder = new APIRequestBuilder("/user","application/json",newUser);
		RequestSpecification requestSpec = apiRequestBuilder.getRequestSpecification();
		requestSpec = RestAssured.given().spec(requestSpec);
		Response res = requestSpec.when().post();
		return res;
	}
	
	@Step
	public void validateCreateUserResponse(Response res) {
		UserAPIResponse userResponse = userResponseDeSerialization(res);
		Assert.assertEquals("Status Check Failed!", 200, res.getStatusCode());
		Assert.assertEquals("Validate Status code in response ", "200", userResponse.getCode()+"");
		Assert.assertEquals("Type ", "unknown", userResponse.getType());
		System.out.println("User message > "+ userResponse.getMessage());
		
	}
	
	@Step
	public Response UpdateUserRequest(UserInfo newUser) {
		APIRequestBuilder apiRequestBuilder = new APIRequestBuilder("/user/"+newUser.getUsername(),"application/json",newUser);
		RequestSpecification requestSpec = apiRequestBuilder.getRequestSpecification();
		requestSpec = RestAssured.given().spec(requestSpec);
		Response res = requestSpec.when().put();
		return res;
	}
	
	@Step
	public void validateUpdateUserResponse(Response res) {
		validateCreateUserResponse(res);
		
	}
	@Step
	public UserAPIResponse userResponseDeSerialization(Response res) {
		return res.as(UserAPIResponse.class);
	}

	public UserInfo fetchUserInfoByUserName(String userName) {
		APIRequestBuilder apiRequestBuilder = new APIRequestBuilder("/user/"+userName,"application/json",null);
		RequestSpecification requestSpec = apiRequestBuilder.getRequestSpecification();
		requestSpec = RestAssured.given().spec(requestSpec);
		Response res = requestSpec.when().get();
		return res.as(UserInfo.class);
	}
	
	public void deleteUserByUserName(String userName) {
		APIRequestBuilder apiRequestBuilder = new APIRequestBuilder("/user/"+userName,"application/json",null);
		RequestSpecification requestSpec = apiRequestBuilder.getRequestSpecification();
		requestSpec = RestAssured.given().spec(requestSpec);
		Response res = requestSpec.when().delete();
		UserAPIResponse userResponse = userResponseDeSerialization(res);
		Assert.assertEquals("Status Check Failed!", 200, res.getStatusCode());
		Assert.assertEquals("Validate Status code in response ", "200", userResponse.getCode()+"");
		Assert.assertEquals("Type ", "unknown", userResponse.getType());
		Assert.assertEquals("User message ", userName, userResponse.getMessage());
	}

	public void compareUserInfo(SoftAssertions softAssertion, UserInfo expected, UserInfo actual) {
		softAssertion.assertThat(expected.getUsername()).isEqualTo(actual.getUsername());
		softAssertion.assertThat(expected.getFirstName()).isEqualTo(actual.getFirstName());
		softAssertion.assertThat(expected.getLastName()).isEqualTo(actual.getLastName());
		softAssertion.assertThat(expected.getEmail()).isEqualTo(actual.getEmail());
		softAssertion.assertThat(expected.getPassword()).isEqualTo(actual.getPassword());
		softAssertion.assertThat(expected.getPhone()).isEqualTo(actual.getPhone());
		softAssertion.assertThat(expected.getUserStatus()).isEqualTo(actual.getUserStatus());
		
//		Assert.assertEquals("Validate user name", expected.getUsername(), actual.getUsername());
//		Assert.assertEquals("Validate user firstname", expected.getFirstName(), actual.getFirstName());
//		Assert.assertEquals("Validate user lastname", expected.getLastName(), actual.getLastName());
//		Assert.assertEquals("Validate user email", expected.getEmail(), actual.getEmail());
//		Assert.assertEquals("Validate user passowrd", expected.getPassword(), actual.getPassword());
//		Assert.assertEquals("Validate user phone", expected.getPhone(), actual.getPhone());
//		Assert.assertEquals("Validate user user status", expected.getUserStatus(), actual.getUserStatus());
	}
}
