package com.sdy.bbbb.dto.response;

import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Post;
import com.sdy.bbbb.util.Chrono;
import lombok.Getter;

import java.util.List;

@Getter
public class PostResponseDto {

    private Long postId;

    private String accountName;

    private List<String> imageUrl;

    private String content;

    private String tag;

    private String category;

    private String gu;

    private Integer commentCount;

    private Integer likeCount;

    private Boolean isLiked;

    private Integer views;

    private String createdAt;

    private String modifiedAt;

    public PostResponseDto(Post post, List<String> imageUrl, boolean isLiked) {
        this.postId = post.getId();
        this.accountName = post.getAccount().getAccountName();
        this.imageUrl = imageUrl;
        this.content = post.getContent();
        this.gu = post.getGuName();
        this.tag = post.getTag();
//        this.category = post.getCategory();
        this.likeCount = post.getLikeCount();
        this.commentCount = post.getCommentCount();
        this.isLiked = isLiked;
        this.views = post.getViews();
        this.createdAt = Chrono.timesAgo(post.getCreatedAt());
        this.modifiedAt = Chrono.timesAgo(post.getModifiedAt());
    }

}
