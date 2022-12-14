package com.sdy.bbbb.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import static com.sdy.bbbb.entity.QComment.comment1;
import static com.sdy.bbbb.entity.QPost.post;


@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> searchCommentsInMyPosts(Account account1) {
        return queryFactory
                .select(comment1)
                .from(comment1)
                .join(post).on(comment1.post.id.eq(post.id))
                .join(comment1.account).fetchJoin()
                .where(post.account.id.eq(account1.getId()))
                .orderBy(comment1.id.desc())
                .fetch();
    }


}
