package com.sdy.bbbb.service;

import com.sdy.bbbb.SSE.AlarmType;
import com.sdy.bbbb.SSE.SseService;
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
import com.sdy.bbbb.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final SseService sseService;


    // 댓글 작성
    @Transactional
    public GlobalResponseDto<CommentResponseDto> createComment(Long postId, CommentRequestDto requestDto, Account account){
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.NotFoundPost));
        // 게시글 없으면 예외 처리
        Comment comment = new Comment(requestDto, post, account);
        // request받은 댓글, 게시글, 유저정보
        commentRepository.save(comment);
        // 댓글 저장
        post.setCommentCount(post.getCommentCount() +1);
        // 게시글에 달린 댓글 수 변경
        postRepository.save(post);
        // 게시글 저장
        CommentResponseDto responseDto = new CommentResponseDto(comment);
        if(!Objects.equals(post.getAccount().getId(), account.getId())) {
            String message = comment.getPost().getContent().length() > 10 ? comment.getPost().getContent().substring(0 ,9) + "..." : comment.getPost().getContent();
            sseService.send(post.getAccount(), AlarmType.eventPostComment, message + " 게시글에 댓글이 달렸습니다.", "postId: " + post.getId());
        }
        return GlobalResponseDto.created("댓글이 생성되었습니다", responseDto);
    }
  
    @Transactional
    public GlobalResponseDto<String> deleteComment(Long id, Account account) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.NotFoundComment));
        // 댓글 없으면 예외처리 (댓글을 찾을 수 없습니다)
        Post post = comment.getPost();
        // 댓글 작성자 아니면 에러코드 (작성자가 아닙니다)
        ServiceUtil.checkCommentAuthor(comment, account);
        commentRepository.delete(comment);
        // 댓글 삭제
        post.setCommentCount(post.getCommentCount() -1);
        // 게시글에 달린 댓글 수 변경
        postRepository.save(post);
        // 게시글 저장
        return GlobalResponseDto.ok("댓글이 삭제되었습니다.", null);
    }
}
