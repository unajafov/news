package az.najafov.deforestationnews.service;

import az.najafov.deforestationnews.dto.BaseNewsResponseDto;
import az.najafov.deforestationnews.dto.CommentRequestDto;
import az.najafov.deforestationnews.dto.CommentResponseDto;
import az.najafov.deforestationnews.dto.CommentUpdateRequestDto;
import az.najafov.deforestationnews.dto.NewsRequestDto;
import az.najafov.deforestationnews.dto.NewsResponseDto;
import az.najafov.deforestationnews.enumeration.NewsReactionType;
import az.najafov.deforestationnews.exception.EntityNotFoundException;
import az.najafov.deforestationnews.mapper.CommentMapper;
import az.najafov.deforestationnews.mapper.NewsMapper;
import az.najafov.deforestationnews.model.Comment;
import az.najafov.deforestationnews.model.News;
import az.najafov.deforestationnews.model.NewsReaction;
import az.najafov.deforestationnews.model.Region;
import az.najafov.deforestationnews.model.User;
import az.najafov.deforestationnews.repository.CommentRepository;
import az.najafov.deforestationnews.repository.NewsReactionRepository;
import az.najafov.deforestationnews.repository.NewsRepository;
import az.najafov.deforestationnews.repository.RegionRepository;
import az.najafov.deforestationnews.repository.UserRepository;
import az.najafov.deforestationnews.security.provider.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final RegionRepository regionRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final NewsReactionRepository newsReactionRepository;
    private final TokenProvider tokenProvider;

    public void create(NewsRequestDto requestDto) {
        News news = new News();
        news.setTitle(requestDto.getTitle());
        news.setPreviewText(requestDto.getPreviewText());
        news.setContext(requestDto.getContext());

        if (Objects.nonNull(requestDto.getRegionId())) {
            Region region = regionRepository.findById(requestDto.getRegionId()).orElseThrow(() ->
                    new EntityNotFoundException(Region.class, requestDto.getRegionId()));
            news.setRegion(region);
        }

        newsRepository.save(news);
    }

    public void update(Long id, NewsRequestDto requestDto) {
        News news = newsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(News.class, id));
        news.setTitle(requestDto.getTitle());
        news.setPreviewText(requestDto.getPreviewText());
        news.setContext(requestDto.getContext());

        if (Objects.nonNull(requestDto.getRegionId())) {
            Region region = regionRepository.findById(requestDto.getRegionId()).orElseThrow(() ->
                    new EntityNotFoundException(Region.class, requestDto.getRegionId()));
            news.setRegion(region);
        }

        newsRepository.save(news);
    }

    public NewsResponseDto getById(Long id) {
        News news = newsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(News.class, id));
        return newsMapper.toResponseDto(news);
    }

    public void delete(Long id) {
        News news = newsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(News.class, id));
        newsRepository.delete(news);
    }

    public List<BaseNewsResponseDto> getAll() {
        return newsRepository.findAll().stream().map(newsMapper::toBaseResponseDto).collect(Collectors.toList());
    }

    public List<BaseNewsResponseDto> getRegionalNews(Long regionId) {
        return newsRepository.findAllByRegion(regionId).stream().map(newsMapper::toBaseResponseDto)
                .collect(Collectors.toList());
    }

    public List<BaseNewsResponseDto> getRegionalNewsByUser(HttpServletRequest request) {
        String token = tokenProvider.getJWTFromRequest(request);
        String username = tokenProvider.extractUsername(token);

        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new EntityNotFoundException(User.class, username));
        if (Objects.isNull(user.getRegion())) {
            return null;
        }
        return newsRepository.findAllByRegion(user.getRegion().getId()).stream().map(newsMapper::toBaseResponseDto)
                .collect(Collectors.toList());
    }

    public void addComment(Long newsId, CommentRequestDto requestDto, HttpServletRequest request) {
        String token = tokenProvider.getJWTFromRequest(request);
        String username = tokenProvider.extractUsername(token);

        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new EntityNotFoundException(User.class, username));

        Comment comment = new Comment();
        comment.setText(requestDto.getText());

        News news = newsRepository.findById(newsId).orElseThrow(() -> new EntityNotFoundException(News.class, newsId));
        comment.setNews(news);
        comment.setUser(user);


        commentRepository.save(comment);
    }

    public void updateComment(Long commentId, CommentUpdateRequestDto requestDto, HttpServletRequest request) {
        String token = tokenProvider.getJWTFromRequest(request);
        String username = tokenProvider.extractUsername(token);

        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new EntityNotFoundException(User.class, username));

        Comment comment = commentRepository.findByIdAndUserId(commentId, user.getId()).orElseThrow(() ->
                new EntityNotFoundException(Comment.class, commentId));
        comment.setText(requestDto.getText());

        commentRepository.save(comment);
    }

    public List<CommentResponseDto> getCommentsByNews(Long newsId) {
        News news = newsRepository.findByIdWithComments(newsId).orElseThrow(() ->
                new EntityNotFoundException(News.class, newsId));
        return news.getComments().stream().map(commentMapper::toResponseDto).collect(Collectors.toList());
    }

    public CommentResponseDto getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new EntityNotFoundException(Comment.class, commentId));
        return commentMapper.toResponseDto(comment);
    }

    public void view(Long newsId, HttpServletRequest request) {
        String token = tokenProvider.getJWTFromRequest(request);
        String username = tokenProvider.extractUsername(token);
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new EntityNotFoundException(User.class, "with username : " + username));
        Long userId = user.getId();
        if (checkHasReaction(userId, newsId, NewsReactionType.VIEW)) return;

        News news = newsRepository.findById(newsId).orElseThrow(() -> new EntityNotFoundException(News.class, newsId));

        NewsReaction newsReaction = new NewsReaction();
        newsReaction.setUser(user);
        newsReaction.setNews(news);
        newsReaction.setType(NewsReactionType.VIEW);
        newsReactionRepository.save(newsReaction);
    }

    public void like(Long newsId, HttpServletRequest request) {
        String token = tokenProvider.getJWTFromRequest(request);
        String username = tokenProvider.extractUsername(token);
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new EntityNotFoundException(User.class, "with username : " + username));
        Long userId = user.getId();
        if (checkHasReaction(userId, newsId, NewsReactionType.LIKE)) return;

        News news = newsRepository.findById(newsId).orElseThrow(() -> new EntityNotFoundException(News.class, newsId));

        Optional<NewsReaction> oppositeReactionOptional = newsReactionRepository
                .findReactionByNewsAndUserAndType(newsId, userId, NewsReactionType.DISLIKE.name());
        if (oppositeReactionOptional.isPresent()) {
            NewsReaction oppositeReaction = oppositeReactionOptional.get();
            newsReactionRepository.delete(oppositeReaction);
        }

        NewsReaction newsReaction = new NewsReaction();
        newsReaction.setUser(user);
        newsReaction.setNews(news);
        newsReaction.setType(NewsReactionType.LIKE);
        newsReactionRepository.save(newsReaction);
    }

    public void dislike(Long newsId, HttpServletRequest request) {
        String token = tokenProvider.getJWTFromRequest(request);
        String username = tokenProvider.extractUsername(token);
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new EntityNotFoundException(User.class, "with username : " + username));
        Long userId = user.getId();

        if (checkHasReaction(userId, newsId, NewsReactionType.DISLIKE)) return;

        News news = newsRepository.findById(newsId).orElseThrow(() -> new EntityNotFoundException(News.class, newsId));

        Optional<NewsReaction> oppositeReactionOptional = newsReactionRepository
                .findReactionByNewsAndUserAndType(newsId, userId, NewsReactionType.LIKE.name());
        if (oppositeReactionOptional.isPresent()) {
            NewsReaction oppositeReaction = oppositeReactionOptional.get();
            newsReactionRepository.delete(oppositeReaction);
        }

        NewsReaction newsReaction = new NewsReaction();
        newsReaction.setUser(user);
        newsReaction.setNews(news);
        newsReaction.setType(NewsReactionType.DISLIKE);
        newsReactionRepository.save(newsReaction);
    }

    private boolean checkHasReaction(Long userId, Long newsId, NewsReactionType type) {
        Optional<NewsReaction> previousReaction = newsReactionRepository
                .findReactionByNewsAndUserAndType(newsId, userId, type.name());
        return previousReaction.isPresent();
    }

}
