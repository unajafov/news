package az.najafov.deforestationnews.controller;

import az.najafov.deforestationnews.common.GenericResponse;
import az.najafov.deforestationnews.dto.DistrictRequestDto;
import az.najafov.deforestationnews.service.DistrictService;
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

@Tag(name = "Admin Districts", description = "API operations for managing districts as admin")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/districts")
public class AdminDistrictController {

    private final DistrictService districtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new district", description = "Creates a new district with the provided details.")
    public GenericResponse<Void> create(@RequestBody DistrictRequestDto districtRequestDto) {
        districtService.create(districtRequestDto);
        return GenericResponse.success("SUCCESS");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a district", description = "Updates an existing district with the specified ID.")
    public GenericResponse<Void> update(@PathVariable Long id, @RequestBody DistrictRequestDto districtRequestDto) {
        districtService.update(id, districtRequestDto);
        return GenericResponse.success("SUCCESS");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a district", description = "Deletes an existing district based on its " +
            "unique identifier.")
    public GenericResponse<Void> delete(@PathVariable Long id) {
        districtService.delete(id);
        return GenericResponse.success("SUCCESS");
    }

}
