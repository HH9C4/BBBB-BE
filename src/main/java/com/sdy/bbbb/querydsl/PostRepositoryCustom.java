package com.sdy.bbbb.querydsl;

import com.sdy.bbbb.entity.Post;

import java.util.List;

public interface PostRepositoryCustom {

    Post searchOneById(Long postId);

//    Post searchPostList(Long postId);
//
    List<Post> test2(String gu, String sort);

    List<Post> searchByTag(Integer type, String searchWord, String sort);

//    List<Post> customSortByGu(String guName);

//    List<Post> customSortByGu2(String guName);


}
