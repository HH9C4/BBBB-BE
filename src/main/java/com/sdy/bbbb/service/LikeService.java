package com.sdy.bbbb.service;

import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.dto.response.LikeResponseDto;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Comment;
import com.sdy.bbbb.entity.Like;
import com.sdy.bbbb.entity.Post;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import com.sdy.bbbb.repository.CommentRepository;
import com.sdy.bbbb.repository.LikeRepository;
import com.sdy.bbbb.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public GlobalResponseDto<LikeResponseDto> like(Long id, Integer level, Account account) {
        // level이 1이면 게시글 좋아요 생성
        if (level == 1) {
            Post post = postRepository.findById(id).orElseThrow(
                    () -> new CustomException(ErrorCode.NotFoundPost));
            // 게시글 없으면 에러처리
//            Optional<Like> like1 = likeRepository.findByPostAndAccount(post, account);
            if (likeRepository.existsByPostAndAccount(post, account)) {
                likeRepository.deleteByPostAndAccount(post, account);
                post.setLikeCount(post.getLikeCount() - 1);
                return GlobalResponseDto.ok("좋아요 취소", new LikeResponseDto(amILiked(post, account), post.getLikeCount()));
            } else {
                Like like = new Like(post, level, account);
                // 좋아요 생성
                likeRepository.save(like);
                // 좋아요 저장
//                post.setViews(post.getViews() -1);
//                // 좋아요 시 조회수 보정
                post.setLikeCount(post.getLikeCount() + 1);
                // 게시글 좋아요 수 변경
                return GlobalResponseDto.created("success Likes!", new LikeResponseDto(amILiked(post, account), post.getLikeCount()));
            }

        }else { // level이 1이 아니면 댓글 좋아요 생성
            Comment comment = commentRepository.findById(id).orElseThrow(
                    () -> new CustomException(ErrorCode.NotFoundComment));
            // 댓글 없으면 에러처리
            if (likeRepository.existsByCommentAndAccount(comment, account)) {
                likeRepository.deleteByCommentAndAccount(comment, account);
                comment.setLikeCount(comment.getLikeCount() - 1);
                // 좋아요 정보가 있는 상태 = 좋아요 취소
                return GlobalResponseDto.ok("좋아요 취소", new LikeResponseDto(myLikedComment(comment, account), comment.getLikeCount()));
            } else {
                Like like = new Like(comment, level, account);
                // 좋아요 생성
                likeRepository.save(like);
                // 좋아요 저장
                comment.setLikeCount(comment.getLikeCount() + 1);
                // 댓글 좋아요 수 변경
                return GlobalResponseDto.created("success Likes!", new LikeResponseDto(myLikedComment(comment, account), comment.getLikeCount()));
            }
        }
    }

    @Transactional
    public GlobalResponseDto createPostLike(Long id, Integer level, Account account) {
        // level이 1이면 게시글 좋아요 생성
        if (level == 1) {
            Post post = postRepository.findById(id).orElseThrow(
                    () -> new CustomException(ErrorCode.NotFoundPost));
            // 게시글 없으면 에러처리
            if (likeRepository.existsByPostAndAccount(post, account)) {
                throw new CustomException(ErrorCode.AlreadyExistsLike);
                // 좋아요 정보가 있는 상태 예외 처리
            } else {
                Like like = new Like(post, level, account);
                // 좋아요 생성
                likeRepository.save(like);
                // 좋아요 저장
//                post.setViews(post.getViews() -1);
//                // 좋아요 시 조회수 보정
                post.setLikeCount(post.getLikeCount() + 1);
                // 게시글 좋아요 수 변경
            }
            return GlobalResponseDto.created("success Likes!", new LikeResponseDto(amILiked(post, account), post.getLikeCount()));
        } // level이 1이 아니면 댓글 좋아요 생성
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.NotFoundPost));
        // 댓글 없으면 에러처리
        if (likeRepository.existsByCommentAndAccount(comment, account)) {
            throw new CustomException(ErrorCode.AlreadyExistsLike);
            // 좋아요 정보가 있는 상태 예외 처리
        } else {
            Like like = new Like(comment, level, account);
            // 좋아요 생성
            likeRepository.save(like);
            // 좋아요 저장
            comment.setLikeCount(comment.getLikeCount() + 1);
            // 댓글 좋아요 수 변경
        }
        return GlobalResponseDto.created("success Likes!", new LikeResponseDto(myLikedComment(comment, account), comment.getLikeCount()));
    }

    @Transactional
    public GlobalResponseDto<LikeResponseDto> deletePostLike(Long id, Integer level, Account account) {
        // level이 1이면 게시글 좋아요 삭제
        if (level == 1) {
            Post post = postRepository.findById(id).orElseThrow(
                    () -> new CustomException(ErrorCode.NotFoundPost));
            // 게시글 없으면 예외처리
            Like like = likeRepository.findByPostAndAccount(post, account).orElseThrow(
                    () -> new CustomException(ErrorCode.AlreadyCancelLike));
            // 좋아요 정보 없으면 예외처리
            likeRepository.delete(like);
            // 좋아요 삭제
            post.setLikeCount(post.getLikeCount() - 1);
//            // 조회수 보정
//            post.setViews(post.getViews() -1);
            // 게시글 좋아요 수 변경
            return GlobalResponseDto.ok("delete Likes!", new LikeResponseDto(amILiked(post, account), post.getLikeCount()));
        } // level이 1이 아니면 댓글 좋아요 삭제
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.NotFoundPost));
        // 댓글 없으면 예외처리
        Like like = likeRepository.findByCommentAndAccount(comment, account).orElseThrow(
                () -> new CustomException(ErrorCode.AlreadyCancelLike));
        // 좋아요 정보 없으면 예외처리
        likeRepository.delete(like);
        // 좋아요 삭제
        comment.setLikeCount(comment.getLikeCount() - 1);
        // 댓글 좋아요 수 변경
        return GlobalResponseDto.ok("delete Likes!", new LikeResponseDto(myLikedComment(comment, account), comment.getLikeCount()));
    }

    //좋아요 여부
    public boolean amILiked(Post post, Account currentAccount) {
        return likeRepository.existsByPostAndAccount(post, currentAccount);
    }

    public boolean myLikedComment(Comment comment, Account currentAccount) {
        return likeRepository.existsByCommentAndAccount(comment, currentAccount);
    }
}
