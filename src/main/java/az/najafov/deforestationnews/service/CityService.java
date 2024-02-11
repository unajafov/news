package az.najafov.deforestationnews.service;

import az.najafov.deforestationnews.dto.CityRequestDto;
import az.najafov.deforestationnews.dto.CityResponseDto;
import az.najafov.deforestationnews.exception.EntityNotFoundException;
import az.najafov.deforestationnews.mapper.CityMapper;
import az.najafov.deforestationnews.model.City;
import az.najafov.deforestationnews.model.Country;
import az.najafov.deforestationnews.repository.CityRepository;
import az.najafov.deforestationnews.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;
    private final CountryRepository countryRepository;

    public void create(CityRequestDto requestDto) {
        City city = new City();
        city.setDomestic(requestDto.isDomestic());
        city.setName(requestDto.getName());
        Country country = countryRepository.findById(requestDto.getCountryId()).orElseThrow(() ->
                new EntityNotFoundException(Country.class, "with ID : " + requestDto.getCountryId()));
        city.setCountry(country);
        cityRepository.save(city);
    }


    public CityResponseDto getById(Long id) {
        return cityMapper.toResponseDto(cityRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(City.class, "with ID : " + id)));
    }


    public List<CityResponseDto> getAll() {
        return cityRepository.findAll().stream().map(cityMapper::toResponseDto).toList();
    }


    public void update(Long id, CityRequestDto requestDto) {
        City city = cityRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(City.class, "with ID : " + id));
        city.setName(requestDto.getName());
        cityRepository.save(city);
    }


    public void delete(Long id) {
        cityRepository.deleteById(id);
    }

    public List<CityResponseDto> getAllByCountry(Long countryId) {
        return cityRepository.findAllByCountry(countryId).stream().map(cityMapper::toResponseDto)
                .collect(Collectors.toList());
    }

}