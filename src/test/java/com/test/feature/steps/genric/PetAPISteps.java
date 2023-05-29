package com.test.feature.steps.genric;

import com.test.feature.pojo.pet.Category;
import com.test.feature.pojo.pet.PetAPIResponse;
import com.test.feature.pojo.pet.PetInfo;
import com.test.feature.pojo.pet.Tag;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.thucydides.core.annotations.Step;
import org.assertj.core.api.SoftAssertions;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PetAPISteps {


    public PetInfo createPetClass(Map<String, String> petData) {
        Category category = new Category(Integer.parseInt(petData.get("categoryId")),petData.get("categoryName"));
        List<String> photoUrls = new ArrayList<>();
        photoUrls.add(petData.get("photoUrls"));
        List<Tag> tags = new ArrayList<>();
        Tag tagData = new Tag(Integer.parseInt(petData.get("tagsId")),petData.get("tagsName"));
        tags.add(tagData);
        PetInfo petInfo = new PetInfo(Integer.parseInt(petData.get("petId")),category,petData.get("petName"),photoUrls,tags,petData.get("status"));
        return petInfo;
    }

    public Response createPetRequest(String url, PetInfo petInfo) {
        APIRequestBuilder apiRequestBuilder = new APIRequestBuilder(url, "application/json", petInfo);
        RequestSpecification requestSpec = apiRequestBuilder.getRequestSpecification();
        requestSpec = RestAssured.given().spec(requestSpec);
        Response res = requestSpec.when().post();
        return res;

    }
    @Step
    public PetInfo validatePetInfoIsAdded(Response res) {
        PetInfo petInfo = petResponseDeSerialization(res);
        Assert.assertEquals("Status Check Passed!", 200, res.getStatusCode());
        return petInfo;
    }

    private PetInfo petResponseDeSerialization(Response res) {
        return res.as(PetInfo.class);
    }

    public void comparePetInfo(SoftAssertions softAssertion, PetInfo expectedPetInfo, PetInfo actualPetResponse) {
        softAssertion.assertThat(expectedPetInfo.getId()).isEqualTo(actualPetResponse.getId());
        softAssertion.assertThat(expectedPetInfo.getName()).isEqualTo(actualPetResponse.getName());
        softAssertion.assertThat(expectedPetInfo.getStatus()).isEqualTo(actualPetResponse.getStatus());
        softAssertion.assertThat(expectedPetInfo.getCategory().getId()).isEqualTo(actualPetResponse.getCategory().getId());
        softAssertion.assertThat(expectedPetInfo.getCategory().getName()).isEqualTo(actualPetResponse.getCategory().getName());
        softAssertion.assertThat(expectedPetInfo.getTags().get(0).getId()).isEqualTo(actualPetResponse.getTags().get(0).getId());
        softAssertion.assertThat(expectedPetInfo.getTags().get(0).getName()).isEqualTo(actualPetResponse.getTags().get(0).getName());
        softAssertion.assertThat(expectedPetInfo.getPhotoUrls().get(0)).isEqualTo(actualPetResponse.getPhotoUrls().get(0));

    }

    public PetInfo fetchPetInfoById(String url,String petId) {
        APIRequestBuilder apiRequestBuilder = new APIRequestBuilder(url + "/" + petId, "application/json", null);
        RequestSpecification requestSpec = apiRequestBuilder.getRequestSpecification();
        requestSpec = RestAssured.given().spec(requestSpec);
        Response res = requestSpec.when().get();
        PetInfo expectedResponse = res.as(PetInfo.class);
        Assert.assertEquals("Status Check Passed!", 200, res.getStatusCode());
        return expectedResponse;

    }

    public PetAPIResponse uploadImageOfPetById(String image, String petUrl, String petId) {
        APIRequestBuilder apiRequestBuilder = new APIRequestBuilder(petUrl + "/" + petId+"/uploadImage",image);
        RequestSpecification requestSpec = apiRequestBuilder.getRequestSpecification();
        requestSpec = RestAssured.given().spec(requestSpec);
        Response res = requestSpec.when().post();
        Assert.assertEquals("Status Check Passed!", 200, res.getStatusCode());
        return res.as(PetAPIResponse.class);
    }

    public PetAPIResponse deletePetInfoById(String petUrl, String petId) {
        APIRequestBuilder apiRequestBuilder = new APIRequestBuilder(petUrl + "/" + petId, "application/json", null);
        RequestSpecification requestSpec = apiRequestBuilder.getRequestSpecification();
        requestSpec = RestAssured.given().spec(requestSpec);
        Response res = requestSpec.when().delete();
        PetAPIResponse expectedResponse = res.as(PetAPIResponse.class);
        Assert.assertEquals("Status Check Passed!", 200, res.getStatusCode());
        res = requestSpec.when().get();
        Assert.assertEquals("Verify pet record is deleted!", 404, res.getStatusCode());
        return expectedResponse;
    }

    public PetInfo updatePetRequest(String petUrl, PetInfo toBeUpdated) {
        APIRequestBuilder apiRequestBuilder = new APIRequestBuilder(petUrl,"application/json",toBeUpdated);
        RequestSpecification requestSpec = apiRequestBuilder.getRequestSpecification();
        requestSpec = RestAssured.given().spec(requestSpec);
        Response res = requestSpec.when().put();
        PetInfo expectedResponse = res.as(PetInfo.class);
        Assert.assertEquals("Status Check Passed!", 200, res.getStatusCode());
        return expectedResponse;
    }

    public PetInfo[] findPetInfoByStatus(String petUrl) {
        APIRequestBuilder apiRequestBuilder = new APIRequestBuilder(petUrl,"application/json",null);
        RequestSpecification requestSpec = apiRequestBuilder.getRequestSpecification();
        requestSpec = RestAssured.given().spec(requestSpec);
        Response res = requestSpec.when().get();
        PetInfo[] expectedResponse = res.as(PetInfo[].class);
        Assert.assertEquals("Status Check Passed!", 200, res.getStatusCode());
        return expectedResponse;
    }

    public PetAPIResponse updatePetDataWithFormData(String url, String formData) {
        APIRequestBuilder apiRequestBuilder = new APIRequestBuilder(url, "application/x-www-form-urlencoded", formData);
        RequestSpecification requestSpec = apiRequestBuilder.getRequestSpecification();
        requestSpec = RestAssured.given().spec(requestSpec);
        Response res = requestSpec.when().post();
        Assert.assertEquals("Status Check Passed!", 200, res.getStatusCode());
        return res.as(PetAPIResponse.class);

    }
}
