package az.najafov.deforestationnews.mapper;

import az.najafov.deforestationnews.dto.BaseNewsResponseDto;
import az.najafov.deforestationnews.dto.NewsResponseDto;
import az.najafov.deforestationnews.enumeration.NewsReactionType;
import az.najafov.deforestationnews.model.News;
import az.najafov.deforestationnews.repository.NewsReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class NewsMapper {

    private final RegionMapper regionMapper;
    private final NewsReactionRepository newsReactionRepository;

    public BaseNewsResponseDto toBaseResponseDto(News news) {
        if (Objects.isNull(news)) {
            return null;
        }
        BaseNewsResponseDto responseDto = new BaseNewsResponseDto();
        responseDto.setId(news.getId());
        responseDto.setTitle(news.getTitle());
        responseDto.setPreviewText(news.getPreviewText());
        if (Objects.nonNull(news.getRegion())) {
            responseDto.setRegion(regionMapper.toResponseDto(news.getRegion()));
        }
        int totalViews = newsReactionRepository.findNewsReactionCountByNewsAndType(news.getId(),
                NewsReactionType.VIEW.name());
        int totalLikes = newsReactionRepository.findNewsReactionCountByNewsAndType(news.getId(),
                NewsReactionType.LIKE.name());
        int totalDislikes = newsReactionRepository.findNewsReactionCountByNewsAndType(news.getId(),
                NewsReactionType.DISLIKE.name());

        responseDto.setViewCount(totalViews);
        responseDto.setLikeCount(totalLikes);
        responseDto.setDislikeCount(totalDislikes);
        return responseDto;
    }

    public NewsResponseDto toResponseDto(News news) {
        if (Objects.isNull(news)) {
            return null;
        }
        NewsResponseDto responseDto = new NewsResponseDto();
        responseDto.setId(news.getId());
        responseDto.setTitle(news.getTitle());
        responseDto.setContext(news.getContext());
        responseDto.setPreviewText(news.getPreviewText());

        if (Objects.nonNull(news.getRegion())) {
            responseDto.setRegion(regionMapper.toResponseDto(news.getRegion()));
        }

        int totalViews = newsReactionRepository.findNewsReactionCountByNewsAndType(news.getId(),
                NewsReactionType.VIEW.name());
        int totalLikes = newsReactionRepository.findNewsReactionCountByNewsAndType(news.getId(),
                NewsReactionType.LIKE.name());
        int totalDislikes = newsReactionRepository.findNewsReactionCountByNewsAndType(news.getId(),
                NewsReactionType.DISLIKE.name());

        responseDto.setViewCount(totalViews);
        responseDto.setLikeCount(totalLikes);
        responseDto.setDislikeCount(totalDislikes);

        return responseDto;
    }

}
