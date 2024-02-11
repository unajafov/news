package az.najafov.deforestationnews.dto;

import lombok.Data;

@Data
public class RegionResponseDto {

    private Long id;
    private String name;
    private CountryResponseDto country;
    private CityResponseDto city;
    private DistrictResponseDto district;

}
