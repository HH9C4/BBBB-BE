package com.sdy.bbbb.service;

import com.sdy.bbbb.dto.request.UpdateRequestDto;
import com.sdy.bbbb.dto.response.*;
import com.sdy.bbbb.entity.*;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import com.sdy.bbbb.repository.*;
import com.sdy.bbbb.s3.S3Uploader2;
import com.sdy.bbbb.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final BookmarkRepository bookmarkRepository;
    private final S3Uploader2 s3Uploader2;



    // 내 게시글에 달린 댓글 알람 기능
    @Transactional(readOnly = true)
    public GlobalResponseDto<List<AlarmResponseDto>> showAlarm(Account account) {
        //내가 쓴 게시글 조회
        List<Post> myPosts = postRepository.findPostsByAccount_IdOrderByCreatedAtDesc(account.getId());
        List<Comment> postsComment = new ArrayList<>();
        List<AlarmResponseDto> alarmResponseDtos = new ArrayList<>();
        for (Post post : myPosts) {
            postsComment.addAll(post.getCommentList());
        }
        for (Comment comment : postsComment) {
            alarmResponseDtos.add(new AlarmResponseDto(comment));
        }
        return GlobalResponseDto.ok("조회 성공!", alarmResponseDtos);
    }

    // 알람체크
    @Transactional
    public GlobalResponseDto<CommentResponseDto> checkAlarm(Long commentId, Account account) {
        Comment comment = commentRepository.findCommentById(commentId).orElseThrow(
                () -> new CustomException(ErrorCode.NotFoundComment)
        );
        ServiceUtil.checkCommentAuthor(comment, account);
        if (!comment.isChecked()) {
            comment.setChecked(true);
        } else {
            throw new CustomException(ErrorCode.AlreadyCheckAlarm);
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
        if (multipartFile != null){
            account.setProfileImage(s3Uploader2.upload(multipartFile, "dir1"));
        }
        if(updateRequestDto != null){
            account.setAccountName(updateRequestDto.getNickname());
        }
        return GlobalResponseDto.ok("수정완료", new LoginResponseDto(account));
    }

    // 좋아요 했는지 안했는지 확인하는 함수
    public boolean amILiked(Post post, Account currentAccount) {
        return likeRepository.existsByPostAndAccount(post, currentAccount);
    }





}
