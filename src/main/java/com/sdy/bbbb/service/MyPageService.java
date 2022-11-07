package com.sdy.bbbb.service;

import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.dto.response.LikeResponseDto;
import com.sdy.bbbb.dto.response.PostResponseDto;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Image;
import com.sdy.bbbb.entity.Like;
import com.sdy.bbbb.entity.Post;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.repository.CommentRepository;
import com.sdy.bbbb.repository.LikeRepository;
import com.sdy.bbbb.repository.MyPageRepository;
import com.sdy.bbbb.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MyPageRepository myPageRepository;

    private final PostRepository postRepository;

    private final LikeRepository likeRepository;

    private final CommentRepository commentRepository;

    // 내 게시글에 달린 댓글 알람 기능
    @Transactional(readOnly = true)
    public GlobalResponseDto<?> showAlarm(Account account) {
        //내가 쓴 게시글 조회
        List<Post> myPostsList = postRepository.findPostsByAccount(account);

    }

    // 알람체크
    public GlobalResponseDto<?> checkAlarm(Long commentId, Account account) {
        commentRepository
    }

    // 내가 작성한 게시글 조회
    @Transactional(readOnly = true)
    public GlobalResponseDto<?> getMyPosts(Account account) {
        List<Post> myPosts = postRepository.findPostsByAccount_Id(account.getId());
        List<PostResponseDto> postResponseDtos = new ArrayList<>();

        for(Post foundPost : myPosts) {
            postResponseDtos.add(new PostResponseDto(foundPost, account));
        }
        return GlobalResponseDto.ok("Success get", postResponseDtos);
    }

    // 내가 좋아요한 게시글 조회
    @Transactional(readOnly = true)
    public GlobalResponseDto<?> getMyLikes(Account account) {
        List<Like> myLikes = likeRepository.findByAccount_Id(account.getId());
        List<LikeResponseDto> likeResponseDtos = new ArrayList<>();
        for(Like like : myLikes) {
            likeResponseDtos.add(new LikeResponseDto(like));
        }
        return GlobalResponseDto.ok("Success get", likeResponseDtos);
    }


}
