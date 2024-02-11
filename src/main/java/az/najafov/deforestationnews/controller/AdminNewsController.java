package az.najafov.deforestationnews.controller;


import az.najafov.deforestationnews.common.GenericResponse;
import az.najafov.deforestationnews.dto.NewsRequestDto;
import az.najafov.deforestationnews.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin News", description = "API operations for managing news as admin")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/news")
public class AdminNewsController {

    private final NewsService newsService;

    @PostMapping
    @Operation(summary = "Create a new news article", description = "Creates a new news article with " +
            "the provided details.")
    public GenericResponse<Void> create(@RequestBody NewsRequestDto requestDto) {
        newsService.create(requestDto);
        return GenericResponse.success("SUCCESS");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a news article", description = "Updates an existing news article with " +
            "the specified ID.")
    public void update(@PathVariable Long id, @RequestBody NewsRequestDto requestDto) {
        newsService.update(id, requestDto);
    }

}
