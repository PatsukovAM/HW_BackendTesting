package mainpackage;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static mainpackage.dto.EndPoints.PATH_TO_PROPERTIES;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class BaseTest {
    static Properties properties = new Properties();
    static String token;
    static String username;
    static ResponseSpecification positiveResponseSpecification;
    static ResponseSpecification negativeResponceSpecification;
    static RequestSpecification requestSpecificationWithAuth;
    static RequestSpecification requestSpecificationNoAuth;

    @BeforeAll
    static void beforeAll() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured());
        getProperties();
        token = properties.getProperty("token");
        username = properties.getProperty("username");

        positiveResponseSpecification = new ResponseSpecBuilder()
                .expectBody("success", is(true))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();

        //RestAssured.responseSpecification=positiveResponseSpecification; //пока не закомментировал эту строку негативное переопределение ответа поумолчанию не работало

        negativeResponceSpecification =new ResponseSpecBuilder()
                .expectBody("success", is(false))
                .build();

        requestSpecificationWithAuth =new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .build();

        requestSpecificationNoAuth =new RequestSpecBuilder()
                .build();
    }

    private static void getProperties() {
        try (InputStream output = new FileInputStream(PATH_TO_PROPERTIES)) {
            properties.load(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
