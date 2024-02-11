package az.najafov.deforestationnews.dto;

import lombok.Data;

@Data
public class UserResponseDto {

    private Long id;
    private String userName;
    private RegionResponseDto region;

}
