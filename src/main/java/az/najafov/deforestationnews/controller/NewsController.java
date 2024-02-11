package az.najafov.deforestationnews.controller;

import az.najafov.deforestationnews.dto.CommentRequestDto;
import az.najafov.deforestationnews.dto.CommentResponseDto;
import az.najafov.deforestationnews.dto.CommentUpdateRequestDto;
import az.najafov.deforestationnews.dto.NewsRequestDto;
import az.najafov.deforestationnews.dto.BaseNewsResponseDto;
import az.najafov.deforestationnews.dto.NewsResponseDto;
import az.najafov.deforestationnews.service.NewsService;
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

@Tag(name = "News", description = "API operations for managing news articles and comments")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/general/news")
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/{id}")
    @Operation(summary = "Get news article by ID", description = "Retrieves a specific news article based on " +
            "its unique identifier.")
    public NewsResponseDto getById(@PathVariable Long id) {
        return newsService.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete news article by ID", description = "Deletes a specific news article based on " +
            "its unique identifier.")
    public void delete(@PathVariable Long id) {
        newsService.delete(id);
    }

    @GetMapping
    @Operation(summary = "Get all news articles", description = "Retrieves a list of all available news articles.")
    public List<BaseNewsResponseDto> getAll() {
        return newsService.getAll();
    }

    @GetMapping("/regions/{regionId}")
    @Operation(summary = "Get regional news articles", description = "Retrieves news articles specific to " +
            "the given region.")
    public List<BaseNewsResponseDto> getRegionalNews(@PathVariable Long regionId) {
        return newsService.getRegionalNews(regionId);
    }

    @GetMapping("/regions/users/{userId}")
    @Operation(summary = "Get regional news articles by user", description = "Retrieves news articles for a specific " +
            "region based on the user's ID.")
    public List<BaseNewsResponseDto> getRegionalNewsByUser(@PathVariable Long userId) {
        return newsService.getRegionalNewsByUser(userId);
    }

    @PostMapping("{id}/comments")
    @Operation(summary = "Add comment to news article", description = "Adds a new comment to the " +
            "specified news article.")
    public void addComment(@PathVariable Long id, CommentRequestDto requestDto) {
        newsService.addComment(id, requestDto);
    }

    @PutMapping("{id}/comments")
    @Operation(summary = "Update comment on news article", description = "Updates an existing comment on the " +
            "specified news article.")
    public void updateComment(@PathVariable Long id, CommentUpdateRequestDto requestDto) {
        newsService.updateComment(id, requestDto);
    }

    @GetMapping("{id}/comments")
    @Operation(summary = "Get comments for news article", description = "Retrieves all comments associated with " +
            "the specified news article.")
    public List<CommentResponseDto> getCommentsByNews(@PathVariable Long id) {
        return newsService.getCommentsByNews(id);
    }

    @GetMapping("/comments/{id}")
    @Operation(summary = "Get comment by ID", description = "Retrieves a specific comment based on " +
            "its unique identifier.")
    public CommentResponseDto getCommentById(@PathVariable Long id) {
        return newsService.getCommentById(id);
    }

    @PostMapping("/{newsId}/users/{userId}/views")
    @Operation(summary = "Add view to news article", description = "Adds a new view to the " +
            "specified news article.")
    public void view(@PathVariable Long newsId, @PathVariable Long userId) {
        newsService.view(userId, newsId);
    }

    @PostMapping("/{newsId}/users/{userId}/likes")
    @Operation(summary = "Add like to news article", description = "Adds a new like to the " +
            "specified news article.")
    public void like(@PathVariable Long newsId, @PathVariable Long userId) {
        newsService.like(userId, newsId);
    }

    @PostMapping("/{newsId}/users/{userId}/dislikes")
    @Operation(summary = "Add dislike to news article", description = "Adds a new dislike to the " +
            "specified news article.")
    public void dislike(@PathVariable Long newsId, @PathVariable Long userId) {
        newsService.dislike(userId, newsId);
    }

}
