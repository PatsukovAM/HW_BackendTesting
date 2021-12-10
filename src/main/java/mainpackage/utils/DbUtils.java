package mainpackage.utils;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import mainpackage.db.dao.CategoriesMapper;
import mainpackage.db.dao.ProductsMapper;
import mainpackage.db.model.Categories;
import mainpackage.db.model.CategoriesExample;
import mainpackage.db.model.ProductsExample;


import java.io.IOException;


@UtilityClass
public class DbUtils {

    private static String resoure="mybatisConfig.xml";
    static Faker faker=new Faker();

    private static SqlSession getSqlSession() throws IOException{
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream(resoure));
        return sqlSessionFactory.openSession(true);
    }

    @SneakyThrows
    public static CategoriesMapper getCategoriesMapper() {return getSqlSession().getMapper(CategoriesMapper.class);}

    @SneakyThrows
    public static ProductsMapper getProductsMapper() {return getSqlSession().getMapper(ProductsMapper.class);}

    @SneakyThrows
    public static void createNewCategory(CategoriesMapper categoriesMapper){
        Categories newCategory = new Categories();
        newCategory.setTitle(faker.book().title());
        categoriesMapper.insert(newCategory);
    }

    public static void createNewCategoryNullTitle(CategoriesMapper categoriesMapper){
        Categories newCategory = new Categories();
        newCategory.setTitle(null);
        categoriesMapper.insert(newCategory);
    }


    public static Integer countCategories(CategoriesMapper categoriesMapper) {
        long categoriesCount = categoriesMapper.countByExample(new CategoriesExample());
        return Math.toIntExact(categoriesCount);
    }

    public static Integer countProducts(ProductsMapper productsMapper) {
        long productsCount = productsMapper.countByExample(new ProductsExample());
        return Math.toIntExact(productsCount);
    }
}




