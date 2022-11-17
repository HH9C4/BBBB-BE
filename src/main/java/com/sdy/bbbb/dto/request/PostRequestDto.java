package com.sdy.bbbb.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
public class PostRequestDto {

    @NotBlank(message = "content 는 공백이거나 null 일 수 없습니다.")
    private String content;

    private List<String> deleteUrl;

    @NotBlank(message = "gu 는 공백이거나 null 일 수 없습니다.")
    private String gu;

    private List<String> tagList;

//    @NotBlank(message = "category 는 공백이거나 null 일 수 없습니다.")
//    private String category;

}
