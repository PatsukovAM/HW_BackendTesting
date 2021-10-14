package mainpackage;

import io.restassured.response.Response;
import mainpackage.dto.AccountResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import static io.restassured.RestAssured.given;
import static mainpackage.dto.EndPoints.URL_ACCAUNT;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AccountTests extends BaseTest {

    @Order(4)
    @Test
    void getAccountInfoTest() {
        AccountResponse accountResponse =given(requestSpecificationWithAuth,positiveResponseSpecification)
                .get(URL_ACCAUNT, username)
                .then()
                .extract()
                .body()
                .as(AccountResponse.class);
    }

    @Order(2)
    @Test
    void getAccountInfoWithLoggingTest() {
        AccountResponse accountResponse =given(requestSpecificationWithAuth)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get(URL_ACCAUNT, username)
                .then()
                .spec(positiveResponseSpecification)
                .extract()
                .body()
                .as(AccountResponse.class);
    }

    @Order(3)
    @Test
    void getAccountInfoWithAsssertionsInGivaenTest() {
        AccountResponse accountResponse =given(requestSpecificationWithAuth)
                .log()
                .method()
                .log()
                .uri()
                .expect()
                .statusCode(200)
                .body("data.url", equalTo(username))
                .body("success", equalTo(true))
                .body("status", equalTo(200))
                .contentType("application/json")
                .when()
                .get(URL_ACCAUNT, username)
                .then()
                .extract()
                .body()
                .as(AccountResponse.class);
    }

    @Order(1)
    @Test
    void getAccountInfoWithAsssertionsAfterTest() {
        AccountResponse accountResponse= given(requestSpecificationWithAuth)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get(URL_ACCAUNT, username)
                .then()
                .extract()
                .body()
                .as(AccountResponse.class);

        assertThat(accountResponse.getData().getUrl(),equalTo(username));
        assertThat(accountResponse.getStatus(),equalTo(200));
        assertThat(accountResponse.getSuccess(),equalTo(true));

    }
}
