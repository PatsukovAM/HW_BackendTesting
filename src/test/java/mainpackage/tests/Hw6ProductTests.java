package mainpackage.tests;

import com.github.javafaker.Faker;
import mainpackage.db.dao.CategoriesMapper;
import mainpackage.db.model.Products;
import mainpackage.db.model.ProductsExample;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import retrofit2.Retrofit;
import mainpackage.db.dao.ProductsMapper;
import mainpackage.dto.Product;
import mainpackage.enums.CategoryType;
import mainpackage.dto.Category;
import mainpackage.service.CategoryService;
import mainpackage.service.ProductService;
import mainpackage.utils.DbUtils;
import mainpackage.utils.RetrofitUtils;


import java.io.IOException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class Hw6ProductTests {

    static int productId;
    Long longId;
    static ProductsMapper productsMapper;
    static CategoriesMapper categoriesMapper;
    static Retrofit client;
    static ProductService productService;
    static CategoryService categoryService;
    Faker faker = new Faker();
    Product product;

    @BeforeAll
    static void beforeAll() {
        client = RetrofitUtils.getRetrofit();
        productService = client.create(ProductService.class);
        categoryService = client.create(CategoryService.class);
        productsMapper = DbUtils.getProductsMapper();
        categoriesMapper = DbUtils.getCategoriesMapper();
    }

    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
    }

    @Test
    void postProductTest() throws IOException {
        Integer countProductsBefore = DbUtils.countProducts(productsMapper);
        Response<Product> response = productService.createProduct(product).execute();
        Integer countProductsAfter = DbUtils.countProducts(productsMapper);
        assertThat(countProductsAfter, equalTo(countProductsBefore + 1));
        assertThat(response.body().getTitle(), equalTo(product.getTitle()));
        assertThat(response.body().getPrice(), equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        productId = response.body().getId();
    }


    @Test
    void getCategoryByIdTest() throws IOException {
        Integer id = CategoryType.FOOD.getId();
        Response<Category> response = categoryService
                .getCategory(id)
                .execute();
        assertThat(response.body().getTitle(), equalTo(CategoryType.FOOD.getTitle()));
        assertThat(response.body().getId(), equalTo(id));
    }


    @Test
    @Order(1)
    void positiveDeleteByPrimaryKeyTest() throws IOException {
        Response<Product> postResponse = productService.createProduct(product).execute();

        productId = postResponse.body().getId();
        longId = new Long(productId);

        productsMapper.deleteByPrimaryKey(longId);

        Response<Product> deleteResponse = productService.getProduct(productId).execute();
        assertThat(deleteResponse.code(), equalTo(404));
    }

    @Test
    @Order(2)
    void positiveSelectByPrimaryKeyTest() throws IOException {
        Response<Product> postResponse = productService.createProduct(product).execute();

        productId = postResponse.body().getId();
        longId = new Long(productId);


        Products selectedProducts;
        selectedProducts = productsMapper.selectByPrimaryKey(longId);

        assertThat(selectedProducts.getId(), equalTo(longId));

    }

    @Test
    @Order(3)
    void positiveUpdateByPrimaryKeySelectiveTest() throws IOException {
        Response<Product> postResponse = productService.createProduct(product).execute();

        productId = postResponse.body().getId();
        product.setId(productId);

        Products updatePostProduct = new Products();
        updatePostProduct.setId(new Long(productId));
        updatePostProduct.setCategory_id(new Long(CategoryType.FOOD.getId()));
        updatePostProduct.setPrice(100);
        updatePostProduct.setTitle("updateTitle");

        productsMapper.updateByPrimaryKeySelective(updatePostProduct);

        Response<Product> updateResponse = productService.getProduct(productId).execute();
        assertThat(updateResponse.body().getTitle(), equalTo(updatePostProduct.getTitle()));
        assertThat(updateResponse.body().getPrice(), equalTo(updatePostProduct.getPrice()));
    }

    @Test
    @Order(4)
    void negativeSelectByPrimaryKeyTest() throws IOException {
        Response<Product> postResponse = productService.createProduct(product).execute();

        productId = postResponse.body().getId();
        longId = new Long(productId);

        productsMapper.deleteByPrimaryKey(longId);

        Products selectedProducts;
        selectedProducts = productsMapper.selectByPrimaryKey(longId);
        assertThat(selectedProducts, equalTo(null));
    }

    @Test
    @Order(5)
    void positiveInsertProductTest() throws IOException {

        Products insertProduct=new Products();

        insertProduct.setTitle(product.getTitle());
        insertProduct.setPrice(product.getPrice());
        insertProduct.setCategory_id(new Long(CategoryType.FOOD.getId()));

        int insertId=productsMapper.insert(insertProduct);
        //  не понятно почему не работает, подозреваю ProductsMapper.xml
        Response<Product> insertResponse = productService.getProduct(productId).execute();
        assertThat(insertResponse.body().getId(), equalTo(insertId));

    }

    @Test
    @Order(6)
    void NegativeDeleteByPrimaryKeyTest() throws IOException {
        Response<Product> postResponse = productService.createProduct(product).execute();

        productId = postResponse.body().getId();
        longId = new Long(productId);

        productsMapper.deleteByPrimaryKey(longId);

        Response<Product> deleteResponse = productService.getProduct(productId).execute();
        assertThat(deleteResponse.code(), equalTo(404));
        deleteResponse = productService.getProduct(productId).execute();
        assertThat(deleteResponse.code(), equalTo(404));
    }

    @Test
    @Order(7)
    void positiveInsertByExampleTest() {

        Products insertProduct=new Products();

        insertProduct.setTitle(product.getTitle());
        insertProduct.setPrice(product.getPrice());
        insertProduct.setCategory_id(new Long(CategoryType.FOOD.getId()));

        productsMapper.insert(insertProduct); //  не понятно почему не работает
    }

    @Test
    @Order(8)
    void positiveUpdateByPrimaryKeyTest() throws IOException {
        Response<Product> postResponse = productService.createProduct(product).execute();

        productId = postResponse.body().getId();
        product.setId(productId);

        Products updatePostProduct = new Products();
        updatePostProduct.setId(new Long(productId));
        updatePostProduct.setCategory_id(new Long(CategoryType.FOOD.getId()));
        updatePostProduct.setPrice(100);
        updatePostProduct.setTitle("updateTitle");

        productsMapper.updateByPrimaryKey(updatePostProduct);

        Response<Product> updateResponse = productService.getProduct(productId).execute();
        assertThat(updateResponse.body().getTitle(), equalTo(updatePostProduct.getTitle()));
        assertThat(updateResponse.body().getPrice(), equalTo(updatePostProduct.getPrice()));
    }

    @Test
    @Order(9)
    void positiveUpdateByExampleTest() throws IOException {
        Response<Product> postResponse = productService.createProduct(product).execute();

        longId = new Long(productId);


        Products updateProduct = new Products();
        updateProduct.setId(new Long(productId));
        updateProduct.setCategory_id(new Long(CategoryType.FOOD.getId()));
        updateProduct.setPrice(100);
        updateProduct.setTitle("updateTitle");


       // productsMapper.updateByExample(updateProduct,new ProductsExample()); //  не понятно почему не работает
    }
/*    @AfterEach
    void tearDown() throws IOException {
       Response<Product> response = productService.deleteProduct(productId).execute();
    }*/
}
