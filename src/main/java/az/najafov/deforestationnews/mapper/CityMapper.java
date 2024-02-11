package az.najafov.deforestationnews.mapper;

import az.najafov.deforestationnews.dto.CityResponseDto;
import az.najafov.deforestationnews.model.City;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CityMapper {

    private final CountryMapper countryMapper;

    public CityResponseDto toResponseDto(City city) {
        if (Objects.isNull(city)) {
            return null;
        }
        CityResponseDto cityResponseDto = new CityResponseDto();
        cityResponseDto.setId(city.getId());
        cityResponseDto.setName(city.getName());
        cityResponseDto.setDomestic(city.isDomestic());
        if (Objects.nonNull(city.getCountry())) {
            cityResponseDto.setCountry(countryMapper.toResponseDto(city.getCountry()));
        }
        return cityResponseDto;
    }

}
