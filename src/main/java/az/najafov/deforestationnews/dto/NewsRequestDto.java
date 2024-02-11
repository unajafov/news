package az.najafov.deforestationnews.dto;

import lombok.Data;

@Data
public class NewsRequestDto {

    private String title;
    private String previewText;
    private String context;
    private Long regionId;

}
