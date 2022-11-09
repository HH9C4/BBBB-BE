package com.sdy.bbbb.service;

import com.sdy.bbbb.dto.request.PostRequestDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.dto.response.OnePostResponseDto;
import com.sdy.bbbb.dto.response.PostResponseDto;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Image;
import com.sdy.bbbb.entity.Post;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import com.sdy.bbbb.repository.ImageRepository;
import com.sdy.bbbb.repository.LikeRepository;
import com.sdy.bbbb.repository.PostRepository;
import com.sdy.bbbb.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final LikeRepository likeRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    //게시글 생성
    public GlobalResponseDto<String> createPost(PostRequestDto postRequestDto, List<MultipartFile> multipartFile, Account currentAccount) throws IOException {
        Post post = new Post(postRequestDto, currentAccount);
        //쿼리 두번 보다 한번으로 하는게 낫겠쥐?
        postRepository.save(post);

//        System.out.println(multipartFile.get(0).toString());
        //이미지 있다면
        createImageIfNotNull(multipartFile, post);

//        if (postRequestDto.getImageUrl() != null) {
//            List<String> imgUrlList = postRequestDto.getImageUrl();
//            List<Image> imageList = new ArrayList<>();
//            for (String imgUrl : imgUrlList) {
//                Image image = new Image(post, imgUrl);
//                imageList.add(image);
//                imageRepository.save(image);
//            }
//            //굳이 set 해줘야하나??
//            post.setImageList(imageList);
//        }

        return GlobalResponseDto.created("게시글이 등록 되었습니다.");
    }

    //게시글 전체 조회
    @Transactional(readOnly = true)
    public GlobalResponseDto<List<PostResponseDto>> getPost(String gu, String sort, Account currentAccount) {
        List<Post> postList;
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        if (sort.equals("new")) {
            postList = postRepository.findPostsByGuOrderByCreatedAtDesc(gu);
        } else if (sort.equals("hot")) {
            postList = postRepository.findPostsByGuOrderByLikeCountDescCreatedAtDesc(gu);
        } else {
            throw new CustomException(ErrorCode.NotFoundSort);//잘못된 요청
        }

        for (Post post : postList) {
            //좋아요 확인

            postResponseDtoList.add(new PostResponseDto(post, currentAccount, getImgUrl(post), amILiked(post, currentAccount)));
        }
        return GlobalResponseDto.ok("조회 성공", postResponseDtoList);
    }

//    게시글 검색
    @Transactional(readOnly = true)
    public GlobalResponseDto<List<PostResponseDto>> searchPost(String searchWord, String sort, Account currentAccount) {
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
            postResponseDtoList.add(new PostResponseDto(post, currentAccount, getImgUrl(post), amILiked(post, currentAccount)));
        }
        return GlobalResponseDto.ok("조회 성공", postResponseDtoList);
    }

    //게시글 상세 조회
    @Transactional
    public GlobalResponseDto<OnePostResponseDto> getOnePost(Long postId, Account currentAccount) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.NotFoundPost));

        post.setViews(post.getViews() + 1);
        //이미지 추출 함수로, DTO에 있는게 나을까?

        return GlobalResponseDto.ok("조회 성공", new OnePostResponseDto(post, currentAccount, getImgUrl(post), amILiked(post, currentAccount)));
    }

    //게시글 수정
    @Transactional
    public GlobalResponseDto<String> updatePost(Long postId, PostRequestDto postRequestDto, List<MultipartFile> multipartFile, Account currentAccount) throws IOException{
        //어차피 쓸거 일단 찾아
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.NotFoundPost));
        //작성자 일치여부 확인
        checkPostAuthor(post, currentAccount);

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

//        if (postRequestDto.getImageUrl() != null) {
//            List<String> imageUrlList = postRequestDto.getImageUrl();
//            for (String imageUrl : imageUrlList) {
//                if (!(imageRepository.existsByImageUrl(imageUrl))) {
//                    Image image = new Image(post, imageUrl);
//                    //테스트시 꼼꼼히 보자
//                    post.getImageList().add(image);
//                    imageRepository.save(image);
//                } else {
//                    throw new CustomException(ErrorCode.AlreadyExists);
//                }
//            }
//        }

        post.update(postRequestDto);
        return GlobalResponseDto.ok("게시글 수정이 완료되었습니다.", null);
    }

    //게시글 삭제
    @Transactional
    public GlobalResponseDto<String> deletePost(Long postId, Account currentAccount) {
        //어차피 쓸거 일단 찾아
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.NotFoundPost));
        //작성자 일치여부 확인
        checkPostAuthor(post, currentAccount);

        postRepository.delete(post);
        return GlobalResponseDto.ok("게시글 삭제가 완료되었습니다.", null);
    }


    //등록 할 이미지가 있다면 사용
    public void createImageIfNotNull(List<MultipartFile> multipartFile, Post post) throws IOException {
        if (multipartFile != null && multipartFile.size() != 0){
            List<Image> imageList = new ArrayList<>();
            for (MultipartFile imgFile : multipartFile) {
                Image image = new Image(post, s3Uploader.uploadFiles(imgFile, "dir1"));
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
    public void checkPostAuthor(Post post, Account currentAccount) {
        if (!post.getAccount().getId().equals(currentAccount.getId())){
            throw new CustomException(ErrorCode.NotMatchAuthor);
        }
    }

    //좋아요 여부
    public boolean amILiked(Post post, Account currentAccount) {
        return likeRepository.existsByPostAndAccount(post, currentAccount);
    }


}
