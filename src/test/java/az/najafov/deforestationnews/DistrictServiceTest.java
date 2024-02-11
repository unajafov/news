package az.najafov.deforestationnews;

import az.najafov.deforestationnews.dto.DistrictRequestDto;
import az.najafov.deforestationnews.mapper.DistrictMapper;
import az.najafov.deforestationnews.model.City;
import az.najafov.deforestationnews.model.District;
import az.najafov.deforestationnews.repository.CityRepository;
import az.najafov.deforestationnews.repository.DistrictRepository;
import az.najafov.deforestationnews.service.DistrictService;
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

public class DistrictServiceTest {

    @Mock
    private DistrictRepository districtRepository;

    @Mock
    private DistrictMapper districtMapper;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private DistrictService districtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        DistrictRequestDto requestDto = new DistrictRequestDto();
        requestDto.setName("TestDistrict");
        requestDto.setCityId(1L);

        City city = new City();
        city.setId(1L);
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

        districtService.create(requestDto);

        verify(districtRepository, times(1)).save(any());
    }

    @Test
    void testGetById() {
        Long districtId = 1L;
        District district = new District();
        when(districtRepository.findById(districtId)).thenReturn(Optional.of(district));

        districtService.getById(districtId);

        verify(districtMapper, times(1)).toResponseDto(district);
    }

    @Test
    void testGetAll() {
        List<District> districts = Arrays.asList(new District(), new District());
        when(districtRepository.findAll()).thenReturn(districts);

        districtService.getAll();

        verify(districtMapper, times(2)).toResponseDto(any());
    }

    @Test
    void testUpdate() {
        Long districtId = 1L;
        DistrictRequestDto requestDto = new DistrictRequestDto();
        requestDto.setName("UpdatedDistrict");

        District district = new District();
        when(districtRepository.findById(districtId)).thenReturn(Optional.of(district));

        districtService.update(districtId, requestDto);

        assertEquals(requestDto.getName(), district.getName());
        verify(districtRepository, times(1)).save(district);
    }

    @Test
    void testDelete() {
        Long districtId = 1L;
        doNothing().when(districtRepository).deleteById(districtId);

        districtService.delete(districtId);

        verify(districtRepository, times(1)).deleteById(districtId);
    }

    @Test
    void testGetAllByCity() {
        Long cityId = 1L;
        List<District> districts = Arrays.asList(new District(), new District());
        when(districtRepository.findAllByCity(cityId)).thenReturn(districts);

        districtService.getAllByCity(cityId);

        verify(districtMapper, times(2)).toResponseDto(any());
    }

}
