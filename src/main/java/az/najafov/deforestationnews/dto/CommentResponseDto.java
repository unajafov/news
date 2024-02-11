package az.najafov.deforestationnews.dto;

import lombok.Data;

@Data
public class CommentResponseDto {

    private Long id;
    private UserResponseDto user;
    private String text;

}
