//package com.sdy.bbbb.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sdy.bbbb.entity.HashTag;
import com.sdy.bbbb.entity.QHashTag;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.sdy.bbbb.entity.QHashTag.hashTag;
import static com.sdy.bbbb.entity.QPost.post;


//@RequiredArgsConstructor
//public class HashTagRepositoryImpl implements HashTagRepositoryCustom {
//    private final JPAQueryFactory queryFactory;
//    private QHashTag qHashTag;

//    @Override
//    public List<HashTag> searchHotTag20() {
//        List<HashTag> result = new ArrayList<>();
//                result = queryFactory
//                .selectFrom(hashTag)
////                .join(post)
////                .on(hashTag.post.eq(post))
//                .where(post.guName.eq("강남구"))
////                .groupBy(hashTag.tag)
////                .orderBy(hashTag.tag.count().desc())
//                .fetch();
//        if(result==null){
//            return null;
//        }else {
//            return result;
//        }
//    }

//    @Override
//    public List<HashTag> searchHottest(String guName) {
//        List<HashTag> result = queryFactory
//                .selectFrom(hashTag)
//                .join(post)
//                .on(post.id.eq(hashTag.post.id))
//                .groupBy(post.guName)
//                .fetch();
//
//
//    }
//}
