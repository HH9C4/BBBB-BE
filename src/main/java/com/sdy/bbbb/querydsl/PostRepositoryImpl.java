package com.sdy.bbbb.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sdy.bbbb.entity.Post;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.sdy.bbbb.entity.QPost.post;
import static com.sdy.bbbb.entity.QImage.image;
import static com.sdy.bbbb.entity.QComment.comment1;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    //    private final ParsingEntityUtils parsingEntityUtils;
    private final JPAQueryFactory queryFactory;


    // 게시글 단건 조회
    @Override
    public Post searchOneById(Long postId) {
        Post result = queryFactory
                .select(post)
                .from(post)
                .where(post.id.eq(postId))
                .join(comment1).on(post.id.eq(comment1.id))
                .fetchOne();
        if(result==null){
            return null;
        }else {
            return result;
        }
    }

//    private OrderSpecifier eqSort2(String sort, Expression<T> target) {
//        if(sort.equals("hot")) {
//            post.likeCount.desc();
//            post.createdAt.desc();
//        } else if(sort.equals("new")) {
//            post.createdAt.desc();
//        } else {
//            throw new CustomException(ErrorCode.NotFoundSort);
//        }
//        return new OrderSpecifier<>()
//    }
//
//    PathBuilder orderByExpression = new PathBuilder(Post.class, "post");
//    private JPAQuery eqSort3(String sort) {
//        if(sort.equals("new")) {
//            return queryFactory.selectZero().orderBy(post.createdAt.desc());
//        } else if (sort.equals("hot")) {
//            return queryFactory.selectZero().orderBy(post.likeCount.desc()).orderBy(post.createdAt.desc());
//        } else {
//            throw new CustomException(ErrorCode.NotFoundUser);
//        }
//    }



    //sort sort 별 구문
//    private OrderSpecifier eqSort(String sort) {
//        if(sort.equals("new")) {
//            return null;
//        } else if (sort.equals("hot")) {
//            return post.likeCount.desc();
//        } else {
//            throw new CustomException(ErrorCode.NotFoundSort);
//        }
//    }

    // 게시글 전체 조회
//    @Override
//    public List<Post> customSortByGu(String guName) {
//        List<Post> result = queryFactory
//                .selectFrom(post)
//                .join(image)
//                .on(post.id.eq(image.post.id))
//                .where(post.guName.eq(guName))
//                .orderBy(post.createdAt.desc())
//                .fetch();
//        return result;
//    }



//    @Override
//    public List<Post> customSortByGu2(String guName) {
//        List<Post> result = queryFactory
//                .selectFrom(post)
//                .join(image)
//                .on(post.id.eq(image.post.id))
//                .where(post.guName.eq(guName))
//                .orderBy(post.likeCount.desc())
//                .fetch();
//        return result;
//    }
}
