package az.najafov.deforestationnews;

import az.najafov.deforestationnews.dto.CityRequestDto;
import az.najafov.deforestationnews.mapper.CityMapper;
import az.najafov.deforestationnews.model.City;
import az.najafov.deforestationnews.model.Country;
import az.najafov.deforestationnews.repository.CityRepository;
import az.najafov.deforestationnews.repository.CountryRepository;
import az.najafov.deforestationnews.service.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CityMapper cityMapper;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CityService cityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        CityRequestDto requestDto = new CityRequestDto();
        requestDto.setDomestic(true);
        requestDto.setName("TestCity");
        requestDto.setCountryId(1L);

        Country country = new Country();
        country.setId(1L);
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));

        cityService.create(requestDto);

        verify(cityRepository, times(1)).save(any());
    }

    @Test
    void testGetById() {
        Long cityId = 1L;
        City city = new City();
        when(cityRepository.findById(cityId)).thenReturn(Optional.of(city));

        cityService.getById(cityId);

        verify(cityMapper, times(1)).toResponseDto(city);
    }

    @Test
    void testGetAll() {
        List<City> cities = Arrays.asList(new City(), new City());
        when(cityRepository.findAll()).thenReturn(cities);

        cityService.getAll();

        verify(cityMapper, times(2)).toResponseDto(any());
    }

    @Test
    void testUpdate() {
        Long cityId = 1L;
        CityRequestDto requestDto = new CityRequestDto();
        requestDto.setName("UpdatedCity");

        City city = new City();
        when(cityRepository.findById(cityId)).thenReturn(Optional.of(city));

        cityService.update(cityId, requestDto);

        assertEquals(requestDto.getName(), city.getName());
        verify(cityRepository, times(1)).save(city);
    }

    @Test
    void testDelete() {
        Long cityId = 1L;
        doNothing().when(cityRepository).deleteById(cityId);

        cityService.delete(cityId);

        verify(cityRepository, times(1)).deleteById(cityId);
    }

    @Test
    void testGetAllByCountry() {
        Long countryId = 1L;
        List<City> cities = Arrays.asList(new City(), new City());
        when(cityRepository.findAllByCountry(countryId)).thenReturn(cities);

        cityService.getAllByCountry(countryId);

        verify(cityMapper, times(2)).toResponseDto(any());
    }

}
