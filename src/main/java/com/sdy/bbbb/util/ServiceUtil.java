package com.sdy.bbbb.util;

import com.sdy.bbbb.entity.*;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class ServiceUtil {
    //작성자 일치 여부 확인

    //Post 에서 이미지 url 추출
    public static List<String> getImgUrl(Post post) {
        List<String> imageUrl = new ArrayList<>();
        for (Image img : post.getImageList()) {
            imageUrl.add(img.getImageUrl());
        }
        return imageUrl;
    }

    //Post 에서 Tag 추출
    public static List<String> getTag(Post post) {
        List<String> tagList = new ArrayList<>();
        for (HashTag hashTag : post.getTagList()) {
            tagList.add(hashTag.getTag());
        }
        return tagList;
    }


    //좋아요 여부 (post)
    public static boolean amILikedPost(Post post, List<Like> likeList) {
        //한번에 가져오고 엔티티로 찾는다?
        for (Like like : likeList){
            System.out.println("포문돈다 post");
            if (like.getPost() != null && like.getPost().getId().equals(post.getId())){
                return true;
            }
        }
        System.out.println("다돌았다 post");
        return false;
    }

    //좋아요 여부 (comment)
    public static boolean amILikedComment(Comment comment, List<Like> likeList) {
        for (Like like : likeList){
            System.out.println("포문돈다 comment");
            if (like.getComment() != null && like.getComment().getId().equals(comment.getId())){
                return true;
            }
        }
        System.out.println("다돌았다 comment");
        return false;
    }

    //작성자 확인(Post)
    public static void checkPostAuthor(Post post, Account account) {
        if (!post.getAccount().getId().equals(account.getId())){
            throw new CustomException(ErrorCode.NotMatchAuthor);
        }
    }

    //작성자 확인(Comment)
    public static void checkCommentAuthor(Comment comment, Account account) {
        if (!comment.getAccount().getId().equals(account.getId())){
            throw new CustomException(ErrorCode.NotMatchAuthor);
        }
    }

    // 디코딩
    public static String decoding(String toDecode) {
        String result = "";
        try {
            result = URLDecoder.decode(toDecode, "UTF-8");
        }catch (UnsupportedEncodingException e){
            throw new CustomException(ErrorCode.FailDecodeString);
        }
        return result;
    }



}