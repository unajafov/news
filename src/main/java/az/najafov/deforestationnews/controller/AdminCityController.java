package az.najafov.deforestationnews.controller;

import az.najafov.deforestationnews.common.GenericResponse;
import az.najafov.deforestationnews.dto.CityRequestDto;
import az.najafov.deforestationnews.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin Cities", description = "API operations for managing cities as admin")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/cities")
public class AdminCityController {

    private final CityService cityService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new city", description = "Creates a new city with the provided details.")
    public GenericResponse<Void> create(@RequestBody CityRequestDto cityRequestDto) {
        cityService.create(cityRequestDto);
        return GenericResponse.success("SUCCESS");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a city", description = "Updates an existing city with the specified ID.")
    public GenericResponse<Void> update(@PathVariable Long id, @RequestBody CityRequestDto cityRequestDto) {
        cityService.update(id, cityRequestDto);
        return GenericResponse.success("SUCCESS");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a city", description = "Deletes an existing city based on its unique identifier.")
    public GenericResponse<Void> delete(@PathVariable Long id) {
        cityService.delete(id);
        return GenericResponse.success("SUCCESS");
    }

}

