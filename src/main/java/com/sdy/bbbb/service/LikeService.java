package com.sdy.bbbb.service;

import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Like;
import com.sdy.bbbb.entity.Post;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import com.sdy.bbbb.repository.LikeRepository;
import com.sdy.bbbb.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    @Transactional
    public GlobalResponseDto createLike(Long postId, String liketLevel, Account account){
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.NotFoundPost));
        //게시글 없으면 에러처리
        Optional<Like> foundLike = likeRepository.findByPostAndAccount(post, account);
        if (foundLike.isPresent()){
            throw new CustomException(ErrorCode.AlreadyExistsLike);
            // 좋아요 정보가 있는 상태 예외 처리 -> 예외코드 만들어야함 (혹시 몰라서 일단 예외처리)
        }else {

            Like like = new Like(post, liketLevel, account);
            likeRepository.save(like);
            // 좋아요 저장
            post.setLikeCount(post.getLikeList().size());
            // 게시글 좋아요 수 변경
            postRepository.save(post);
            // 게시글 저장
        return GlobalResponseDto.created("success Likes!");

        }
    }

    @Transactional
    public GlobalResponseDto deleteLike(Long postId, Account account){
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.NotFoundPost));
        //게시글 없으면 에러처리
        Optional<Like> foundLike = likeRepository.findByPostAndAccount(post, account);
        if (foundLike.isPresent()){
            likeRepository.delete(foundLike.get());
            // 좋아요 삭제
            post.setLikeCount(post.getLikeList().size());
            // 게시글 좋아요 수 변경
            postRepository.save(post);
            // 게시글 저장
        }else {
            throw new CustomException(ErrorCode.AlreadyCancelLike);
            // 좋아요 정보가 없는 상태 예외처리
        }
        return GlobalResponseDto.ok("delete Likes!", null);
    }
}
