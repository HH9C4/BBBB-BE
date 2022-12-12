package com.sdy.bbbb.dto.response.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sdy.bbbb.dto.response.BookmarkResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BaseGuInfoDto {

    private String gu_nm;
    private String gu_added;
    private String gu_confirmed;
    private List<SpotInfoDto> spotInfoList;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isBookmarked;


    public BaseGuInfoDto(GuBaseInfo guBaseInfo, List<SpotInfoDto> spotInfoList) {
        this.gu_nm = guBaseInfo.getGu_nm();
        this.gu_added = guBaseInfo.getGu_added();
        this.gu_confirmed = guBaseInfo.getGu_confirmed();
        this.spotInfoList = spotInfoList;
    }

    public BaseGuInfoDto(GuBaseInfo guBaseInfo, Boolean isBookmarked, List<SpotInfoDto> spotInfoList) {
        this.gu_nm = guBaseInfo.getGu_nm();
        this.gu_added = guBaseInfo.getGu_added();
        this.gu_confirmed = guBaseInfo.getGu_confirmed();
        this.isBookmarked = isBookmarked;
        this.spotInfoList = spotInfoList;
    }
}
