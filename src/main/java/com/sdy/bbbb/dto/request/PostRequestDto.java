package com.sdy.bbbb.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
public class PostRequestDto {
    @NotBlank
    private String content;
    @NotBlank
    private String category;
    private List<String> imageUrl;
    private List<String> deleteUrl;
    @NotBlank
    private String gu;
}
