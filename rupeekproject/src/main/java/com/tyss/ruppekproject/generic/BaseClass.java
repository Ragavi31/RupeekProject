package com.tyss.ruppekproject.generic;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;

import org.testng.annotations.BeforeSuite;

import io.restassured.specification.RequestSpecification;

public class BaseClass {

	@BeforeSuite()
	public void preConfiguration() {
		baseURI = "http://13.126.80.194";
		port = 8080;
		

	}
}
