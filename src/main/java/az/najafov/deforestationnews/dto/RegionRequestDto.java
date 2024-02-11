package az.najafov.deforestationnews.dto;

import lombok.Data;

@Data
public class RegionRequestDto {

    private String name;
    private Long countryId;
    private Long cityId;
    private Long districtId;

}
