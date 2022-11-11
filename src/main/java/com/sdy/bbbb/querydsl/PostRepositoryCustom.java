package com.sdy.bbbb.querydsl;

import com.sdy.bbbb.entity.Post;

import java.util.Optional;

public interface PostRepositoryCustom {

    Post searchOneById(Long postId);
}
