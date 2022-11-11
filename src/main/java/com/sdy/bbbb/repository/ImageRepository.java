package com.sdy.bbbb.repository;

import com.sdy.bbbb.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    boolean existsByImageUrl(String imgUrl);
    void deleteByImageUrl(String imgUrl);

}
