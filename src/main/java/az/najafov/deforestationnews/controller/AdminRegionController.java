package az.najafov.deforestationnews.controller;

import az.najafov.deforestationnews.dto.RegionRequestDto;
import az.najafov.deforestationnews.service.RegionService;
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

@Tag(name = "Admin Regions", description = "API operations for managing regions as admin")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/regions")
public class AdminRegionController {

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


}
