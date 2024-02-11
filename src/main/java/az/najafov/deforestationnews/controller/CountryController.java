package az.najafov.deforestationnews.controller;

import az.najafov.deforestationnews.common.GenericResponse;
import az.najafov.deforestationnews.dto.CountryRequestDto;
import az.najafov.deforestationnews.dto.CountryResponseDto;
import az.najafov.deforestationnews.service.CountryService;
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

@Tag(name = "Countries", description = "API operations for managing countries")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/general/countries")
public class CountryController {

    private final CountryService countryService;

    @GetMapping("/{id}")
    @Operation(summary = "Get country by ID", description = "Retrieves a specific country based on its " +
            "unique identifier.")
    public GenericResponse<CountryResponseDto> getById(@PathVariable Long id) {
        return GenericResponse.success(countryService.getById(id), "SUCCESS");
    }

    @GetMapping
    @Operation(summary = "Get all countries", description = "Retrieves a list of all available countries.")
    public GenericResponse<List<CountryResponseDto>> getAll() {
        return GenericResponse.success(countryService.getAll(), "SUCCESS");
    }

}
