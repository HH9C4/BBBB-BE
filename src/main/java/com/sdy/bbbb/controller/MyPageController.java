//package com.sdy.bbbb.controller;
//
//
//import com.sdy.bbbb.config.UserDetailsImpl;
//import com.sdy.bbbb.dto.response.GlobalResponseDto;
//import com.sdy.bbbb.entity.Account;
//import com.sdy.bbbb.service.MyPageService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("api/mypage")
//public class MyPageController {
//
//    private final MyPageService myPageService;
//
//    // 알람 기능
//    @GetMapping("/alarm")
//    public GlobalResponseDto<?> showAlarm(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        Account account = userDetails.getAccount();
//        return myPageService.showAlarm(account);
//    }
//
//    // 알람 확인 기능
////    @PostMapping("/alarm/{commentId}")
////    public GlobalResponseDto<?> checkAlarm(@PathVariable Long commentId,
////                                           @AuthenticationPrincipal UserDetailsImpl userDetails){
////
////
////    }
////
////    // 내가 작성한 글
////    @GetMapping("/")
//
//    //내가 좋아요한 글
//}
