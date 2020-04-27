package com.tyss.rupeekproject.testscripts;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.testng.annotations.Test;

import com.tyss.ruppekproject.generic.BaseClass;
import com.tyss.ruppekproject.generic.ISourceTarget;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * 
 * @author Ragavi
 *
 */
public class RestAssuredApiTesting extends BaseClass {

	String token;
	String phoneNo;
	JSONObject jsonObject;

	/**
	 * To get a jwttoken for authentication
	 */
	@Test()
	public void authenticationWithValidUNandPwd() {
		jsonObject = new JSONObject();
		jsonObject.put("username", "rupeek");
		jsonObject.put("password", "password");
		RequestSpecification requestSpec = given();
		requestSpec.contentType(ContentType.JSON);
		requestSpec.body(jsonObject.toJSONString());
		Response response = requestSpec.post(ISourceTarget.GET_TOKEN);
		token = response.jsonPath().get("token");
		response.getBody().prettyPrint();
		response.then().assertThat().statusCode(200).and().contentType(ContentType.JSON);
	}

	@Test(dependsOnMethods = { "authenticationWithValidUNandPwd" })
	public void authWithInvalidUN() {

		jsonObject.put("username", "rupeekPro");
		jsonObject.put("password", "password");
		given().contentType(ContentType.JSON).and().body(jsonObject.toJSONString()).when().post(ISourceTarget.GET_TOKEN)
				.then().assertThat().statusCode(401).and().contentType(ContentType.JSON);
	}

	@Test(dependsOnMethods = { "authenticationWithValidUNandPwd" })
	public void authWithInvalidPwd() {

		jsonObject.put("username", "rupeek");
		jsonObject.put("password", "pass");
		given().contentType(ContentType.JSON).and().body(jsonObject.toJSONString()).when().post(ISourceTarget.GET_TOKEN)
				.then().assertThat().statusCode(401).and().contentType(ContentType.JSON);

	}

	@Test(dependsOnMethods = { "authenticationWithValidUNandPwd" })
	public void authWithoutUN() {

		jsonObject.put("password", "password");
		given().contentType(ContentType.JSON).and().body(jsonObject.toJSONString()).when().post(ISourceTarget.GET_TOKEN)
				.then().assertThat().statusCode(401).and().contentType(ContentType.JSON);

	}

	@Test(dependsOnMethods = { "authenticationWithValidUNandPwd" })
	public void authWithoutPwd() {
		jsonObject.put("username", "rupeekPro");
		given().contentType(ContentType.JSON).and().body(jsonObject.toJSONString()).when().post(ISourceTarget.GET_TOKEN)
				.then().assertThat().statusCode(401).and().contentType(ContentType.JSON);

	}

	@Test(dependsOnMethods = { "authenticationWithValidUNandPwd" })
	public void authWithInvalidUNAndPwd() {
		jsonObject.put("username", "rupeekPro");
		jsonObject.put("password", "pass");
		given().contentType(ContentType.JSON).and().body(jsonObject.toJSONString()).when().post(ISourceTarget.GET_TOKEN)
				.then().assertThat().statusCode(401).and().contentType(ContentType.JSON);

	}

	/**
	 * To get all customer Information
	 */
	@Test(dependsOnMethods = { "authenticationWithValidUNandPwd" })
	public void getAllCustomers() {

		Response response = given().auth().oauth2(token).when().get(ISourceTarget.GET_ALL_RESOURCE);
		response.prettyPrint();
		response.then().assertThat().statusCode(200).and().contentType(ContentType.JSON);
		phoneNo = response.jsonPath().get("phone[0]");

	}

	@Test()
	public void getAllCustomersWithoutAuth() {

		Response response = when().get(ISourceTarget.GET_ALL_RESOURCE);
		response.prettyPrint();
		response.then().assertThat().statusCode(401).and().contentType(ContentType.ANY);
	}

	/**
	 * To get user based on phone number
	 */
	@Test(dependsOnMethods = { "authenticationWithValidUNandPwd", "getAllCustomers" })
	public void getUserWithPhoneNum() {

		Response response = given().auth().oauth2(token).pathParam("phone", phoneNo).when()
				.get(ISourceTarget.GET_SINGLE_RESOURCE);
		response.prettyPrint();
		response.then().assertThat().statusCode(200).and().contentType(ContentType.JSON);
		String body = response.getBody().asString();
		Assert.assertTrue(body.contains(phoneNo));
	}

	@Test(dependsOnMethods = { "authenticationWithValidUNandPwd" })
	public void getUserWithInvalidPathParam() {

		Response response = given().auth().oauth2(token).pathParam("phone", "BillGates").when()
				.get(ISourceTarget.GET_SINGLE_RESOURCE);
		response.prettyPrint();
		response.then().assertThat().statusCode(200);

	}

}
