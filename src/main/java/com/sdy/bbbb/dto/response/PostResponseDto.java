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
    private List<String> imageUrl;
    private String content;
    private String tag;
    private String gu;
    private Integer likeCount;
    private Boolean isLiked;
    private Integer views;
    private String createdAt;
    private String modifiedAt;



    public PostResponseDto(Post post, Account account, List<String> imageUrl, boolean isLiked) {
        this.postId = post.getId();
        this.accountName = account.getUsername();
        this.imageUrl = imageUrl;
        this.content = post.getContent();
        this.gu = post.getGu();
        this.tag = post.getTag();
        this.likeCount = post.getLikeCount();
        this.isLiked = isLiked;
        this.views = post.getViews();
        this.createdAt = Chrono.timesAgo(post.getCreatedAt());
        this.modifiedAt = Chrono.timesAgo(post.getModifiedAt());
    }

    public PostResponseDto(Post post, Account account) {
        this.postId = post.getId();
        this.accountName = account.getUsername();
        this.content = post.getContent();
        this.gu = post.getGu();
        this.tag = post.getTag();
        this.likeCount = post.getLikeCount();
        this.views = post.getViews();
        this.createdAt = Chrono.timesAgo(post.getCreatedAt());
        this.modifiedAt = Chrono.timesAgo(post.getModifiedAt());
    }

    //이미지 없을 경우?? 어차피 빈배열일테니 필요없다.
//    public PostResponseDto(Post post, Account account, boolean isLiked) {
//        this.postId = post.getId();
//        this.accountName = account.getUsername();
//        this.imageUrl = null;
//        this.content = post.getContent();
//        this.gu = post.getGu();
//        this.tag = post.getTag();
//        this.likeCount = post.getLikeCount();
//        this.isLiked = isLiked;
//        this.views = post.getViews();
//        this.createdAt = Chrono.timesAgo(post.getCreatedAt());
//        this.modifiedAt = Chrono.timesAgo(post.getModifiedAt());
//    }
}
