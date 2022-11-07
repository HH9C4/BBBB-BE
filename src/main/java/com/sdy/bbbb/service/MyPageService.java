//package com.sdy.bbbb.service;
//
//import com.sdy.bbbb.dto.response.GlobalResponseDto;
//import com.sdy.bbbb.dto.response.PostResponseDto;
//import com.sdy.bbbb.entity.Account;
//import com.sdy.bbbb.entity.Image;
//import com.sdy.bbbb.entity.Post;
//import com.sdy.bbbb.repository.MyPageRepository;
//import com.sdy.bbbb.repository.PostRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class MyPageService {
//
//    private final MyPageRepository myPageRepository;
//
//    private final PostRepository postRepository;
//
//    // 내 게시글에 달린 댓글 알람 기능
//    @Transactional(readOnly = true)
//    public GlobalResponseDto<?> showAlarm(Account account) {
//        //내가 쓴 게시글 조회
//        List<Post> myPostsList = postRepository.findPostsByAccount(account);
//
//    }
//}
