package com.sdy.bbbb.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sdy.bbbb.entity.Post;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.sdy.bbbb.entity.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    //    private final ParsingEntityUtils parsingEntityUtils;
    private final JPAQueryFactory queryFactory;

    @Override
    public Post searchOneById(Long postId) {
        Post result = queryFactory
                .select(post)
                .from(post)
                .where(post.id.eq(postId))
                .fetchOne();
        if(result==null){
            return null;
        }else {
            return result;
        }
    }


}
