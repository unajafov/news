package az.najafov.deforestationnews;

import az.najafov.deforestationnews.dto.BaseNewsResponseDto;
import az.najafov.deforestationnews.dto.CommentRequestDto;
import az.najafov.deforestationnews.dto.NewsRequestDto;
import az.najafov.deforestationnews.dto.NewsResponseDto;
import az.najafov.deforestationnews.mapper.CommentMapper;
import az.najafov.deforestationnews.mapper.NewsMapper;
import az.najafov.deforestationnews.model.News;
import az.najafov.deforestationnews.model.Region;
import az.najafov.deforestationnews.model.User;
import az.najafov.deforestationnews.repository.CommentRepository;
import az.najafov.deforestationnews.repository.NewsReactionRepository;
import az.najafov.deforestationnews.repository.NewsRepository;
import az.najafov.deforestationnews.repository.RegionRepository;
import az.najafov.deforestationnews.repository.UserRepository;
import az.najafov.deforestationnews.security.provider.TokenProvider;
import az.najafov.deforestationnews.service.NewsService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NewsServiceTest {

    @InjectMocks
    private NewsService newsService;

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

    @Mock
    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize annotated mocks
    }

    @Test
    void testCreate() {
        // Mock data
        NewsRequestDto requestDto = new NewsRequestDto();
        requestDto.setTitle("Test News Title");
        requestDto.setPreviewText("Test Preview Text");
        requestDto.setContext("Test News Context");

        // Mock behavior
        when(regionRepository.findById(any())).thenReturn(java.util.Optional.of(new Region()));

        // Test the method
        assertDoesNotThrow(() -> newsService.create(requestDto));

        // Verify that the save method is called
        verify(newsRepository, times(1)).save(any());
    }

    @Test
    void testUpdate() {
        // Mock data
        Long newsId = 1L;
        NewsRequestDto requestDto = new NewsRequestDto();
        requestDto.setTitle("Updated Title");
        requestDto.setPreviewText("Updated Preview Text");
        requestDto.setContext("Updated Context");

        // Mock behavior
        when(newsRepository.findById(newsId)).thenReturn(java.util.Optional.of(new News()));
        when(regionRepository.findById(any())).thenReturn(java.util.Optional.of(new Region()));

        // Test the method
        assertDoesNotThrow(() -> newsService.update(newsId, requestDto));

        // Verify that the save method is called
        verify(newsRepository, times(1)).save(any());
    }

    @Test
    void testGetById() {
        // Mock data
        Long newsId = 1L;

        // Mock behavior
        when(newsRepository.findById(newsId)).thenReturn(java.util.Optional.of(new News()));
        when(newsMapper.toResponseDto(any())).thenReturn(new NewsResponseDto());

        // Test the method
        NewsResponseDto responseDto = newsService.getById(newsId);

        // Verify that the mapper is called
        verify(newsMapper, times(1)).toResponseDto(any());
        assertNotNull(responseDto);
    }

    @Test
    void testDelete() {
        // Mock data
        Long newsId = 1L;

        // Mock behavior
        when(newsRepository.findById(newsId)).thenReturn(java.util.Optional.of(new News()));

        // Test the method
        assertDoesNotThrow(() -> newsService.delete(newsId));

        // Verify that the delete method is called
        verify(newsRepository, times(1)).delete(any());
    }

    @Test
    void testGetAll() {
        // Mock behavior
        when(newsRepository.findAll()).thenReturn(Collections.emptyList());
        when(newsMapper.toBaseResponseDto(any())).thenReturn(new BaseNewsResponseDto());

        // Test the method
        List<BaseNewsResponseDto> responseDtos = newsService.getAll();

        // Verify that the mapper is called
        verify(newsMapper, times(0)).toBaseResponseDto(any());
        assertNotNull(responseDtos);
        assertTrue(responseDtos.isEmpty());
    }

    @Test
    void testGetRegionalNews() {
        // Mock data
        Long regionId = 1L;

        // Mock behavior
        when(newsRepository.findAllByRegion(regionId)).thenReturn(Collections.emptyList());

        // Test the method
        List<BaseNewsResponseDto> responseDtos = newsService.getRegionalNews(regionId);

        // Verify that the mapper is NOT called
        verify(newsMapper, never()).toBaseResponseDto(any());
        assertNotNull(responseDtos);
        assertTrue(responseDtos.isEmpty());
    }

    @Test
    void testGetRegionalNewsByUser() {
        // Mock data
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String token = "mockToken";
        String username = "testUser";

        // Mock behavior
        when(tokenProvider.getJWTFromRequest(request)).thenReturn(token);
        when(tokenProvider.extractUsername(token)).thenReturn(username);

        User userWithRegion = createUserWithRegion();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userWithRegion));
        when(newsRepository.findAllByRegion(any())).thenReturn(Collections.emptyList());
        when(newsMapper.toBaseResponseDto(any())).thenReturn(new BaseNewsResponseDto());

        // Test the method
        List<BaseNewsResponseDto> responseDtos = newsService.getRegionalNewsByUser(request);

        assertNotNull(responseDtos);
        assertTrue(responseDtos.isEmpty());
    }

    @Test
    void testGetRegionalNewsByUserWithoutRegion() {
        // Mock data
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String token = "mockToken";
        String username = "testUser";

        // Mock behavior
        when(tokenProvider.getJWTFromRequest(request)).thenReturn(token);
        when(tokenProvider.extractUsername(token)).thenReturn(username);

        User userWithoutRegion = createUserWithoutRegion();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userWithoutRegion));

        // Test the method
        List<BaseNewsResponseDto> responseDtos = newsService.getRegionalNewsByUser(request);

        // Verify that the mapper is NOT called
        verify(newsMapper, never()).toBaseResponseDto(any());
        assertNull(responseDtos);
    }

    @Test
    void testAddComment() {
        // Mock data
        Long newsId = 1L;
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setText("Test Comment");

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String token = "mockToken";
        String username = "testUser";

        // Mock behavior
        when(tokenProvider.getJWTFromRequest(request)).thenReturn(token);
        when(tokenProvider.extractUsername(token)).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(createTestUser()));
        when(newsRepository.findById(newsId)).thenReturn(Optional.of(createTestNews()));

        // Test the method
        assertDoesNotThrow(() -> newsService.addComment(newsId, requestDto, request));

        // Verify that the save method is called
        verify(commentRepository, times(1)).save(any());
    }


    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        return user;
    }

    private News createTestNews() {
        News news = new News();
        news.setId(1L);
        news.setTitle("Test News Title");
        return news;
    }

    private User createUserWithRegion() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        Region region = new Region();
        region.setId(1L);
        user.setRegion(region);

        return user;
    }

    private User createUserWithoutRegion() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        return user;
    }

}
