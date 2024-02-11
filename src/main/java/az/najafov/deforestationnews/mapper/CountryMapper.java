package az.najafov.deforestationnews.mapper;

import az.najafov.deforestationnews.dto.CountryResponseDto;
import az.najafov.deforestationnews.model.Country;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CountryMapper {

    public CountryResponseDto toResponseDto(Country country) {
        if (Objects.isNull(country)) {
            return null;
        }
        CountryResponseDto countryResponseDto = new CountryResponseDto();
        countryResponseDto.setId(country.getId());
        countryResponseDto.setName(country.getName());
        return countryResponseDto;
    }

}
