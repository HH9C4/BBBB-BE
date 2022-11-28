package com.sdy.bbbb.repository;

import com.sdy.bbbb.entity.HashTag;
import com.sdy.bbbb.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {

//    List<HashTag> findByPost(Post post);

    void deleteByPost(Post post);

    @Query(nativeQuery = true, value = "select H.tag as hot, count(h.tag) from hash_tag h join post p on h.post_id = p.id where p.gu_name = ?1 group by hot order by count(h.tag) desc limit 20")
    List<HotTag> findHotTagWithNativeQuery(String guName);

    List<HashTag> findHashTagsByPost_GuName(String gu);

    List<HashTag> findByPost_GuName(String guName);

    List<HashTag> findTagsByPost_GuName(String guName);

    List<HashTag> findDistinctByPost_GuNameOrderByTagDesc(String guName);

//    @Query(nativeQuery = true, value = "select count(tag), tag from hash_tag h join post p on h_post_id = p_id")
//    List<HashTag> findHashTagsByPost_GuName(String guName);

//    List<HashTag>

}
