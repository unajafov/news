package az.najafov.deforestationnews.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DistrictResponseDto {

    private Long id;
    private String name;
    private CityResponseDto city;

}
