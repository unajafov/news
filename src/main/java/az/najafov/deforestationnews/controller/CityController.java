package az.najafov.deforestationnews.controller;

import az.najafov.deforestationnews.common.GenericResponse;
import az.najafov.deforestationnews.dto.CityRequestDto;
import az.najafov.deforestationnews.dto.CityResponseDto;
import az.najafov.deforestationnews.service.CityService;
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

@Tag(name = "Cities", description = "API operations for managing cities")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/general/cities")
public class CityController {

    private final CityService cityService;

    @GetMapping("/{id}")
    @Operation(summary = "Get city by ID", description = "Retrieves a specific city based on its unique identifier.")
    public GenericResponse<CityResponseDto> getById(@PathVariable Long id) {
        return GenericResponse.success(cityService.getById(id), "SUCCESS");
    }

    @GetMapping
    @Operation(summary = "Get all cities", description = "Retrieves a list of all available cities.")
    public GenericResponse<List<CityResponseDto>> getAll() {
        return GenericResponse.success(cityService.getAll(), "SUCCESS");
    }

    @GetMapping("/countries/{id}")
    @Operation(summary = "Get cities by country", description = "Retrieves cities associated with the " +
            "specified country.")
    public GenericResponse<List<CityResponseDto>> getAllByCountry(@PathVariable Long id) {
        return GenericResponse.success(cityService.getAllByCountry(id), "SUCCESS");
    }

}