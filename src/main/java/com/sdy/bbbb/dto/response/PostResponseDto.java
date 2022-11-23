package com.sdy.bbbb.dto.response;

import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Post;
import com.sdy.bbbb.util.Chrono;
import lombok.Getter;

import java.util.List;

@Getter
public class PostResponseDto extends TagResponseDto{

    private Long postId;

    private String accountName;

    private String profileImage;

    private List<String> imageUrl;

    private String content;


    private String category;

    private String gu;

    private Integer commentCount;

    private Integer likeCount;

    private Boolean isLiked;

    private Integer views;

    private String createdAt;

    private String modifiedAt;


    public PostResponseDto(Post post, List<String> imageUrl, List<String> tagList, boolean isLiked) {
        super(tagList);
        this.postId = post.getId();
        this.accountName = post.getAccount().getAccountName();
        this.profileImage = post.getAccount().getProfileImage();
        this.imageUrl = imageUrl;
        this.content = post.getContent();
        this.gu = post.getGuName();
//        this.tag = tagList;
        this.category = post.getCategory();
        this.likeCount = post.getLikeCount();
        this.commentCount = post.getCommentCount();
        this.isLiked = isLiked;
        this.views = post.getViews();
        this.createdAt = Chrono.timesAgo(post.getCreatedAt());
        this.modifiedAt = Chrono.timesAgo(post.getModifiedAt());
    }

}
