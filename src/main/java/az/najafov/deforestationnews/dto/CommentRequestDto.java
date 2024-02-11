package az.najafov.deforestationnews.dto;

import lombok.Data;

@Data
public class CommentRequestDto {

    private Long newsId;
    private Long userId;
    private String text;

}
