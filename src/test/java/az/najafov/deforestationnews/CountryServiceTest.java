package az.najafov.deforestationnews;

import az.najafov.deforestationnews.dto.CountryRequestDto;
import az.najafov.deforestationnews.mapper.CountryMapper;
import az.najafov.deforestationnews.model.Country;
import az.najafov.deforestationnews.repository.CountryRepository;
import az.najafov.deforestationnews.service.CountryService;
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

public class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CountryMapper countryMapper;

    @InjectMocks
    private CountryService countryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        CountryRequestDto requestDto = new CountryRequestDto();
        requestDto.setName("TestCountry");

        countryService.create(requestDto);

        verify(countryRepository, times(1)).save(any());
    }

    @Test
    void testGetById() {
        Long countryId = 1L;
        Country country = new Country();
        when(countryRepository.findById(countryId)).thenReturn(Optional.of(country));

        countryService.getById(countryId);

        verify(countryMapper, times(1)).toResponseDto(country);
    }

    @Test
    void testGetAll() {
        List<Country> countries = Arrays.asList(new Country(), new Country());
        when(countryRepository.findAll()).thenReturn(countries);

        countryService.getAll();

        verify(countryMapper, times(2)).toResponseDto(any());
    }

    @Test
    void testUpdate() {
        Long countryId = 1L;
        CountryRequestDto requestDto = new CountryRequestDto();
        requestDto.setName("UpdatedCountry");

        Country country = new Country();
        when(countryRepository.findById(countryId)).thenReturn(Optional.of(country));

        countryService.update(countryId, requestDto);

        assertEquals(requestDto.getName(), country.getName());
        verify(countryRepository, times(1)).save(country);
    }

    @Test
    void testDelete() {
        Long countryId = 1L;
        doNothing().when(countryRepository).deleteById(countryId);

        countryService.delete(countryId);

        verify(countryRepository, times(1)).deleteById(countryId);
    }

}
