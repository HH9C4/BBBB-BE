package com.sdy.bbbb.dto.response;

import com.sdy.bbbb.entity.Bookmark;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookmarkResponseDto {
    private String gu;
    private boolean bookmarked;

    public BookmarkResponseDto(Bookmark bookmark){
        this.gu = bookmark.getGu().getGu();
        this.bookmarked = bookmark.isBookmarked();
    }
}
