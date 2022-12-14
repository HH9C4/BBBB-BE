package com.sdy.bbbb.service;

import com.sdy.bbbb.util.AlarmType;
import com.sdy.bbbb.service.SSE.SseService;
import com.sdy.bbbb.dto.request.PostRequestDto;
import com.sdy.bbbb.dto.response.*;
import com.sdy.bbbb.entity.*;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import com.sdy.bbbb.repository.querydsl.PostRepositoryImpl;
import com.sdy.bbbb.repository.*;
import com.sdy.bbbb.s3.S3Uploader;
import com.sdy.bbbb.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostRepositoryImpl postRepositoryImpl;
    private final ImageRepository imageRepository;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final HashTagRepository hashTagRepository;

    private final GuRepository guRepository;
    private final S3Uploader s3Uploader;

    private final SseService sseService;

//    private final String[] guList = {"강남구", "강동구", "강북구", "강서구", "관악구", "광진구",
//            "구로구", "금천구", "노원구", "도봉구", "동대문구", "동작구", "마포구", "서대문구", "서초구",
//            "성동구", "성북구", "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "중구", "중랑구"};

    @Transactional
    //게시글 생성
    public GlobalResponseDto<PostResponseDto> createPost(PostRequestDto postRequestDto,
                                                         List<MultipartFile> multipartFile,
                                                         Account account) {
        //올바른 구 인지 검증
        validateGu(postRequestDto.getGu());
        //새로운 데이터 생성
        Post post = new Post(postRequestDto, account);

        postRepository.save(post);

        //이미지 있다면
        createImageIfNotNull(multipartFile, post);
        //태그 있다면
        createTagIfNotNull(postRequestDto.getTagList(), post);

        //구를 북마크한 사람들에게 알림 전송
        sendNotice(postRequestDto, post, account);

        return GlobalResponseDto.created("게시글이 등록 되었습니다.",
                new PostResponseDto(post, ServiceUtil.getImgUrl(post), ServiceUtil.getTag(post),false));
    }



    //게시글 전체 조회(구별)
    @Transactional(readOnly = true)
    public GlobalResponseDto<PostListResponseDto> getPost(String guName,
                                                          String category,
                                                          String sort,
                                                          Pageable pageable,
                                                          Account account) {

        //구를 디비에서 찾아서 올바르게 들어왔는지 검사하는 로직이 필요할까?
        guName = ServiceUtil.decoding(guName);
        category = ServiceUtil.decoding(category);
        validateGu(guName);

        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        PageImpl<Post> postList1 = postRepositoryImpl.test2(guName, category, sort, pageable);

        List<Like> likeList = likeRepository.findLikesByAccount(account);
        for (Post post : postList1) {
            postResponseDtoList.add(
                    new PostResponseDto(post, ServiceUtil.getImgUrl(post), ServiceUtil.getTag(post), ServiceUtil.amILikedPost(post, likeList)));
        }

        boolean isBookMarked = bookmarkRepository.existsByGu_GuNameAndAccount(guName, account);
        return GlobalResponseDto.ok("조회 성공", new PostListResponseDto(isBookMarked, postResponseDtoList, postList1.getTotalElements()));
    }

//    게시글 검색
    @Transactional(readOnly = true)
    public GlobalResponseDto<PostListResponseDto> searchPost(Integer type,
                                                             String searchWord,
                                                             String sort,
                                                             Pageable pageable,
                                                             Account account) {

        searchWord = ServiceUtil.decoding(searchWord);
        PageImpl<Post> postList = postRepositoryImpl.searchByTag(type, searchWord, sort, pageable);

        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        List<Like> likeList = likeRepository.findLikesByAccount(account);
        for (Post post : postList) {
            //좋아요 확인
            postResponseDtoList.add(
                    new PostResponseDto(post, ServiceUtil.getImgUrl(post), ServiceUtil.getTag(post), ServiceUtil.amILikedPost(post, likeList)));
        }
        return GlobalResponseDto.ok("조회 성공", new PostListResponseDto(null, postResponseDtoList, postList.getTotalElements()));
    }

    //게시글 상세 조회
    @Transactional
    public GlobalResponseDto<OnePostResponseDto> getOnePost(Long postId, Account account) {
        Post post = postRepository.searchOneByIdWithCommentList(postId).orElseThrow(() -> new CustomException(ErrorCode.NotFoundPost));

        post.setViews(post.getViews() + 1);
        //이미지 추출 함수로, DTO에 있는게 나을까?
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
//        System.out.println("가져왔니!?"+post.getCommentList().size());
        List<Like> likeList = likeRepository.findLikesByAccount(account);

        for(Comment comment : post.getCommentList()){

            commentResponseDtoList.add(new CommentResponseDto(comment, ServiceUtil.amILikedComment(comment, likeList)));
        }

        return GlobalResponseDto.ok("조회 성공", new OnePostResponseDto(post, ServiceUtil.getImgUrl(post), ServiceUtil.getTag(post), ServiceUtil.amILikedPost(post, likeList), commentResponseDtoList));
    }

    //핫태그 20
    @Transactional(readOnly = true)
    public GlobalResponseDto<TagResponseDto> hotTag20(String guName) {
        guName = ServiceUtil.decoding(guName);
        validateGu(guName);
        List<HotTag> hashTagList = hashTagRepository.findHotTagWithNativeQuery(guName);
        List<String> tagList = new ArrayList<>();
        for(HotTag tag : hashTagList){
            tagList.add(tag.getHot());
        }
        return GlobalResponseDto.ok("조회 성공", new TagResponseDto(tagList));
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
        ServiceUtil.checkPostAuthor(post, account);

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

        //태그 수정
        hashTagRepository.deleteByPost(post);
        createTagIfNotNull(postRequestDto.getTagList(), post);

        post.update(postRequestDto);
        List<Like> likeList = likeRepository.findLikesByAccount(account);
        return GlobalResponseDto.created("게시글 수정이 완료되었습니다.",
                new PostResponseDto(post, ServiceUtil.getImgUrl(post), ServiceUtil.getTag(post), ServiceUtil.amILikedPost(post, likeList)));
    }

    //게시글 삭제
    @Transactional
    public GlobalResponseDto<String> deletePost(Long postId, Account account) {
        //어차피 쓸거 일단 찾아
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.NotFoundPost));
        //작성자 일치여부 확인
        ServiceUtil.checkPostAuthor(post, account);

        postRepository.delete(post);
//        int result = postRepository.deleteReturnAffectedRows(post.getId());
        return GlobalResponseDto.ok("게시글 삭제가 완료되었습니다.", post.getGuName());
    }

    @Transactional(readOnly = true)
    public GlobalResponseDto<TagResponseDto> getTags(String gu) {
        gu = ServiceUtil.decoding(gu);
        validateGu(gu);
        List<HashTag> tagList = hashTagRepository.findByPost_GuName(gu);
        Set<String> tagStrSet = new HashSet<>();
        for(HashTag tag : tagList){
            tagStrSet.add(tag.getTag());
        }
        List<String> tagStrList = new ArrayList<>(List.copyOf(tagStrSet));
        tagStrList.sort(Comparator.naturalOrder());
        return GlobalResponseDto.ok("조회완료", new TagResponseDto(tagStrList));
    }


    //알림 전송
    private void sendNotice(PostRequestDto postRequestDto, Post post, Account account){
        //해당 구를 북마크한 어카운트를 찾아와라.
        List<Bookmark> bookmarks = bookmarkRepository.findByGuFetchAccount(postRequestDto.getGu());

        for(Bookmark bookmark : bookmarks) {
            if (!Objects.equals(bookmark.getAccount().getId(), account.getId())) {
                sseService.send(bookmark.getAccount(), AlarmType.eventGuPost, "북마크한 " + bookmark.getGu().getGuName() + "에 새로운 게시글이 추가되었어요!", "guName : " + postRequestDto.getGu() + ", postId: " + post.getId());
            }
        }
    }

    //등록 할 이미지가 있다면 사용
    public void createImageIfNotNull(List<MultipartFile> multipartFile, Post post) {
        if (multipartFile != null && multipartFile.size() > 0){
            List<Image> imageList = new ArrayList<>();
            for (MultipartFile imgFile : multipartFile) {
                Image image = new Image(post, s3Uploader.upload(imgFile, "dir1"));
                imageList.add(image);
                imageRepository.save(image);
            }
            post.setImageList(imageList);
        }
    }

    //태그가 있다면 태그 저장
    private void createTagIfNotNull(List<String> tagList, Post post) {
        if (tagList != null && tagList.size() > 0) {
            List<HashTag> hashTagList = new ArrayList<>();
            for (String tag : tagList) {
                HashTag hashTag = new HashTag(post, tag);
                hashTagList.add(hashTag);
                hashTagRepository.save(hashTag);
            }
            post.setTagList(hashTagList);
        }
    }

    private void validateGu(String guName) {
        List<Gu> guList = guRepository.findAll();
        for(Gu gu : guList){
            if(gu.getGuName().equals(guName)){
                return;
            }
        }
        throw new CustomException(ErrorCode.NotFoundGu);
    }

}
