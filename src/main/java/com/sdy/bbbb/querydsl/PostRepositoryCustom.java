package com.sdy.bbbb.querydsl;

import com.sdy.bbbb.entity.Post;

import java.util.List;

public interface PostRepositoryCustom {

    Post searchOneById(Long postId);

    List<Post> searchPostsByGuName(String guName);

//    List<Post> customSortByGu2(String guName);


}
