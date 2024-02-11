package az.najafov.deforestationnews.service;

import az.najafov.deforestationnews.dto.CountryRequestDto;
import az.najafov.deforestationnews.dto.CountryResponseDto;
import az.najafov.deforestationnews.exception.EntityNotFoundException;
import az.najafov.deforestationnews.mapper.CountryMapper;
import az.najafov.deforestationnews.model.Country;
import az.najafov.deforestationnews.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    public void create(CountryRequestDto requestDto) {
        Country country = new Country();
        country.setName(requestDto.getName());
        countryRepository.save(country);
    }

    public CountryResponseDto getById(Long id) {
        return countryMapper.toResponseDto(countryRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(Country.class, "with ID : " + id)));
    }

    public List<CountryResponseDto> getAll() {
        return countryRepository.findAll().stream().map(countryMapper::toResponseDto).toList();
    }

    public void update(Long id, CountryRequestDto requestDto) {
        Country country = countryRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(Country.class, "with ID : " + id));
        country.setName(requestDto.getName());
        countryRepository.save(country);
    }

    public void delete(Long id) {
        countryRepository.deleteById(id);
    }

}
