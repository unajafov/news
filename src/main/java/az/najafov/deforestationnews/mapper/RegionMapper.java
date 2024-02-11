package az.najafov.deforestationnews.mapper;

import az.najafov.deforestationnews.dto.RegionRequestDto;
import az.najafov.deforestationnews.dto.RegionResponseDto;
import az.najafov.deforestationnews.exception.EntityNotFoundException;
import az.najafov.deforestationnews.model.City;
import az.najafov.deforestationnews.model.Country;
import az.najafov.deforestationnews.model.District;
import az.najafov.deforestationnews.model.Region;
import az.najafov.deforestationnews.repository.CityRepository;
import az.najafov.deforestationnews.repository.CountryRepository;
import az.najafov.deforestationnews.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RegionMapper {

    private final CountryMapper countryMapper;
    private final CityMapper cityMapper;
    private final DistrictMapper districtMapper;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final DistrictRepository districtRepository;

    public RegionResponseDto toResponseDto(Region region) {
        if (Objects.isNull(region)) {
            return null;
        }
        RegionResponseDto responseDto = new RegionResponseDto();
        responseDto.setId(region.getId());
        responseDto.setName(region.getName());
        if (Objects.nonNull(region.getCountry())) {
            responseDto.setCountry(countryMapper.toResponseDto(region.getCountry()));
        }
        if (Objects.nonNull(region.getCity())) {
            responseDto.setCity(cityMapper.toResponseDto(region.getCity()));
        }
        if (Objects.nonNull(region.getDistrict())) {
            responseDto.setDistrict(districtMapper.toResponseDto(region.getDistrict()));
        }

        return responseDto;
    }

    public Region toEntity(RegionRequestDto requestDto) {
        Region region = new Region();
        region.setName(requestDto.getName());
        if (Objects.nonNull(requestDto.getCountryId())) {
            Country country = countryRepository.findById(requestDto.getCountryId()).orElseThrow(() ->
                    new EntityNotFoundException(Country.class, requestDto.getCountryId()));
            region.setCountry(country);
        }
        if (Objects.nonNull(requestDto.getCityId())) {
            City city = cityRepository.findById(requestDto.getCityId()).orElseThrow(() ->
                    new EntityNotFoundException(City.class, requestDto.getCityId()));
            region.setCity(city);
        }
        if (Objects.nonNull(requestDto.getDistrictId())) {
            District district = districtRepository.findById(requestDto.getDistrictId()).orElseThrow(() ->
                    new EntityNotFoundException(District.class, requestDto.getDistrictId()));
            region.setDistrict(district);
        }
        return region;
    }

}
