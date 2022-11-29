package com.sdy.bbbb.service;

import com.sdy.bbbb.dto.request.UpdateRequestDto;
import com.sdy.bbbb.dto.response.*;
import com.sdy.bbbb.entity.*;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import com.sdy.bbbb.repository.querydsl.CommentRepositoryImpl;
import com.sdy.bbbb.repository.*;
import com.sdy.bbbb.s3.S3Uploader;
import com.sdy.bbbb.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final BookmarkRepository bookmarkRepository;
    private final S3Uploader s3Uploader;
    private final CommentRepositoryImpl commentRepositoryImpl;
    private final AccountRepository accountRepository;



    // 내 게시글에 달린 댓글 알람 기능
    @Transactional(readOnly = true)
    public GlobalResponseDto<List<AlarmResponseDto>> showAlarm(Account account) {
        List<Comment> commentList = commentRepositoryImpl.searchCommentsInMyPosts(account);

        List<AlarmResponseDto> alarmResponseDtos = new ArrayList<>();

        for (Comment comment : commentList) {
            if (!comment.getAccount().getId().equals(account.getId())) {
                alarmResponseDtos.add(new AlarmResponseDto(comment));
            }
        }
        return GlobalResponseDto.ok("조회 성공!", alarmResponseDtos);
    }

    // 알람체크
    @Transactional
    public GlobalResponseDto<CommentResponseDto> checkAlarm(Long commentId, Account account) {
        Comment comment = commentRepository.findCommentById(commentId).orElseThrow(
                () -> new CustomException(ErrorCode.NotFoundComment)
        );
        Post post = comment.getPost();
        List<Comment> commentList = post.getCommentList();
        for(Comment comment1 : commentList) {
            if (!comment1.isChecked()) {
                comment1.setChecked(true);
            } else {
                throw new CustomException(ErrorCode.AlreadyCheckAlarm);
            }
        }
        return GlobalResponseDto.ok("알람 확인!", new CommentResponseDto(comment));
    }

    // 내가 작성한 게시글 조회
    @Transactional(readOnly = true)
    public GlobalResponseDto<List<PostResponseDto>> getMyPosts(Account account) {
        List<Post> myPosts = postRepository.findPostsByAccount_IdOrderByCreatedAtDesc(account.getId());
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for (Post foundPost : myPosts) {
            postResponseDtos.add(new PostResponseDto(foundPost, ServiceUtil.getImgUrl(foundPost), ServiceUtil.getTag(foundPost), amILiked(foundPost, account)));
        }
        return GlobalResponseDto.ok("조회 성공!", postResponseDtos);
    }

    // 내가 좋아요한 게시글 조회
    @Transactional(readOnly = true)
    public GlobalResponseDto<List<PostResponseDto>> getMyLikes(Account account) {
        List<Like> myLikes = likeRepository.findLikesByAccount_idAndLikeLevelOrderByIdDesc(account.getId(), 1);
        List<Post> likedPost = new ArrayList<>();
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for (Like like : myLikes) {
            likedPost.add(like.getPost());
        }
        for (Post post : likedPost) {
            postResponseDtos.add(new PostResponseDto(post, ServiceUtil.getImgUrl(post), ServiceUtil.getTag(post), amILiked(post, account)));
        }
        return GlobalResponseDto.ok("조회 성공!", postResponseDtos);

    }

    // 내가 누른 북마크 목록
    @Transactional(readOnly = true)
    public GlobalResponseDto<List<BookmarkResponseDto>> getMyBookmarks(Account account) {

        List<Bookmark> myBooks = bookmarkRepository.findBookmarkByAccount_IdOrderByBookmarked(account.getId());
        List<BookmarkResponseDto> bookmarkResponseDtos = new ArrayList<>();
        for (Bookmark bookmark : myBooks) {
            bookmarkResponseDtos.add(
                    new BookmarkResponseDto(bookmark)
            );
        }
        return GlobalResponseDto.ok("조회 성공!", bookmarkResponseDtos);
    }

    // 마이페이지 수정 (프로필 이미지 사진 수정, 닉네임 수정)
    @Transactional
    public GlobalResponseDto<LoginResponseDto> updateMyInfo(Account account, UpdateRequestDto updateRequestDto, MultipartFile multipartFile) {
        Account account1 = accountRepository.findById(account.getId()).orElseThrow(
                ()-> new CustomException(ErrorCode.NotFoundUser)
        );
        if (multipartFile != null){
            account1.setProfileImage(s3Uploader.upload(multipartFile, "dir1"));
        }

         if (updateRequestDto != null) {
             if(!Objects.equals(updateRequestDto.getNickname(), "") && !Objects.equals(updateRequestDto.getNickname(), null)){
                 account1.setAccountName(updateRequestDto.getNickname());
             }
         }

        return GlobalResponseDto.ok("수정완료", new LoginResponseDto(account1));
    }

    // 닉네임 중복 확인
    public GlobalResponseDto<UpdateRequestDto> checkNickname(Account account, UpdateRequestDto updateRequestDto) {
        Account account1 = accountRepository.findById(account.getId()).orElseThrow(()-> new CustomException(ErrorCode.NotFoundUser));
        if(accountRepository.existsAccountByAccountName(updateRequestDto.getNickname())) {
            return GlobalResponseDto.fail("이미 존재하는 닉네임입니다.");
        } else {
            return GlobalResponseDto.ok("사용가능한 닉네임입니다!", updateRequestDto);
        }
    }

    // 좋아요 했는지 안했는지 확인하는 함수
    public boolean amILiked(Post post, Account currentAccount) {
        return likeRepository.existsByPostAndAccount(post, currentAccount);
    }


}
