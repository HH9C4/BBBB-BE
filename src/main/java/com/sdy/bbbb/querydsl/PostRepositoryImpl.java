package com.sdy.bbbb.querydsl;

import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.HashTag;
import com.sdy.bbbb.entity.Post;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.sdy.bbbb.entity.QComment.comment1;
import static com.sdy.bbbb.entity.QHashTag.hashTag;
import static com.sdy.bbbb.entity.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl {


    private final JPAQueryFactory queryFactory;

//    /**
//     * Creates a new {@link QuerydslRepositorySupport} instance for the given domain type.
//     *
//     * @param domainClass must not be {@literal null}.
//     */
//    public PostRepositoryImpl(JPAQueryFactory queryFactory) {
//        super(Post.class);
//        this.queryFactory = queryFactory;
//    }
//    public EventPostRepositoryImpl(JPAQueryFactory queryFactory, EventPostLikeRepository eventPostLikeRepository) {
//        super(EventPost.class);
//        this.queryFactory = queryFactory;
//        this.eventPostLikeRepository = eventPostLikeRepository;
//    }


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
                .select(post).distinct()
//                .distinct()
                .from(post)
//                .leftJoin(hashTag).on(post.id.eq(hashTag.post.id)).fetchJoin()
                .join(post.account).fetchJoin()
                .leftJoin(post.tagList).fetchJoin()
                .where(post.guName.eq(gu), category(category))
//                .where(category(category))
//                .leftJoin(post.likeList)
                .orderBy(eqSort(sort), post.createdAt.desc())
                //페이징 할 때 수정해야 할것이다!
//                .orderBy(post.createdAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
//                .stream().distinct().collect(Collectors.toList());

        Long totalCount = queryFactory.select(Wildcard.count).distinct()
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
                .select(post).distinct()
                .from(post)
//                .leftJoin(post.tagList).fetchJoin()
                .leftJoin(hashTag).on(post.id.eq(hashTag.post.id)).fetchJoin()
//                .leftJoin(hashTag).on(post.id.eq(hashTag.post.id))
                .where(tagOrNot(type, searchWord))
                .orderBy(eqSort(sort), post.createdAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
//                .stream().distinct().collect(Collectors.toList());

            Long totalCount = queryFactory.select(Wildcard.count).distinct()
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
        }else{
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

//    private PageImpl toPage(List<Post> postList, Pageable pageable, Account account) {
//        List<Post> eventPostAllResDtos = new ArrayList<>();
//        for (Post post : postList) {
//            boolean bookmark;
//            if (member != null) {
//                bookmark = eventPostLikeRepository.existsByMemberAndEventPost(member, eventPost);
//            } else {
//                bookmark = false;
//            }
//
//            eventPostAllResDtos.add(EventPostAllResDto.toEPARD(eventPost, bookmark));
//        }
//        return new PageImpl<>(eventPostAllResDtos, pageable, eventPostList.size());


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
