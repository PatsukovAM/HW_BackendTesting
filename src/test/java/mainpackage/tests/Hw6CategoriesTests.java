package mainpackage.tests;


import mainpackage.db.dao.CategoriesMapper;
import mainpackage.enums.CategoryType;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import retrofit2.Retrofit;
import mainpackage.db.dao.ProductsMapper;
import mainpackage.dto.Category;
import mainpackage.service.CategoryService;
import mainpackage.service.ProductService;
import mainpackage.utils.DbUtils;
import mainpackage.utils.RetrofitUtils;


import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class Hw6CategoriesTests {

    static ProductsMapper productsMapper;
    static CategoriesMapper categoriesMapper;
    static Retrofit client;
    static ProductService productService;
    static CategoryService categoryService;

    @BeforeAll
    static void beforeAll() {
        client = RetrofitUtils.getRetrofit();
        productService = client.create(ProductService.class);
        categoryService = client.create(CategoryService.class);
        productsMapper = DbUtils.getProductsMapper();
        categoriesMapper=DbUtils.getCategoriesMapper();
    }

    @Test
    @Order(1)
    void PositiveGetExistCategoryTest() throws IOException {
        Response<Category> categoryResponse=categoryService.getCategory(CategoryType.FOOD.getId()).execute();
        assert categoryResponse.body() != null;
        assertThat(categoryResponse.body().getTitle(),equalTo(CategoryType.FOOD.getTitle()));
    }

    @Test
    @Order(2)
    void NegativeGetNoExistCategoryTest() throws IOException {
        Response<Category> categoryResponse=categoryService.getCategory(4).execute();
        assertThat(categoryResponse.body(),equalTo(null));
    }


    @Test
    @Order(3)
    void PosiveCreateNewCategoryTest() throws IOException {
        int categoryCounterBefore=DbUtils.countCategories(categoriesMapper);
        DbUtils.createNewCategory(categoriesMapper);
        int categoryCounterAfter=DbUtils.countCategories((categoriesMapper));
        Response<Category> categoryResponse=categoryService.getCategory(categoryCounterAfter).execute();
        assertNotNull(categoryResponse.body());
        assertThat((categoryCounterBefore+1), equalTo(categoryCounterAfter));
    }

}
