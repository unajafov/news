package az.najafov.deforestationnews;

import az.najafov.deforestationnews.dto.BaseNewsResponseDto;
import az.najafov.deforestationnews.dto.CommentRequestDto;
import az.najafov.deforestationnews.dto.CommentResponseDto;
import az.najafov.deforestationnews.dto.CommentUpdateRequestDto;
import az.najafov.deforestationnews.dto.NewsRequestDto;
import az.najafov.deforestationnews.dto.NewsResponseDto;
import az.najafov.deforestationnews.enumeration.NewsReactionType;
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
import az.najafov.deforestationnews.service.NewsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NewsServiceTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private NewsMapper newsMapper;

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private NewsReactionRepository newsReactionRepository;

    @InjectMocks
    private NewsService newsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        NewsRequestDto requestDto = new NewsRequestDto();
        requestDto.setTitle("Test News");
        requestDto.setPreviewText("Preview Text");
        requestDto.setContext("News Content");
        requestDto.setRegionId(1L);

        Region region = new Region();
        when(regionRepository.findById(requestDto.getRegionId())).thenReturn(Optional.of(region));

        newsService.create(requestDto);

        verify(newsRepository, times(1)).save(any());
    }

    @Test
    void testUpdate() {
        Long newsId = 1L;
        NewsRequestDto requestDto = new NewsRequestDto();
        requestDto.setTitle("Updated News");
        requestDto.setPreviewText("Updated Preview Text");
        requestDto.setContext("Updated News Content");
        requestDto.setRegionId(2L);

        News existingNews = new News();
        when(newsRepository.findById(newsId)).thenReturn(Optional.of(existingNews));

        Region region = new Region();
        when(regionRepository.findById(requestDto.getRegionId())).thenReturn(Optional.of(region));

        newsService.update(newsId, requestDto);

        assertEquals(requestDto.getTitle(), existingNews.getTitle());
        assertEquals(requestDto.getPreviewText(), existingNews.getPreviewText());
        assertEquals(requestDto.getContext(), existingNews.getContext());
        assertEquals(region, existingNews.getRegion());

        verify(newsRepository, times(1)).save(existingNews);
    }

    @Test
    void testGetById() {
        Long newsId = 1L;
        News existingNews = new News();
        when(newsRepository.findById(newsId)).thenReturn(Optional.of(existingNews));

        NewsResponseDto responseDto = new NewsResponseDto();
        when(newsMapper.toResponseDto(existingNews)).thenReturn(responseDto);

        NewsResponseDto result = newsService.getById(newsId);

        assertNotNull(result);
        assertEquals(responseDto, result);
    }

    @Test
    void testDelete() {
        Long newsId = 1L;
        News existingNews = new News();
        when(newsRepository.findById(newsId)).thenReturn(Optional.of(existingNews));

        newsService.delete(newsId);

        verify(newsRepository, times(1)).delete(existingNews);
    }

    @Test
    void testGetAll() {
        News news1 = new News();
        News news2 = new News();
        when(newsRepository.findAll()).thenReturn(Arrays.asList(news1, news2));

        BaseNewsResponseDto baseNews1 = new BaseNewsResponseDto();
        BaseNewsResponseDto baseNews2 = new BaseNewsResponseDto();
        when(newsMapper.toBaseResponseDto(news1)).thenReturn(baseNews1);
        when(newsMapper.toBaseResponseDto(news2)).thenReturn(baseNews2);

        List<BaseNewsResponseDto> result = newsService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(baseNews1));
        assertTrue(result.contains(baseNews2));
    }

    @Test
    void testGetRegionalNews() {
        Long regionId = 1L;
        News news1 = new News();
        News news2 = new News();
        when(newsRepository.findAllByRegion(regionId)).thenReturn(Arrays.asList(news1, news2));

        BaseNewsResponseDto baseNews1 = new BaseNewsResponseDto();
        BaseNewsResponseDto baseNews2 = new BaseNewsResponseDto();
        when(newsMapper.toBaseResponseDto(news1)).thenReturn(baseNews1);
        when(newsMapper.toBaseResponseDto(news2)).thenReturn(baseNews2);

        List<BaseNewsResponseDto> result = newsService.getRegionalNews(regionId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(baseNews1));
        assertTrue(result.contains(baseNews2));
    }

    @Test
    void testGetRegionalNewsByUser() {
        Long userId = 1L;
        User user = new User();
        user.setRegion(new Region());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        News news1 = new News();
        News news2 = new News();
        when(newsRepository.findAllByRegion(user.getRegion().getId())).thenReturn(Arrays.asList(news1, news2));

        BaseNewsResponseDto baseNews1 = new BaseNewsResponseDto();
        BaseNewsResponseDto baseNews2 = new BaseNewsResponseDto();
        when(newsMapper.toBaseResponseDto(news1)).thenReturn(baseNews1);
        when(newsMapper.toBaseResponseDto(news2)).thenReturn(baseNews2);

        List<BaseNewsResponseDto> result = newsService.getRegionalNewsByUser(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(baseNews1));
        assertTrue(result.contains(baseNews2));
    }

    @Test
    void testAddComment() {
        Long newsId = 1L;
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setText("Test Comment");
        requestDto.setUserId(1L);

        News news = new News();
        when(newsRepository.findById(newsId)).thenReturn(Optional.of(news));

        User user = new User();
        when(userRepository.findById(requestDto.getUserId())).thenReturn(Optional.of(user));

        newsService.addComment(newsId, requestDto);

        verify(commentRepository, times(1)).save(any());
    }

    @Test
    void testUpdateComment() {
        Long commentId = 1L;
        CommentUpdateRequestDto requestDto = new CommentUpdateRequestDto();
        requestDto.setText("Updated Comment");

        Comment existingComment = new Comment();
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));

        newsService.updateComment(commentId, requestDto);

        assertEquals(requestDto.getText(), existingComment.getText());

        verify(commentRepository, times(1)).save(existingComment);
    }

    @Test
    public void testGetCommentsByNews() {
        Long newsId = 1L;

        // Create a News entity
        News news = new News();
        news.setId(newsId);

        // Create comments
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();

        // Set up the comments collection for the News entity
        news.setComments(Arrays.asList(comment1, comment2));

        // Mock news repository to return the expected News entity with comments
        when(newsRepository.findByIdWithComments(newsId)).thenReturn(Optional.of(news));

        // Mock comment mapper
        CommentResponseDto commentResponse1 = new CommentResponseDto();
        CommentResponseDto commentResponse2 = new CommentResponseDto();
        when(commentMapper.toResponseDto(comment1)).thenReturn(commentResponse1);
        when(commentMapper.toResponseDto(comment2)).thenReturn(commentResponse2);

        // Invoke the method under test
        List<CommentResponseDto> result = newsService.getCommentsByNews(newsId);

        // Assertions...
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(commentResponse1));
        assertTrue(result.contains(commentResponse2));
    }

    @Test
    void testGetCommentById() {
        Long commentId = 1L;
        Comment existingComment = new Comment();
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));

        CommentResponseDto responseDto = new CommentResponseDto();
        when(commentMapper.toResponseDto(existingComment)).thenReturn(responseDto);

        CommentResponseDto result = newsService.getCommentById(commentId);

        assertNotNull(result);
        assertEquals(responseDto, result);
    }

    @Test
    void testView() {
        Long userId = 1L;
        Long newsId = 1L;
        User user = new User();
        user.setId(userId);
        News news = new News();
        news.setId(newsId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(newsRepository.findById(newsId)).thenReturn(Optional.of(news));
        when(newsReactionRepository.findReactionByNewsAndUserAndType(newsId, userId, NewsReactionType.VIEW.name()))
                .thenReturn(Optional.empty());

        newsService.view(userId, newsId);

        verify(newsReactionRepository, times(1)).save(any());
    }

    @Test
    void testLike() {
        Long userId = 1L;
        Long newsId = 1L;
        User user = new User();
        user.setId(userId);
        News news = new News();
        news.setId(newsId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(newsRepository.findById(newsId)).thenReturn(Optional.of(news));
        when(newsReactionRepository.findReactionByNewsAndUserAndType(newsId, userId, NewsReactionType.LIKE.name()))
                .thenReturn(Optional.empty());

        when(newsReactionRepository.findReactionByNewsAndUserAndType(newsId, userId, NewsReactionType.DISLIKE.name()))
                .thenReturn(Optional.of(new NewsReaction()));

        newsService.like(userId, newsId);

        verify(newsReactionRepository, times(1)).delete(any());
        verify(newsReactionRepository, times(1)).save(any());
    }

    @Test
    void testDislike() {
        Long userId = 1L;
        Long newsId = 1L;
        User user = new User();
        user.setId(userId);
        News news = new News();
        news.setId(newsId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(newsRepository.findById(newsId)).thenReturn(Optional.of(news));
        when(newsReactionRepository.findReactionByNewsAndUserAndType(newsId, userId, NewsReactionType.DISLIKE.name()))
                .thenReturn(Optional.empty());

        when(newsReactionRepository.findReactionByNewsAndUserAndType(newsId, userId, NewsReactionType.LIKE.name()))
                .thenReturn(Optional.of(new NewsReaction()));

        newsService.dislike(userId, newsId);

        verify(newsReactionRepository, times(1)).delete(any());
        verify(newsReactionRepository, times(1)).save(any());
    }

}
