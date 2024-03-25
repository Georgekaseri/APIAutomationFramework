package org.example.tests.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import org.example.base.BaseTest;
import org.example.endpoints.APIConstants;
import org.example.payloads.Booking;
import org.example.payloads.BookingResponse;
import org.testng.annotations.Test;


import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

public class Integration extends BaseTest {


    String token;
    String bookingId;
    String bookingIdPojo;
    // Update the Booking with Token and Booking ID  - How to pass the Variables from one test to another
    // Auth - API Key
    // Cookies Based Auth side
    // OAuth 2.0 - Method how you can use the   OAuth 2.0
    // Delete
    // Get a Token Extract the Token


    // Create a Booking
    @Test(groups = "P0")
    public void testCreateBooking() throws JsonProcessingException {
        token = getToken();
        assertThat(token).isNotNull().isNotEmpty();


        requestSpecification.basePath(APIConstants.CREATE_UPDATE_BOOKING_URL);
        response = RestAssured.given().spec(requestSpecification)
                .when().body(payloadManager.createPayload()).post();
        validatableResponse = response.then().log().all();
        jsonPath = JsonPath.from(response.asString());
        validatableResponse.statusCode(200);

        // Direct Extraction from json Path
        bookingId = jsonPath.getString("bookingid");
        System.out.println("This is  Json Path Booking ID " + bookingId);

        // Booking Response Class
        BookingResponse bookingResponse = payloadManager.JsonToObject(response.asString());
        bookingIdPojo = bookingResponse.getBookingid().toString();
        System.out.println(bookingIdPojo);


        assertThat(bookingId).isNotNull().isNotEmpty();

    }


    // Update the Booking with Token and Booking ID
    @Test(groups = "P0", dependsOnMethods = {"testCreateBooking"})
    public void testCreateAndUpdateBooking() throws IOException {

        requestSpecification.basePath(APIConstants.CREATE_UPDATE_BOOKING_URL + "/" + bookingId);
        response = RestAssured.given().spec(requestSpecification).cookie("token", token)
                .when().body(payloadManager.updatedPayload()).put();
        validatableResponse = response.then().log().all();
//        validatableResponse.body("firstname", Matchers.is("George"));
//        validatableResponse.body("lastname", Matchers.is("Kaseri"));

        Booking bookingresp = payloadManager.JsonToObjectPUT(response.asString());
        assertThat(bookingresp.getFirstname()).isEqualTo("George").isNotNull();
        assertThat(bookingresp.getLastname()).isEqualTo("Kaseri").isNotNull();
        assertThat(bookingresp.getDepositpaid()).isNotNull();
        assertThat(bookingresp.getTotalprice()).isNotNull();
        assertThat(bookingresp.getBookingdates().getCheckin()).isNotEmpty();
        assertThat(bookingresp.getBookingdates().getCheckout()).isNotEmpty();


    }

    // Delete Booking ID
    @Test(groups = "P0", dependsOnMethods = {"testCreateAndUpdateBooking",})
    public void testDeleteCreatedBooking() {

        requestSpecification.basePath(APIConstants.CREATE_UPDATE_BOOKING_URL + "/" + bookingId).cookie("token", token);
        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification).auth().basic("admin", "password123")
                .when().delete().then().log().all();
        validatableResponse.statusCode(201);
    }

    @Test(groups = "P0", dependsOnMethods = {"testDeleteCreatedBooking"})
    public void testDeleteBookingByGet() {
        requestSpecification.basePath(APIConstants.CREATE_UPDATE_BOOKING_URL + "/" + bookingId);
        response = RestAssured.given().spec(requestSpecification)
                .when().get();
        validatableResponse = response.then().log().all();
        validatableResponse.statusCode(404);
    }

}