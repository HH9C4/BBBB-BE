package com.sdy.bbbb.querydsl;

import com.sdy.bbbb.entity.Post;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostRepositoryCustom {

    Post searchOneById(Long postId);

    List<Post> searchPostsByGuName(String guName);

    List<Post> searchPostsByGuNameOrderByLikeCount(String guName);
}
