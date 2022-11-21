package com.sdy.bbbb.data.dataDto;

import java.util.List;

public class BaseGuInfoDto {

    private String gu_nm;
    private String gu_added;
    private String gu_confirmed;
    private List<SpotInfoDto> spotInfoList;
    private List<SpotCalculatedDto> spotCalculatedList;

    public BaseGuInfoDto(GuBaseInfo guBaseInfo, List<SpotInfoDto> spotInfoList, List<SpotCalculatedDto> spotCalculatedDtoList) {
        this.gu_nm = guBaseInfo.getGu_nm();
        this.gu_added = guBaseInfo.getGu_added();
        this.gu_confirmed = guBaseInfo.getGu_confirmed();
        this.spotInfoList = spotInfoList;
        this.spotCalculatedList = spotCalculatedDtoList;
    }
}
