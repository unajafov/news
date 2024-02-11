package az.najafov.deforestationnews.mapper;

import az.najafov.deforestationnews.dto.DistrictResponseDto;
import az.najafov.deforestationnews.model.District;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class DistrictMapper {

    private final CityMapper cityMapper;

    public DistrictResponseDto toResponseDto(District district) {
        if (Objects.isNull(district)) {
            return null;
        }
        DistrictResponseDto districtResponseDto = new DistrictResponseDto();
        districtResponseDto.setId(district.getId());
        districtResponseDto.setName(district.getName());
        if (Objects.nonNull(district.getCity())) {
            districtResponseDto.setCity(cityMapper.toResponseDto(district.getCity()));
        }
        return districtResponseDto;
    }

}
