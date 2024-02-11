package az.najafov.deforestationnews.service;

import az.najafov.deforestationnews.dto.RegionRequestDto;
import az.najafov.deforestationnews.dto.RegionResponseDto;
import az.najafov.deforestationnews.exception.EntityNotFoundException;
import az.najafov.deforestationnews.mapper.RegionMapper;
import az.najafov.deforestationnews.model.City;
import az.najafov.deforestationnews.model.Country;
import az.najafov.deforestationnews.model.District;
import az.najafov.deforestationnews.model.Region;
import az.najafov.deforestationnews.repository.CityRepository;
import az.najafov.deforestationnews.repository.CountryRepository;
import az.najafov.deforestationnews.repository.DistrictRepository;
import az.najafov.deforestationnews.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RegionService {

    private final RegionRepository regionRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final DistrictRepository districtRepository;
    private final RegionMapper regionMapper;

    public void create(RegionRequestDto requestDto) {
        Region region = regionMapper.toEntity(requestDto);
        regionRepository.save(region);
    }

    public void update(Long id, RegionRequestDto requestDto) {
        Region region = regionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Region.class, id));
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
        regionRepository.save(region);
    }

    public RegionResponseDto getById(Long id) {
        Region region = regionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Region.class, id));
        return regionMapper.toResponseDto(region);
    }

    public void delete(Long id) {
        Region region = regionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Region.class, id));
        regionRepository.delete(region);
    }

    public List<RegionResponseDto> getAll() {
        return regionRepository.findAll().stream().map(regionMapper::toResponseDto).collect(Collectors.toList());
    }

    public List<RegionResponseDto> getAllByCountry(Long countryId) {
        List<Region> regions = regionRepository.findAllByCountry(countryId);
        return regions.stream().map(regionMapper::toResponseDto).collect(Collectors.toList());
    }

    public List<RegionResponseDto> getAllByCity(Long cityId) {
        List<Region> regions = regionRepository.findAllByCity(cityId);
        return regions.stream().map(regionMapper::toResponseDto).collect(Collectors.toList());
    }

    public List<RegionResponseDto> getAllByDistrict(Long districtId) {
        List<Region> regions = regionRepository.findAllByDistrict(districtId);
        return regions.stream().map(regionMapper::toResponseDto).collect(Collectors.toList());
    }
}
