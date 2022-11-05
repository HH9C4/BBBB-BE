package com.sdy.bbbb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Post;
import com.sdy.bbbb.util.Chrono;
import lombok.Getter;

import java.util.List;

@Getter
public class PostResponseDto {
    private Long postId;
    private String accountName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> imageUrl;
    private String content;
    private String category;
    private String gu;
    private Integer likeCount;
    private Boolean isLiked;
    private Integer views;
    private String createdAt;
    private String modifiedAt;

//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private Comment comment;

    public PostResponseDto(Post post, Account account, List<String> imageUrl, boolean isLiked) {
        this.postId = post.getId();
        this.accountName = account.getUsername();
        this.imageUrl = imageUrl;
        this.content = post.getContent();
        this.category = post.getCategory();
        this.gu = post.getGu();
        this.likeCount = post.getLikeCount();
        this.isLiked = isLiked;
        this.views = post.getViews();
        this.createdAt = Chrono.timesAgo(post.getCreatedAt());
        this.modifiedAt = Chrono.timesAgo(post.getModifiedAt());
    }
}
