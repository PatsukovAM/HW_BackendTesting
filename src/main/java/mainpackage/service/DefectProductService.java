package mainpackage.service;

import mainpackage.dto.DefectProduct;
import mainpackage.dto.Product;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DefectProductService {
    @POST("products")
    Call<DefectProduct> createDefectProduct(@Body DefectProduct defectProduct);
}
