package az.najafov.deforestationnews.dto;

import lombok.Data;

@Data
public class CityRequestDto {

    private String name;
    private boolean domestic;
    private Long countryId;

}