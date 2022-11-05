package com.sdy.bbbb.service;

import com.sdy.bbbb.dto.request.CommentRequestDto;
import com.sdy.bbbb.dto.response.CommentResponseDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Transactional
    public GlobalResponseDto<CommentResponseDto> createComment(CommentRequestDto requestDto, Long id){
        // 유저 아이디도 가져와야함
        return GlobalResponseDto.created("댓글이 생성되었습니다");
    }
}
