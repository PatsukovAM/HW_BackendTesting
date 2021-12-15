package mainpackage.tests;

import com.github.javafaker.Faker;
import mainpackage.dto.DefectProduct;
import mainpackage.dto.Product;
import mainpackage.enums.CategoryType;
import mainpackage.service.CategoryService;
import mainpackage.service.ProductService;
import mainpackage.utils.PrettyLogger;
import mainpackage.utils.RetrofitUtils;
import okhttp3.Request;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import retrofit2.Retrofit;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.IOException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class ProductTests {
    static Retrofit client;
    static ProductService productService;
    static CategoryService categoryService;
    Faker faker=new Faker();
    Product product;
    PrettyLogger prettyLogger= new PrettyLogger();
    static Integer productId;
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
                .withTitle("Smartwatches")
                .withPrice(15000)
                .withCategoryTitile(CategoryType.ELECTRONICS.getTitle());
    }

    @Order(1)
    @Test
    void positivePostProductTest() throws IOException{
        Response<Product> response = productService.createProduct(product).execute();
        assertThat(response.body().getTitle(),equalTo(product.getTitle()));
        assertThat(response.body().getPrice(),equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitile(),equalTo(product.getCategoryTitile()));
        assertThat(response.code(),equalTo(200));

        productId=response.body().getId();
    }

    @Order(2)
    @Test
    void positiveGetProductTest() throws IOException{
        Response<Product> response = productService.getProduct(productId).execute();
        assertThat(response.body().getTitle(),equalTo(product.getTitle()));
        assertThat(response.body().getPrice(),equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitile(),equalTo(product.getCategoryTitile()));
        assertThat(response.code(),equalTo(200));
    }

    @Order(3)
    @Test
    void positivePutProductTest() throws IOException{

        product.setTitle(modifyTitle);
        product.setPrice(modifyPrice);

        Response<Product> response= productService.modifyProduct(product).execute();
        assertThat(response.body().getTitle(),equalTo(product.getTitle()));
        assertThat(response.body().getPrice(),equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitile(),equalTo(product.getCategoryTitile()));
        assertThat(response.code(),equalTo(200));
    }

    @Order(4)
    @Test
    void positiveDeleteProductTest() throws IOException{
        Response<Product> response = productService.deleteProduct(productId).execute();
        assertThat(response.code(),equalTo(200));
    }

    @Order(5)
    @Test
    void negativeGetProductTest() throws IOException{
        Response<Product> response = productService.getProduct(productId).execute();
        assertThat(response.code(),equalTo(404));
    }

    @Order(6)
    @Test
    void negativePostProductNullTitleTest() throws IOException{
        product.setPrice(null);
        Response<Product> response = productService.createProduct(product).execute();
        assertThat(response.code(),equalTo(400));
    }

    @Order(7)
    @Test
    void negativePostProductNullPriceTest() throws IOException{
        product.setPrice(null);
        Response<Product> response = productService.createProduct(product).execute();
        assertThat(response.code(),equalTo(400));
    }

    @Order(8)
    @Test
    void negativePostProductNegativePriceTest() throws IOException{
        product.setPrice(-600);
        Response<Product> response = productService.createProduct(product).execute();
        assertThat(response.code(),equalTo(400));
    }

    @Order(9)
    @Test
    void negativePostProductOverFlowPriceTest() throws IOException{

        DefectProduct defectProduct= new DefectProduct()
                .withTitle("defect")
                .withPrice(20000000000000L)
                .withCategoryTitile(CategoryType.FURNITURE.getTitle());

        Response<Product> response = productService.createProduct(product).execute();
        assertThat(response.code(),equalTo(400));
    }

    @Order(10)
    @Test
    void negativeGetProductNegativeIdTest() throws IOException{
        Response<Product> response = productService.getProduct(-2).execute();
        assertThat(response.code(),equalTo(404));
    }
}
