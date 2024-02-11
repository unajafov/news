package az.najafov.deforestationnews.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class DistrictRequestDto {

    private String name;
    private Long cityId;

}
