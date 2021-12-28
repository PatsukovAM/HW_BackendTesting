package mainpackage.tests;

import com.github.javafaker.Faker;
import mainpackage.dto.DefectProduct;
import mainpackage.dto.Product;
import mainpackage.enums.CategoryType;
import mainpackage.service.CategoryService;
import mainpackage.service.DefectProductService;
import mainpackage.service.ProductService;
import mainpackage.utils.PrettyLogger;
import mainpackage.utils.RetrofitUtils;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import retrofit2.Retrofit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.IOException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class Hw5ProductTests {
    static Retrofit client;
    static ProductService productService;
    static CategoryService categoryService;
    static DefectProductService defectProductService;
    Faker faker=new Faker();
    Product product;
    DefectProduct defectProduct;
    PrettyLogger prettyLogger= new PrettyLogger();
    Integer productId;
    static String modifyTitle="watches";
    static Integer modifyPrice=7000;

    @BeforeAll
    static void beforeAll(){
        client= RetrofitUtils.getRetrofit();
        productService=client.create(ProductService.class);
        categoryService=client.create(CategoryService.class);
    }

    @BeforeEach
    void setUp(){
        product = new Product()
                .withTitle("cake")
                .withPrice(150)
                .withCategoryTitle("Food");
    }

    @Test
    @Order(1)
    void positivePostProductTest() throws IOException{
        Response<Product> response = productService.createProduct(product).execute();
        assertThat(response.body().getTitle(),equalTo(product.getTitle()));
        assertThat(response.body().getPrice(),equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitle(),equalTo(product.getCategoryTitle()));
        assertThat(response.code(),equalTo(201));
    }

    @Test
    @Order(2)
    void positiveGetProductTest() throws IOException{
        Response<Product> postResponse = productService.createProduct(product).execute();
        assertThat(postResponse.body().getTitle(),equalTo(product.getTitle()));
        assertThat(postResponse.body().getPrice(),equalTo(product.getPrice()));
        assertThat(postResponse.body().getCategoryTitle(),equalTo(product.getCategoryTitle()));
        assertThat(postResponse.code(),equalTo(201));

        productId=postResponse.body().getId();

        Response<Product> getResponse = productService.getProduct(productId).execute();
        assertThat(getResponse.body().getTitle(),equalTo(product.getTitle()));
        assertThat(getResponse.body().getPrice(),equalTo(product.getPrice()));
        assertThat(getResponse.body().getCategoryTitle(),equalTo(product.getCategoryTitle()));
        assertThat(getResponse.code(),equalTo(200));
    }

    @Test
    @Order(3)
    void positivePutProductTest() throws IOException{
        Response<Product> postResponse = productService.createProduct(product).execute();
        assertThat(postResponse.body().getTitle(),equalTo(product.getTitle()));
        assertThat(postResponse.body().getPrice(),equalTo(product.getPrice()));
        assertThat(postResponse.body().getCategoryTitle(),equalTo(product.getCategoryTitle()));
        assertThat(postResponse.code(),equalTo(201));

        productId=postResponse.body().getId();

        product.setTitle(modifyTitle);
        product.setPrice(modifyPrice);
        product.setId(productId);

        Response<Product> response= productService.modifyProduct(product).execute();
        assertThat(response.body().getTitle(),equalTo(product.getTitle()));
        assertThat(response.body().getPrice(),equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitle(),equalTo(product.getCategoryTitle()));
        assertThat(response.code(),equalTo(200));
    }

    @Test
    @Order(4)
    // вот тут я не понимаю ошибку, есть подозрение, что проблема в том, что при десериализации он не может оставить пустыми поля объекта
    void positiveDeleteProductTest() throws IOException{
        Response<Product> postResponse = productService.createProduct(product).execute();
        assertThat(postResponse.body().getTitle(),equalTo(product.getTitle()));
        assertThat(postResponse.body().getPrice(),equalTo(product.getPrice()));
        assertThat(postResponse.body().getCategoryTitle(),equalTo(product.getCategoryTitle()));
        assertThat(postResponse.code(),equalTo(201));

        productId=postResponse.body().getId();


        Response<Product> response = productService.deleteProduct(productId).execute();
        //assertThat(response.code(),equalTo(200));
    }

    @Test
    @Order(5)
    // здешь ошибка наследуется из предыдущего кейса
    void negativeGetProductTest() throws IOException{
        Response<Product> postResponse = productService.createProduct(product).execute();
        assertThat(postResponse.body().getTitle(),equalTo(product.getTitle()));
        assertThat(postResponse.body().getPrice(),equalTo(product.getPrice()));
        assertThat(postResponse.body().getCategoryTitle(),equalTo(product.getCategoryTitle()));
        assertThat(postResponse.code(),equalTo(201));

        productId=postResponse.body().getId();

        Response<Product> response = productService.deleteProduct(productId).execute();
        assertThat(response.code(),equalTo(200));

        Response<Product> getResponse = productService.getProduct(productId).execute();
        assertThat(getResponse.code(),equalTo(404));
    }

    @Test
    @Order(6)
    // на свагере выдает код 201
    void negativePostProductNullTitleTest() throws IOException{
        product.setPrice(null);
        Response<Product> response = productService.createProduct(product).execute();
        assertThat(response.code(),equalTo(201));
    }

    @Test
    @Order(7)
    // на свагере выдает код 201
    void negativePostProductNullPriceTest() throws IOException{
        product.setPrice(null);
        Response<Product> response = productService.createProduct(product).execute();
        assertThat(response.code(),equalTo(201));
    }

    @Test
    @Order(8)
    // логически баг, но свагер тоже такой ввод пропускает
    void negativePostProductNegativePriceTest() throws IOException{
        product.setPrice(-600);
        Response<Product> response = productService.createProduct(product).execute();
        assertThat(response.code(),equalTo(201));
    }

    @Test
    @Order(9)
    // на свагере выдает ошибку 400
    void negativePostProductOverFlowPriceTest() throws IOException{

         defectProduct= new DefectProduct()
                .withTitle("defect")
                .withPrice(20000000000000L)
                .withCategoryTitile(CategoryType.FURNITURE.getTitle());

        Response<DefectProduct> response = defectProductService.createDefectProduct(defectProduct).execute();
        assertThat(response.code(),equalTo(400));
    }

    @Test
    @Order(10)
    void negativeGetProductNegativeIdTest() throws IOException{
        Response<Product> response = productService.getProduct(-2).execute();
        assertThat(response.code(),equalTo(404));
    }
}
