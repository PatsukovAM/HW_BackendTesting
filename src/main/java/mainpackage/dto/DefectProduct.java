package mainpackage.dto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@ToString
public class DefectProduct {
    Integer id;
    String title;
    Long price;
    String categoryTitile;
}
