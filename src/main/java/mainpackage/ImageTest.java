package mainpackage;


import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import mainpackage.dto.PostImageResponse;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static mainpackage.dto.EndPoints.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalToObject;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.util.Base64;
import java.util.Properties;


public class ImageTest extends BaseTest {

    private static String imageBase64;
    static String uploadedImageId = null;
    static String uploadedImageDeleteHash = null;

    static String encodeFile;
    MultiPartSpecification base64MultiPartSpec;
    MultiPartSpecification multiPartSpecWithFile;
    static RequestSpecification requestSpecificationWithAuthAndMultipartImage;
    static RequestSpecification requestSpecificationToUpdate;

    private byte[] getFileContent() {
        byte[] beteArray= new byte[0];
        try  {
            beteArray= FileUtils.readFileToByteArray(new File(PATH_TO_IMAGE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return beteArray;
    }

    @BeforeEach
    void beforeTest() {

         byte[] byteArray = getFileContent();
         encodeFile = Base64.getEncoder().encodeToString(byteArray);

         base64MultiPartSpec = new MultiPartSpecBuilder(encodeFile)
                 .controlName("image")
                 .build();

         multiPartSpecWithFile =new MultiPartSpecBuilder(new File(PATH_TO_IMAGE))
                 .controlName("image")
                 .build();

         requestSpecificationWithAuthAndMultipartImage = new RequestSpecBuilder()
                 .addRequestSpecification(requestSpecificationWithAuth)
                 .addMultiPart(multiPartSpecWithFile)
                 .addFormParam("type", "image/jpeg")
                 .addFormParam("title", "hw_image")
                 .build();

         requestSpecificationToUpdate = new RequestSpecBuilder()
                 .addFormParam("title", "updated title")
                 .addFormParam("description", "updated description")
                 .build();

    }

    @Order(1)
    @Test
    void PositiveUploadFileTest() {

        PostImageResponse postImageResponse = given(requestSpecificationWithAuthAndMultipartImage,positiveResponseSpecification)
                .post(URL_UPLOAD)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class);

        uploadedImageId = postImageResponse.getData().getId();
        assertNotNull(uploadedImageId);
        uploadedImageDeleteHash = postImageResponse.getData().getDeletehash();
        assertNotNull(uploadedImageDeleteHash);
    }

    @Order(2)
    @Test
    void PositiveCheckUploadFileTest() {
        given(requestSpecificationWithAuth)
                .when()
                .get(URL_UPLOADIMAGEID, uploadedImageId);
    }

    @Order(3)
    @Test
    void PositiveCheckFavoriteLabelOnTest() {
        given(requestSpecificationWithAuth)
                .when()
                .post(URL_FAVORITED, uploadedImageId)
                .then()
                .body("data",equalTo("favorited"));
    }

    @Order(4)
    @Test
    void PositiveCheckFavoriteLabelOffTest() {
                given(requestSpecificationWithAuth)
                .when()
                .post(URL_FAVORITED, uploadedImageId)
                .then()
                .body("data",equalTo("unfavorited"));



    }

    @Order(5)
    @Test
    void NegativeCheckDeleteUploadedImageNoAuthUserTest() {
        given(requestSpecificationNoAuth,negativeResponceSpecification)
                .delete(URL_DELETEHASH, uploadedImageDeleteHash)
                .then()
                .body("data.method", equalTo("DELETE"))
                .body("data.error", equalTo("Authentication required"))
                .statusCode(401);
    }

    @Order(6)
    @Test
    void PositiveCheckDeleteUploadedImageAuthUserTest() {
        given(requestSpecificationWithAuth)
                .when()
                .delete(URL_UPLOADIMAGEID, uploadedImageId)
                .then()
                .body("data", equalTo(true));
    }

    @Order(7)
    @Test
    void NegativeUploadFileNullValueTest() {
        given(requestSpecificationWithAuth)
                .multiPart("image", "")
                .when()
                .post(URL_UPLOAD)
                .then()
                .spec(negativeResponceSpecification)
                .body("data.error", equalTo("Bad Request"));
    }

    @Order(8)
    @Test
    void PositiveUploadBase64FileTest() {
        PostImageResponse postImageBase64Response = given(requestSpecificationWithAuth)
                .multiPart(base64MultiPartSpec)
                .formParam("type", "base64")
                .when()
                .post(URL_UPLOAD)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class);

        uploadedImageId = postImageBase64Response.getData().getId();
        assertNotNull(uploadedImageId);
        uploadedImageDeleteHash = postImageBase64Response.getData().getDeletehash();
        assertNotNull(uploadedImageDeleteHash);

    }

    @Order(9)
    @Test
    void PositiveCheckUpdateImageInformationAuthUserTest() {
        given(requestSpecificationWithAuth)
                .spec(requestSpecificationToUpdate)
                .when()
                .post(URL_DELETEHASH, uploadedImageDeleteHash)
                .then()
                .spec(positiveResponseSpecification);

    }

    @Order(10)
    @Test
    void NegativeCheckUpdateImageInformationNoAuthUserTest() {
        given(requestSpecificationToUpdate,negativeResponceSpecification)
                .post(URL_DELETEHASH, uploadedImageDeleteHash)
                .then()
                .statusCode(401)
                .body("data.method", equalTo("POST"))
                .body("data.error", equalTo("Authentication required"));

    }
/*    @Order(11)
    @Test // тут как вариант можно эксепшен ловить, но но это будет ошибка на клиенте. а нам нужен сервер, в постмане такой тест кейс проходил
    void PositiveUploadFileNoAuthUserTest() {
        PostImageResponse postImageResponse= given(requestSpecificationNoAuth)
                .multiPart("image", new File(PATH_TO_IMAGE))
                .formParam("type", "image/jpeg")
                .formParam("title", "hw_image")
                .when()
                .post(UPLOAD)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class);

        uploadedImageId = postImageBResponse.getData().getId();
        assertNotNull(uploadedImageId);
        uploadedImageDeleteHash = postImageResponse.getData().getDeletehash();
        assertNotNull(uploadedImageDeleteHash);
    }*/
}
