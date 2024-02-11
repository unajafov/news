package az.najafov.deforestationnews.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityResponseDto {

    private Long id;
    private String name;
    private CountryResponseDto country;
    private boolean domestic;

}