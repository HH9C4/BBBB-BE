package com.sdy.bbbb.repository.querydsl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sdy.bbbb.entity.Post;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.sdy.bbbb.entity.QHashTag.hashTag;
import static com.sdy.bbbb.entity.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl {

    private final JPAQueryFactory queryFactory;

    // 게시글 단건 조회
    public Post searchOneById(Long postId) {
        return queryFactory
                .select(post)
                .from(post)
                .join(post.account).fetchJoin()
                .leftJoin(post.commentList).fetchJoin()
                .where(post.id.eq(postId))
                .fetchOne();
    }

    //게시글 전체 조회
    public PageImpl<Post> test2(String gu, String category, String sort, Pageable pageable) {
        List<Post> postList = queryFactory
                .select(post)
//                .distinct()
                .from(post)
//                .leftJoin(hashTag).on(post.id.eq(hashTag.post.id)).fetchJoin()
                .join(post.account).fetchJoin()
//                .leftJoin(post.tagList).fetchJoin()
                .where(post.guName.eq(gu), category(category))
//                .where(category(category))
//                .leftJoin(post.likeList)
                .orderBy(eqSort(sort), post.createdAt.desc())
                //페이징 할 때 수정해야 할것이다!
//                .orderBy(post.createdAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .distinct()
                .fetch();
//                .stream().distinct().collect(Collectors.toList());

        Long totalCount = queryFactory.select(post.id.countDistinct())
                .from(post)
//                .leftJoin(hashTag).on(post.id.eq(hashTag.post.id)).fetchJoin()
                .where(post.guName.eq(gu), category(category))
//                .where(category(category))
//                .leftJoin(post.likeList)
                //페이징 할 때 수정해야 할것이다!
//                .orderBy(post.createdAt.desc())
                .fetchOne();

        return new PageImpl<>(postList, pageable, totalCount);
    }

    // 검색
    public PageImpl<Post> searchByTag(Integer type, String searchWord, String sort, Pageable pageable) {
        List<Post> postList = queryFactory
                .select(post)
                .from(post)
                .join(post.account).fetchJoin()
                .leftJoin(hashTag).on(post.id.eq(hashTag.post.id))
//                .leftJoin(post.tagList).fetchJoin()
//                .leftJoin(hashTag).on(post.id.eq(hashTag.post.id))
                .where(tagOrNot(type, searchWord))
                .orderBy(eqSort(sort), post.createdAt.desc())
//                .distinct()
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .distinct().fetch();
//                .stream().distinct().collect(Collectors.toList());

            Long totalCount = queryFactory.select(post.id.countDistinct())
                    .from(post)
//                .leftJoin(post.tagList).fetchJoin()
                    .leftJoin(hashTag).on(post.id.eq(hashTag.post.id))
//                .leftJoin(hashTag).on(post.id.eq(hashTag.post.id))
                    .where(tagOrNot(type, searchWord))
                    .fetchOne();

        return new PageImpl<>(postList, pageable, totalCount);
    }



    //    sort sort 별 구문
    private OrderSpecifier eqSort(String sort) {
        if(sort.equalsIgnoreCase("new")) {
            return post.createdAt.desc();
            //나중에 생각해보자
        } else {
            return post.likeCount.desc();
        }
    }

//    public OrderSpecifier<?> listSort(String sort) {
//        if ("최신순".equals(sort)) {
//            return new OrderSpecifier<>(Order.DESC, post.createdAt);
//        }
//        return new OrderSpecifier<>(Order.DESC, post.likeCount);
//    }

    private OrderByNull eqSort2(String sort) {
        if(sort.equalsIgnoreCase("new")) {
            return OrderByNull.DEFAULT;
            //나중에 생각해보자
        } else if (sort.equalsIgnoreCase("hot")) {
            return (OrderByNull)post.likeCount.desc();
        } else {
            throw new CustomException(ErrorCode.BadRequest);
        }
    }

    private BooleanExpression tagOrNot(Integer type, String searchWord) {
        if(type == 0){
            return post.content.contains(searchWord).or(hashTag.tag.contains(searchWord));
        }else if (type == 1) {
            return hashTag.tag.contains(searchWord);
//                 post.tagList.getElementType().
        }else{
//            return post.tagList.equals(hashTag.tag.contains(searchWord));
            throw new CustomException(ErrorCode.BadRequest);
        }
    }

    private BooleanExpression category(String category){
        if (category.equalsIgnoreCase("all")){
            return null;
        }else {
            return post.category.eq(category);
        }
    }

}
