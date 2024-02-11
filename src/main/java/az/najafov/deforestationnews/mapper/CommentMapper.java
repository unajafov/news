package az.najafov.deforestationnews.mapper;

import az.najafov.deforestationnews.dto.CommentResponseDto;
import az.najafov.deforestationnews.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final UserMapper userMapper;

    public CommentResponseDto toResponseDto(Comment comment) {
        if (Objects.isNull(comment)) {
            return null;
        }
        CommentResponseDto responseDto = new CommentResponseDto();
        responseDto.setId(comment.getId());
        responseDto.setUser(userMapper.toResponseDto(comment.getUser()));
        responseDto.setText(comment.getText());

        return responseDto;
    }

}
