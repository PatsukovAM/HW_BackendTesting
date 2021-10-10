package mainpackage;


import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalToObject;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.Base64;
import java.util.Properties;

import static io.restassured.RestAssured.given;


public class ImageTest extends BaseTest {
    static Properties imageTestProperties = new Properties();
    private static String imageBase64;
    static String uploadedImageId = null;
    static String uploadedImageDeleteHash = null;
    private final String PATH_TO_IMAGE = "src/main/resources/image.jpeg";
    static String encodeFile;

    private static void getImageProperties() {
        try (InputStream image = new FileInputStream("src/main/resources/imageBase64.properties")) {
            properties.load(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/*    @BeforeEach
    void beforeTest() {
        //getImageProperties();
        //imageBase64=imageTestProperties.getProperty("image");

        // byte[] byteArray = getFileContent();
        //  encodeFile = Base64.getEncoder().encodeToString(byteArray);
    }*/

    @Order(1)
    @Test
    void PositiveUploadFileTest() {
        Response imageLoadResponse = given()
                .headers("Authorization", token)
                .multiPart("image", new File(PATH_TO_IMAGE))
                .formParam("type", "image/jpeg")
                .formParam("title", "hw_image")
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek();


        assertThat(imageLoadResponse.jsonPath().get("success"), equalTo(true));
        assertThat(imageLoadResponse.jsonPath().get("status"), equalTo(200));

        uploadedImageId = imageLoadResponse.jsonPath().getString("data.id");
        assertNotNull(uploadedImageId);
        uploadedImageDeleteHash = imageLoadResponse.jsonPath().getString("data.deletehash");
        assertNotNull(uploadedImageDeleteHash);

    }


    @Order(2)
    @Test
    void PositiveCheckUploadFileTest() {
        given()
                .headers("Authorization", token)
                .when()
                .get("https://api.imgur.com/3/image/{uploadImageId}", uploadedImageId)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Order(3)
    @Test
    void PositiveCheckFavoriteLabelOnTest() {
        given()
                .headers("Authorization", token)
                .when()
                .post("https://api.imgur.com/3/image/{uploadedImageId}/favorite", uploadedImageId)
                .prettyPeek()
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("data", equalTo("favorited"));

    }

    @Order(4)
    @Test
    void PositiveCheckFavoriteLabelOffTest() {
        given()
                .headers("Authorization", token)
                .when()
                .post("https://api.imgur.com/3/image/{uploadedImageId}/favorite", uploadedImageId)
                .prettyPeek()
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("data", equalTo("unfavorited"));
        ;

    }

    @Order(5)
    @Test
    void NegativeCheckDeleteUploadedImageNoAuthUserTest() {
        given()
                .when()
                .delete("https://api.imgur.com/3/image/{uploadImageId}", uploadedImageId)
                .prettyPeek()
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("data.method", equalTo("DELETE"))
                .body("data.error", equalTo("Authentication required"));
    }

    @Order(6)
    @Test
    void PositiveCheckDeleteUploadedImageAuthUserTest() {
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/image/{uploadImageId}", uploadedImageId)
                .prettyPeek()
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("data", equalTo(true));
    }

    @Order(7)
    @Test
    void NegativeUploadFileNullValueTest() {
        given()
                .headers("Authorization", token)
                .multiPart("image", "")
                .formParam("type", "image/jpeg")
                .formParam("title", "hw_image")
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("data.method", equalTo("POST"))
                .body("data.error", equalTo("Bad Request"));
    }

    @Order(8)
    @Test
    void PositiveUploadFileNoAuthUserTest() {
        Response imageLoadResponse = given()
                .headers("Authorization", token)
                .multiPart("image", new File(PATH_TO_IMAGE))
                .formParam("type", "image/jpeg")
                .formParam("title", "hw_image")
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek();


        assertThat(imageLoadResponse.jsonPath().get("success"), equalTo(true));
        assertThat(imageLoadResponse.jsonPath().get("status"), equalTo(200));

        uploadedImageId = imageLoadResponse.jsonPath().getString("data.id");
        assertNotNull(uploadedImageId);
        uploadedImageDeleteHash = imageLoadResponse.jsonPath().getString("data.deletehash");
        assertNotNull(uploadedImageDeleteHash);
    }

    @Order(9)
    @Test
    void PositiveCheckUpdateImageInformationAuthUserTest() {
        given()
                .headers("Authorization", token)
                .formParam("title", "updated title")
                .formParam("description", "updated description")
                .when()
                .post("https://api.imgur.com/3/image/{imageDeleteHash}", uploadedImageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("data", equalTo(true));

    }

    @Order(10)
    @Test
    void NegativeCheckUpdateImageInformationNoAuthUserTest() {
        given()
                .formParam("title", "updated title")
                .formParam("description", "updated description")
                .when()
                .post("https://api.imgur.com/3/image/{imageDeleteHash}", uploadedImageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("data.method", equalTo("POST"))
                .body("data.error", equalTo("Authentication required"));

    }
}
