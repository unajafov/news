package az.najafov.deforestationnews.dto;

import lombok.Data;

@Data
public class BaseNewsResponseDto {

    private Long id;
    private String title;
    private String previewText;
    private RegionResponseDto region;
    private int viewCount;
    private int likeCount;
    private int dislikeCount;

}
