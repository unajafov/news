package az.najafov.deforestationnews.controller;

import az.najafov.deforestationnews.common.GenericResponse;
import az.najafov.deforestationnews.dto.DistrictRequestDto;
import az.najafov.deforestationnews.dto.DistrictResponseDto;
import az.najafov.deforestationnews.service.DistrictService;
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

@Tag(name = "Districts", description = "API operations for managing districts")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/general/districts")
public class DistrictController {

    private final DistrictService districtService;

    @GetMapping("/{id}")
    @Operation(summary = "Get district by ID", description = "Retrieves a specific district based on its " +
            "unique identifier.")
    public GenericResponse<DistrictResponseDto> getById(@PathVariable Long id) {
        return GenericResponse.success(districtService.getById(id), "SUCCESS");
    }

    @GetMapping
    @Operation(summary = "Get all districts", description = "Retrieves a list of all available districts.")
    public GenericResponse<List<DistrictResponseDto>> getAll() {
        return GenericResponse.success(districtService.getAll(), "SUCCESS");
    }

    @GetMapping("/cities/{id}")
    @Operation(summary = "Get districts by city", description = "Retrieves districts associated with the " +
            "specified city.")
    public GenericResponse<List<DistrictResponseDto>> getAllByCity(@PathVariable Long id) {
        return GenericResponse.success(districtService.getAllByCity(id), "SUCCESS");
    }

}
