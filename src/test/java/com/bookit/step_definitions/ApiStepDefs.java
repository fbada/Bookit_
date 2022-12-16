package com.bookit.step_definitions;

import com.bookit.pages.SelfPage;
import com.bookit.utilities.BookitUtils;
import com.bookit.utilities.ConfigurationReader;
import com.bookit.utilities.DB_Util;
import com.bookit.utilities.Environment;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.it.Ma;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.Map;

import static io.restassured.RestAssured.*;

public class ApiStepDefs {

    String token;
    Response response;
    String emailGlobal;


    @Given("I logged Bookit api as a {string}")
    public void i_logged_bookit_api_as_a(String role) {
        token = BookitUtils.generateTokenByRole ( role );
        System.out.println ( "token = " + token );

        Map<String, String> credentials = BookitUtils.returnCredentials ( role );
        emailGlobal = credentials.get ( "email" );

    }

    @When("I sent get request to {string} endpoint")
    public void i_sent_get_request_to_endpoint(String endpoint) {
        response = given ().accept ( ContentType.JSON )
                .header ( "Authorization", token )
                .when ().get ( Environment.BASE_URL + endpoint );

    }

    @Then("status code should be {int}")
    public void status_code_should_be(int expectedStatusCode) {

        System.out.println ( "response.statusCode() = " + response.statusCode () );
        Assert.assertEquals ( expectedStatusCode, response.statusCode () );


    }

    @Then("content type is {string}")
    public void content_type_is(String expectedContentType) {
        System.out.println ( "response.contentType() = " + response.contentType () );
        Assert.assertEquals ( expectedContentType, response.contentType () );

    }

    @Then("role is {string}")
    public void role_is(String expectedRole) {

        System.out.println ( "response.path(\"role\") = " + response.path ( "role" ) );
        System.out.println ( "response.jsonPath().getString(\"role\") = " + response.jsonPath ().getString ( "role" ) );

        Assert.assertEquals ( expectedRole, response.path ( "role" ) );

    }

    @Then("the information about current user from api and database should match")
    public void the_information_about_current_user_from_api_and_database_should_match() {

        // GET DATA FROM API
        JsonPath jsonPath = response.jsonPath ();
        /*
        {
            "id": 11312,
            "firstName": "Lissie",
            "lastName": "Finnis",
            "role": "student-team-leader"
}
         */
        // lastname
        String actualLastName = jsonPath.getString ( "lastName" );
        // firstname
        String actualFirstName = jsonPath.getString ( "firstName" );
        // role
        String actualRole = jsonPath.getString ( "role" );

        // GET DATA FROM DB

        String query = "select firstname,lastname,role from users where email='" + emailGlobal + "'";

        DB_Util.runQuery ( query );

        Map<String, String> dbMap = DB_Util.getRowMap ( 1 );

        System.out.println ( dbMap );


        String expectedFirstName = dbMap.get ( "firstname" );
        String expectedLastName = dbMap.get ( "lastname" );
        String expectedRole = dbMap.get ( "role" );

        // ASSERTIONS

        Assert.assertEquals ( expectedFirstName, actualFirstName );
        Assert.assertEquals ( expectedLastName, actualLastName );
        Assert.assertEquals ( expectedRole, actualRole );

    }

    @Then("UI,API and Database user information must be match")
    public void ui_api_and_database_user_information_must_be_match() {


        // GET DATA FROM API
        JsonPath jsonPath = response.jsonPath ();
        /*
        {
            "id": 11312,
            "firstName": "Lissie",
            "lastName": "Finnis",
            "role": "student-team-leader"
}
         */
        // lastname
        String actualLastName = jsonPath.getString ( "lastName" );
        // firstname
        String actualFirstName = jsonPath.getString ( "firstName" );
        // role
        String actualRole = jsonPath.getString ( "role" );

        // GET DATA FROM DB

        String query = "select firstname,lastname,role from users where email='" + emailGlobal + "'";

        DB_Util.runQuery ( query );

        Map<String, String> dbMap = DB_Util.getRowMap ( 1 );

        System.out.println ( dbMap );


        String expectedFirstName = dbMap.get ( "firstname" );
        String expectedLastName = dbMap.get ( "lastname" );
        String expectedRole = dbMap.get ( "role" );

        // API vs DB Assertion

        Assert.assertEquals ( expectedFirstName, actualFirstName );
        Assert.assertEquals ( expectedLastName, actualLastName );
        Assert.assertEquals ( expectedRole, actualRole );


        // GET DATA FROM UI
        // There is no only firstname and lastname.It is stored as fullname

        SelfPage selfPage = new SelfPage ();
        String actualNameFromUI = selfPage.name.getText ();
        String actualRoleFromUI = selfPage.role.getText ();


        // UI vs DB Assertion
        String expectedName = expectedFirstName + " " + expectedLastName;

        Assert.assertEquals ( expectedName, actualNameFromUI );
        Assert.assertEquals ( expectedRole, actualRoleFromUI );

        // UI vs API Assertion
        String actualNameFromAPI = actualFirstName + " " + actualLastName;
        Assert.assertEquals ( actualNameFromAPI, actualNameFromUI );
        Assert.assertEquals ( actualRole, actualRoleFromUI );

    }
}