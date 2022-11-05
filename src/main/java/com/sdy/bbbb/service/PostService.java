package com.sdy.bbbb.service;

import com.sdy.bbbb.dto.request.PostRequestDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.dto.response.PostResponseDto;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Image;
import com.sdy.bbbb.entity.Post;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import com.sdy.bbbb.repository.ImageRepository;
import com.sdy.bbbb.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;

    @Transactional
    //게시글 작성
    public GlobalResponseDto<String> createPost(PostRequestDto postRequestDto, Account currentAccount) {
        Post post = new Post(postRequestDto, currentAccount);
        //쿼리 두번 보다 한번으로 하는게 낫겠쥐?

        //이미지 있다면
        if (postRequestDto.getImageUrl() != null) {
            List<String> imgUrlList = postRequestDto.getImageUrl();
            List<Image> imageList = new ArrayList<>();
            for(String imgUrl : imgUrlList) {
                Image image = new Image(post,imgUrl);
                imageList.add(image);
                imageRepository.save(image);
            }
            post.setImageList(imageList);
        }

        postRepository.save(post);
        return GlobalResponseDto.created("게시글이 등록 되었습니다.");
    }

    //게시글 전체 조회
    @Transactional(readOnly = true)
    public GlobalResponseDto<List<PostResponseDto>> getPost(String gu, String sort, Account currentAccount) {
        List<Post> postList;
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        if (sort.equals("new")){
            postList = postRepository.findPostsByGuOrderByCreatedAtDesc(gu);
        }else if (sort.equals("hot")){
            postList = postRepository.findPostsByGuOrderByLikeCountDesc(gu);
        }else{
            postList = postRepository.findPostsByGuAndCategoryOrderByCreatedAt(gu, sort);
        }
        for(Post post : postList){
            List<String> imageUrl = new ArrayList<>();
            for(Image img : post.getImageList()){
                imageUrl.add(img.getImageUrl());
            }
            postResponseDtoList.add(new PostResponseDto(post, currentAccount, imageUrl, false));
        }
        return GlobalResponseDto.ok("조회 성공", postResponseDtoList);
    }

//    @Transactional(readOnly = true)
//    public GlobalResponseDto<PostResponseDto> getOnePost() {
//
//    }


    @Transactional
    //게시글 수정
    public GlobalResponseDto<String> updatePost(Long postId, PostRequestDto postRequestDto, Account currentAccount) {
        //어차피 쓸거 일단 찾아
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.NotFound));
        //작성자 일치여부 확인
        if(!post.getAccount().getId().equals(currentAccount.getId())) throw new CustomException(ErrorCode.NotMatch);//에러코드 수정
        //이미지 수정
        //삭제할 이미지 있다면
        if(postRequestDto.getDeleteUrl() != null){
            List<String> imageUrlList = postRequestDto.getDeleteUrl();
            for(String imageUrl : imageUrlList) {
                if (imageRepository.existsByImageUrl(imageUrl)) {
                    imageRepository.deleteByImageUrl(imageUrl);
                }else{
                    throw new CustomException(ErrorCode.NotFound);
                }
            }
        }
        //추가할 이미지 있다면
        if(postRequestDto.getImageUrl() != null){
            List<String> imageUrlList = postRequestDto.getImageUrl();
            for(String imageUrl : imageUrlList) {
                if (!(imageRepository.existsByImageUrl(imageUrl))) {
                    Image image = new Image(post, imageUrl);
                    //테스트시 꼼꼼히 보자
                    post.getImageList().add(image);
                    imageRepository.save(image);
                }else {
                    throw new CustomException(ErrorCode.AlreadyExists);
                }
            }
        }

        post.update(postRequestDto);
        return GlobalResponseDto.ok("게시글 수정이 완료되었습니다.", null);
    }

    @Transactional
    //게시글 삭제
    public GlobalResponseDto<String> deletePost(Long postId, Account currentAccount) {
        //어차피 쓸거 일단 찾아
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.NotFound));
        //작성자 일치여부 확인
        if(!post.getAccount().getId().equals(currentAccount.getId())) throw new CustomException(ErrorCode.NotMatch);//에러코드 수정

        postRepository.delete(post);
        return GlobalResponseDto.ok("게시글 삭제가 완료되었습니다.", null);
    }



    //굳이??
//    public boolean checkPostAuthor(Long postId, Account currentAccount) {
//        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.NotFound));
//        return post.getAccount().getId().equals(currentAccount.getId());
//    }

}
