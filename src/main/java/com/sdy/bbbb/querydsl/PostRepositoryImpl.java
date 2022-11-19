package com.sdy.bbbb.querydsl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sdy.bbbb.entity.HashTag;
import com.sdy.bbbb.entity.Post;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sdy.bbbb.entity.QComment.comment1;
import static com.sdy.bbbb.entity.QHashTag.hashTag;
import static com.sdy.bbbb.entity.QPost.post;

@RequiredArgsConstructor
@Repository
public class PostRepositoryImpl implements PostRepositoryCustom {


    private final JPAQueryFactory queryFactory;


    // 게시글 단건 조회
    @Override
    public Post searchOneById(Long postId) {
        return queryFactory
                .select(post)
                .from(post)
                .join(post.account).fetchJoin()
                .leftJoin(post.commentList).fetchJoin()
                .where(post.id.eq(postId))
                .fetchOne();
    }

    @Override
    public List<Post> test2(String gu, String category, String sort) {
        return queryFactory
                .select(post)
//                .distinct()
                .from(post)
//                .leftJoin(hashTag).on(post.id.eq(hashTag.post.id)).fetchJoin()
                .join(post.account).fetchJoin()
//                .leftJoin(post.tagList).fetchJoin()
                .where(post.guName.eq(gu))
//                .leftJoin(post.likeList)
                .orderBy(eqSort2(sort), post.createdAt.desc())
                //페이징 할 때 수정해야 할것이다!
//                .orderBy(post.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Post> searchByTag(Integer type, String searchWord, String sort) {
        return queryFactory
                .select(post).distinct()
                .from(post)
                .leftJoin(post.tagList).fetchJoin()
                .where(tagOrNot(type, searchWord))
                .orderBy(eqSort(sort), post.createdAt.desc())
                .orderBy(hashTag.id.asc())
                .fetch();
    }

    //    sort sort 별 구문
    private OrderSpecifier eqSort(String sort) {
        if(sort.equals("new")) {
            return post.createdAt.desc();
            //나중에 생각해보자
        } else if (sort.equals("hot")) {
            return post.likeCount.desc();
        } else {
            throw new CustomException(ErrorCode.BadRequest);
        }
    }

    private BooleanExpression tagOrNot(Integer type, String searchWord) {
        if(type == 0){
            return post.content.contains(searchWord).or(hashTag.tag.contains(searchWord));
        }else if (type == 1) {
            return hashTag.tag.contains(searchWord);
        }else{
            throw new CustomException(ErrorCode.BadRequest);
        }
    }

    private BooleanExpression category(String category){
        if (category.equals("All")){
            return null;
        }else {
            return post.category.eq(category);
        }
    }

    //        private OrderSpecifier eqSort2(String sort, Expression<T> target) {
//        if(sort.equals("hot")) {
//            post.likeCount.desc();
//            post.createdAt.desc();
//        } else if(sort.equals("new")) {
//            post.createdAt.desc();
//        } else {
//            throw new CustomException(ErrorCode.NotFoundPost);
//        }
//        return new OrderSpecifier<>();
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
