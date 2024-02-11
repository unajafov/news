package az.najafov.deforestationnews.service;

import az.najafov.deforestationnews.dto.DistrictRequestDto;
import az.najafov.deforestationnews.dto.DistrictResponseDto;
import az.najafov.deforestationnews.exception.EntityNotFoundException;
import az.najafov.deforestationnews.mapper.DistrictMapper;
import az.najafov.deforestationnews.model.City;
import az.najafov.deforestationnews.model.District;
import az.najafov.deforestationnews.repository.CityRepository;
import az.najafov.deforestationnews.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DistrictService {

    private final DistrictRepository districtRepository;
    private final DistrictMapper districtMapper;
    private final CityRepository cityRepository;

    public void create(DistrictRequestDto requestDto) {
        District district = new District();
        district.setName(requestDto.getName());
        Long cityId = requestDto.getCityId();

        City city = cityRepository.findById(cityId).orElseThrow(() ->
                new EntityNotFoundException(City.class, "with ID : " + cityId));
        district.setCity(city);

        districtRepository.save(district);
    }

    public DistrictResponseDto getById(Long id) {
        return districtMapper.toResponseDto(districtRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(District.class, "with ID : " + id)));
    }

    public List<DistrictResponseDto> getAll() {
        return districtRepository.findAll().stream().map(districtMapper::toResponseDto).toList();
    }

    public void update(Long id, DistrictRequestDto requestDto) {
        District district = districtRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(District.class, "with ID : " + id));
        district.setName(requestDto.getName());
        districtRepository.save(district);
    }

    public void delete(Long id) {
        districtRepository.deleteById(id);
    }

    public List<DistrictResponseDto> getAllByCity(Long cityId) {
        return districtRepository.findAllByCity(cityId).stream().map(districtMapper::toResponseDto)
                .collect(Collectors.toList());
    }

}
