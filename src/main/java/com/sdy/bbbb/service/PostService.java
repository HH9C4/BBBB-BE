package com.sdy.bbbb.service;

import com.sdy.bbbb.dto.request.PostRequestDto;
import com.sdy.bbbb.dto.response.CommentResponseDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.dto.response.OnePostResponseDto;
import com.sdy.bbbb.dto.response.PostResponseDto;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Comment;
import com.sdy.bbbb.entity.Image;
import com.sdy.bbbb.entity.Post;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import com.sdy.bbbb.querydsl.PostRepositoryImpl;
import com.sdy.bbbb.repository.ImageRepository;
import com.sdy.bbbb.repository.LikeRepository;
import com.sdy.bbbb.repository.PostRepository;
import com.sdy.bbbb.s3.S3Uploader2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final LikeRepository likeRepository;
    private final S3Uploader2 s3Uploader2;

    @Transactional
    //게시글 생성
    public GlobalResponseDto<PostResponseDto> createPost(PostRequestDto postRequestDto,
                                                         List<MultipartFile> multipartFile,
                                                         Account account) {
        Post post = new Post(postRequestDto, account);
        //쿼리 두번 보다 한번으로 하는게 낫겠쥐?
        postRepository.save(post);

        //이미지 있다면
        createImageIfNotNull(multipartFile, post);

        return GlobalResponseDto.created("게시글이 등록 되었습니다.",
                new PostResponseDto(post, getImgUrl(post), false));
    }

    //게시글 전체 조회
    @Transactional(readOnly = true)
    public GlobalResponseDto<List<PostResponseDto>> getPost(String gu,
                                                            String sort,
                                                            Account account) {
        //구를 디비에서 찾아서 올바르게 들어왔는지 검사하는 로직이 필요할까?
        gu = decoding(gu);
        List<Post> postList;
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        if (sort.equals("new")) {
//            postList = List.copyOf(postRepository.findPostsByGuNameOrderByCreatedAtDesc(gu));
            postList = List.copyOf(Set.copyOf(postRepository.searchPostsByGuName(gu)));
        } else if (sort.equals("hot")) {
            postList = postRepository.findPostsByGuNameOrderByLikeCountDescCreatedAtDesc(gu);
        } else {
            throw new CustomException(ErrorCode.NotFoundSort);//잘못된 요청
        }

        for (Post post : postList) {
            //좋아요 확인

            postResponseDtoList.add(
                    new PostResponseDto(post, getImgUrl(post), amILiked(post, account)));
        }
        return GlobalResponseDto.ok("조회 성공", postResponseDtoList);
    }

//    게시글 검색
    @Transactional(readOnly = true)
    public GlobalResponseDto<List<PostResponseDto>> searchPost(String searchWord,
                                                               String sort,
                                                               Account account) {
        searchWord = decoding(searchWord);
        List<Post> postList;
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        if (sort.equals("new")) {
            postList = postRepository.findPostsByTagContainsAndContentContainsOrderByCreatedAtDesc(searchWord, searchWord);
        } else if (sort.equals("hot")) {
            postList = postRepository.findPostsByTagContainsAndContentContainsOrderByLikeCountDescCreatedAtDesc(searchWord, searchWord);
        } else {
            throw new CustomException(ErrorCode.NotFoundSort);//잘못된 요청
        }

        for (Post post : postList) {
            //좋아요 확인
            postResponseDtoList.add(
                    new PostResponseDto(post, getImgUrl(post), amILiked(post, account)));
        }
        return GlobalResponseDto.ok("조회 성공", postResponseDtoList);
    }

    //게시글 상세 조회
    @Transactional
    public GlobalResponseDto<OnePostResponseDto> getOnePost(Long postId, Account account) {
//        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.NotFoundPost));
        Post post = postRepository.searchOneById(postId);

        post.setViews(post.getViews() + 1);
        //이미지 추출 함수로, DTO에 있는게 나을까?
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for(Comment comment : post.getCommentList()){
            commentResponseDtoList.add(new CommentResponseDto(comment));
        }

        return GlobalResponseDto.ok("조회 성공", new OnePostResponseDto(post, getImgUrl(post), amILiked(post, account), commentResponseDtoList));
    }

    //게시글 수정
    @Transactional
    public GlobalResponseDto<PostResponseDto> updatePost(Long postId,
                                                PostRequestDto postRequestDto,
                                                List<MultipartFile> multipartFile,
                                                Account account) {
        //어차피 쓸거 일단 찾아
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.NotFoundPost));
        //작성자 일치여부 확인
        checkPostAuthor(post, account);

        //이미지 수정
        //삭제할 이미지 있다면
        if (postRequestDto.getDeleteUrl() != null) {
            List<String> imageUrlList = postRequestDto.getDeleteUrl();
            for (String imageUrl : imageUrlList) {
                if (imageRepository.existsByImageUrl(imageUrl)) {
                    imageRepository.deleteByImageUrl(imageUrl);
                } else {
                    throw new CustomException(ErrorCode.NotFoundImage);
                }
            }
        }
        //추가할 이미지 있다면
        createImageIfNotNull(multipartFile, post);

        post.update(postRequestDto);
        return GlobalResponseDto.created("게시글 수정이 완료되었습니다.",
                new PostResponseDto(post, getImgUrl(post), false));
    }

    //게시글 삭제
    @Transactional
    public GlobalResponseDto<String> deletePost(Long postId, Account account) {
        //어차피 쓸거 일단 찾아
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.NotFoundPost));
        //작성자 일치여부 확인
        checkPostAuthor(post, account);

        postRepository.delete(post);
        return GlobalResponseDto.ok("게시글 삭제가 완료되었습니다.", null);
    }


    //등록 할 이미지가 있다면 사용
    public void createImageIfNotNull(List<MultipartFile> multipartFile, Post post) {
        if (multipartFile != null && multipartFile.size() != 0){
            List<Image> imageList = new ArrayList<>();
            for (MultipartFile imgFile : multipartFile) {
                Image image = new Image(post, s3Uploader2.upload(imgFile, "dir1"));
                imageList.add(image);
                imageRepository.save(image);
            }
            post.setImageList(imageList);
        }
    }

    //Post 의 Image 의 url (string)추출
    public List<String> getImgUrl(Post post){
        List<String> imageUrl = new ArrayList<>();
        for(Image img : post.getImageList()){
            imageUrl.add(img.getImageUrl());
        }
        return imageUrl;
    }

    //작성자 확인
    public void checkPostAuthor(Post post, Account account) {
        if (!post.getAccount().getId().equals(account.getId())){
            throw new CustomException(ErrorCode.NotMatchAuthor);
        }
    }

    //좋아요 여부
    public boolean amILiked(Post post, Account currentAccount) {
        return likeRepository.existsByPostAndAccount(post, currentAccount);
    }

    //utf-8 디코딩
    public String decoding(String toDecode) {
        String result = "";
        try {
            result = URLDecoder.decode(toDecode, "UTF-8");
        }catch (UnsupportedEncodingException e){
            throw new CustomException(ErrorCode.FailDecodeString);
        }
        return result;
    }

}
