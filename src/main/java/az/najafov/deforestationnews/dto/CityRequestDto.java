package az.najafov.deforestationnews.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class CityRequestDto {

    private String name;
    private boolean domestic;
    private Long countryId;

}