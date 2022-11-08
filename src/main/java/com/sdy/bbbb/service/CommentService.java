package com.sdy.bbbb.service;

import com.sdy.bbbb.dto.request.CommentRequestDto;
import com.sdy.bbbb.dto.response.CommentResponseDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Comment;
import com.sdy.bbbb.entity.Post;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import com.sdy.bbbb.repository.CommentRepository;
import com.sdy.bbbb.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public GlobalResponseDto createComment(Long postId, CommentRequestDto requestDto, Account account){
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.NotFound));
        // 예외 코드 아직 안만듬 -> 게시글 없으면 예외 처리 (게시글을 찾을 수 없습니다)
        Comment comment = new Comment(requestDto, post, account);
        // request받은 댓글, 게시글, 유저정보
        commentRepository.save(comment);
        // 댓글 저장
        post.getCommentList().add(comment); // getter 최대한 안쓰기 -> 메소드 만들기
        // 34번줄 성우님과 상의 (게시글 GET 메소드시 업데이트 or 댓글 생성시 자동 업데이트 -> 댓글 생성시 업데이트 댓글 카운트도 생각)
        return GlobalResponseDto.created("댓글이 생성되었습니다");
    }
    @Transactional
    public GlobalResponseDto deleteComment(Long id, Account account) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.NotFound));
        // 예외코드 아직 안만듬 -> 댓글 없으면 예외처리 (댓글을 찾을 수 없습니다)
        if (!comment.getAccount().getId().equals(account.getId())) {
            throw new CustomException(ErrorCode.NotFound);
        }// 댓글 작성자 아니면 에러코드 (작성자가 아닙니다)
        commentRepository.delete(comment);
        // 댓글 삭제
        return GlobalResponseDto.ok("댓글이 삭제되었습니다.", null);
    }
}
