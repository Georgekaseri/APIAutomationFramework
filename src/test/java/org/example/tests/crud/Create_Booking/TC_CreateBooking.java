package org.example.tests.crud.Create_Booking;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.example.base.BaseTest;
import org.example.endpoints.APIConstants;
import org.example.payloads.Booking;
import org.example.payloads.BookingResponse;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import org.assertj.core.api.Assert;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.base.BaseTest.requestSpecification;

public class TC_CreateBooking extends BaseTest {
    String token;
    String bookingId;
    String bookingIdPojo;
    @Test
    public void testPositivePOSTReq () throws JsonProcessingException {
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
    @Test
    public void testNagativePost () throws IOException {
        requestSpecification.basePath(APIConstants.CREATE_UPDATE_BOOKING_URL);
        response = RestAssured.given().spec(requestSpecification)
                .when().body(payloadManager.createPayloadnegativeNoFirstName()).post();
        validatableResponse = response.then().log().all();
        jsonPath = JsonPath.from(response.asString());
//        Booking bookingresp1 = payloadManager.JsonToObjectPOST(response.asString());
//        assertThat(bookingresp1.getFirstname()).isEmpty();
//        validatableResponse.statusCode(500);






    }



    }

