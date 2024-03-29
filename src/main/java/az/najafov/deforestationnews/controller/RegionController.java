package az.najafov.deforestationnews.controller;

import az.najafov.deforestationnews.dto.RegionRequestDto;
import az.najafov.deforestationnews.dto.RegionResponseDto;
import az.najafov.deforestationnews.service.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Regions", description = "API operations for managing geographical regions")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/regions")
public class RegionController {

    private final RegionService regionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new region", description = "Creates a new geographical region with " +
            "the provided details.")
    public void create(@RequestBody RegionRequestDto requestDto) {
        regionService.create(requestDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a region", description = "Updates an existing geographical region with " +
            "the specified ID.")
    public void update(@PathVariable Long id, RegionRequestDto requestDto) {
        regionService.update(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete region by ID", description = "Deletes a specific geographical region based on " +
            "its unique identifier.")
    public void delete(@PathVariable Long id) {
        regionService.delete(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get region by ID", description = "Retrieves a specific geographical region based " +
            "on its unique identifier.")
    public RegionResponseDto getById(@PathVariable Long id) {
        return regionService.getById(id);
    }

    @GetMapping
    @Operation(summary = "Get all regions", description = "Retrieves a list of all available geographical regions.")
    public List<RegionResponseDto> getAll() {
        return regionService.getAll();
    }

    @GetMapping("/countries/{id}")
    @Operation(summary = "Get regions by country", description = "Retrieves geographical regions associated with " +
            "the specified country.")
    public List<RegionResponseDto> getAllByCountry(@PathVariable Long id) {
        return regionService.getAllByCountry(id);
    }

    @GetMapping("/cities/{id}")
    @Operation(summary = "Get regions by city", description = "Retrieves geographical regions associated with " +
            "the specified city.")
    public List<RegionResponseDto> getAllByCity(@PathVariable Long id) {
        return regionService.getAllByCity(id);
    }

    @GetMapping("/districts/{id}")
    @Operation(summary = "Get regions by district", description = "Retrieves geographical regions associated " +
            "with the specified district.")
    public List<RegionResponseDto> getAllByDistrict(@PathVariable Long id) {
        return regionService.getAllByDistrict(id);
    }

}
