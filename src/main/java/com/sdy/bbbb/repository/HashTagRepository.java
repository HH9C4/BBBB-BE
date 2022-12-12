package com.sdy.bbbb.repository;

import com.sdy.bbbb.entity.HashTag;
import com.sdy.bbbb.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {

    void deleteByPost(Post post);

    @Query(value = "select tag as hot, count(tag) as cnt from hash_tag h " +
            "join post p on h.post_id = p.id " +
            "where p.gu_name = ?1 " +
            "group by tag " +
            "order by cnt desc " +
            "limit 20", nativeQuery = true)
    List<HotTag> findHotTagWithNativeQuery(String guName);

    List<HashTag> findByPost_GuName(String guName);

}
