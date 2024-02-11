package az.najafov.deforestationnews;

import az.najafov.deforestationnews.dto.RegionRequestDto;
import az.najafov.deforestationnews.dto.RegionResponseDto;
import az.najafov.deforestationnews.mapper.RegionMapper;
import az.najafov.deforestationnews.model.Region;
import az.najafov.deforestationnews.repository.RegionRepository;
import az.najafov.deforestationnews.service.RegionService;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RegionServiceTest {

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private RegionMapper regionMapper;

    @InjectMocks
    private RegionService regionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        RegionRequestDto requestDto = new RegionRequestDto();
        Region region = new Region();

        when(regionMapper.toEntity(requestDto)).thenReturn(region);

        regionService.create(requestDto);

        verify(regionRepository, times(1)).save(region);
    }

    @Test
    void testUpdate() {
        Long regionId = 1L;
        RegionRequestDto requestDto = new RegionRequestDto();
        requestDto.setName("UpdatedRegion");

        Region region = new Region();
        when(regionRepository.findById(regionId)).thenReturn(Optional.of(region));

        regionService.update(regionId, requestDto);

        assertEquals(requestDto.getName(), region.getName());
        verify(regionRepository, times(1)).save(region);
    }

    @Test
    void testGetById() {
        Long regionId = 1L;
        Region region = new Region();
        when(regionRepository.findById(regionId)).thenReturn(Optional.of(region));
        RegionResponseDto responseDto = new RegionResponseDto();
        when(regionMapper.toResponseDto(region)).thenReturn(responseDto);

        RegionResponseDto result = regionService.getById(regionId);

        assertEquals(responseDto, result);
    }

    @Test
    void testDelete() {
        Long regionId = 1L;
        Region region = new Region();
        when(regionRepository.findById(regionId)).thenReturn(Optional.of(region));

        regionService.delete(regionId);

        verify(regionRepository, times(1)).delete(region);
    }

    @Test
    void testGetAll() {
        List<Region> regions = Arrays.asList(new Region(), new Region());
        when(regionRepository.findAll()).thenReturn(regions);

        regionService.getAll();

        verify(regionMapper, times(2)).toResponseDto(any());
    }

    @Test
    void testGetAllByCountry() {
        Long countryId = 1L;
        List<Region> regions = Arrays.asList(new Region(), new Region());
        when(regionRepository.findAllByCountry(countryId)).thenReturn(regions);

        regionService.getAllByCountry(countryId);

        verify(regionMapper, times(2)).toResponseDto(any());
    }

    @Test
    void testGetAllByCity() {
        Long cityId = 1L;
        List<Region> regions = Arrays.asList(new Region(), new Region());
        when(regionRepository.findAllByCity(cityId)).thenReturn(regions);

        regionService.getAllByCity(cityId);

        verify(regionMapper, times(2)).toResponseDto(any());
    }

    @Test
    void testGetAllByDistrict() {
        Long districtId = 1L;
        List<Region> regions = Arrays.asList(new Region(), new Region());
        when(regionRepository.findAllByDistrict(districtId)).thenReturn(regions);

        regionService.getAllByDistrict(districtId);

        verify(regionMapper, times(2)).toResponseDto(any());
    }

}
