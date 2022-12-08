package com.sdy.bbbb.service.data;

import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.response.BookmarkResponseDto;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Bookmark;
import com.sdy.bbbb.repository.BookmarkRepository;
import com.sdy.bbbb.repository.data.DataRepository;
import com.sdy.bbbb.repository.data.JamRepository;
import com.sdy.bbbb.entity.data.JamOfWeek;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.dto.response.data.*;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import com.sdy.bbbb.repository.GuRepository;
import com.sdy.bbbb.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataService {

    private final DataRepository dataRepository;
    private final JamRepository jamRepository;

    private final GuRepository guRepository;
    private final BookmarkRepository bookmarkRepository;


    // 데이터 1 - 주말 데이터 저장 로직
    @Scheduled(cron = "0 0 1 * * MON")
    @Transactional
    public void saveWeekendData() {
        //새 주말 데이터
        List<JamDto> jamDtos = dataRepository.getJamWeekendFromDb();
        // 오래된 주말 데이터
        List<JamOfWeek> oldList = jamRepository.findByIsWeekend(true);
        // 새 데이터 업데이트
        for (int i = 0; i < jamDtos.size(); i++) {
            oldList.get(i).update(jamDtos.get(i));
        }

        log.info("주말 데이터 저장완료");
    }

    // 데이터 1 - 주중 데이터 저장 로직
    @Transactional
    @Scheduled(cron = "0 0 1 * * SAT")
    public void saveWeekdayData() {
        // 새 주중 데이터
        List<JamDto> jamWeekdays = dataRepository.getJamWeekDayFromDb();
        // 오래된 주중 데이터
        List<JamOfWeek> oldList = jamRepository.findByIsWeekend(false);
        // 새 데이터 업데이트
        for (int i = 0; i < oldList.size(); i++) {
            oldList.get(i).update(jamWeekdays.get(i));
        }

        log.info("주중 데이터 저장완료");
    }


    // 데이터 조회
    @Transactional(readOnly = true)
    public GlobalResponseDto<DataResponseDto> getMainInfo() {

        // data 1
        List<JamOfWeek> jamList = jamRepository.findAll();
        List<JamTop5Dto> jamDtoList = new ArrayList<>();
        for(JamOfWeek jam : jamList){
            jamDtoList.add(new JamTop5Dto(jam));
        }

        // data 2
        List<PopulationDto> popList = dataRepository.getPopulationFromDb();
        List<PopulationChangesDto> dtoList = new ArrayList<>();
        int i = 1;
        for (PopulationDto pop : popList) {
            dtoList.add(new PopulationChangesDto(pop, i++));
        }

        return GlobalResponseDto.ok("조회 성공", new DataResponseDto(jamDtoList, dtoList));
    }

    // 데이터 삭제 로직
    @Transactional
    @Scheduled(cron = "0 0 3 * * *")
    public void deleteSpotData() {
        int affectedRows = dataRepository.clearDb();
        log.info(affectedRows + " 건의 데이터 삭제 완료");
    }


    // 구별 데이터 조회
    public GlobalResponseDto<BaseGuInfoDto> getGuInformation(String gu) {
        gu = ServiceUtil.decoding(gu);
        //구valid 해야함(준비중)

        List<GuBaseInfo> guBaseInfoList = dataRepository.getGuBaseInfo(gu);

        GuBaseInfo guBaseInfos;
        if (guBaseInfoList.size() != 0) {
            guBaseInfos = guBaseInfoList.get(0);
        }else {
            throw new CustomException(ErrorCode.NotReadyForData);
        }


        //지난주 0요일
        List<SpotCalculated> spotCalculateds = dataRepository.getGuInfo(gu);
        //오늘
        List<SpotCalculated> todaySpotCalculatedList = dataRepository.getGuInfoToday(gu);


//        기존
        List<SpotInfoDto> spotInfoDtoList = new ArrayList<>();
        for(GuBaseInfo guBaseInfo : guBaseInfoList){
            Map<String, Long> lastPopByHour =  new HashMap();
            for(SpotCalculated spot1 : spotCalculateds) {
                if (guBaseInfo.getArea_nm().equals(spot1.getArea_Nm())) {
                    lastPopByHour.put("L" + spot1.getThat_Hour(), (long)Double.parseDouble(spot1.getPopulation_By_Hour()));
                }
            }

            Map<String, Long> todayPopByHour = new HashMap<>();
            for(SpotCalculated spot2 : todaySpotCalculatedList) {
                if (guBaseInfo.getArea_nm().equals(spot2.getArea_Nm())) {
                    todayPopByHour.put("T" + spot2.getThat_Hour(), (long)Double.parseDouble(spot2.getPopulation_By_Hour()));
                }
            }
            spotInfoDtoList.add(new SpotInfoDto(guBaseInfo,new HourDataDto(lastPopByHour, todayPopByHour)));
        }

        return GlobalResponseDto.ok("조회 성공", new BaseGuInfoDto(guBaseInfos, spotInfoDtoList));
    }





    // 구별 데이터 조회
    public GlobalResponseDto<BaseGuInfoDto> getGuInformation2(String gu, UserDetailsImpl userDetails) {
        gu = ServiceUtil.decoding(gu);
        //구valid 해야함(준비중)

        List<GuBaseInfo> guBaseInfoList = dataRepository.getGuBaseInfo(gu);

        GuBaseInfo guBaseInfos;
        if (guBaseInfoList.size() != 0) {
            guBaseInfos = guBaseInfoList.get(0);
        }else {
            throw new CustomException(ErrorCode.NotReadyForData);
        }


        //지난주 0요일
        List<SpotCalculated> spotCalculateds = dataRepository.getGuInfo(gu);
        //오늘
        List<SpotCalculated> todaySpotCalculatedList = dataRepository.getGuInfoToday(gu);


//        기존
        List<SpotInfoDto> spotInfoDtoList = new ArrayList<>();
        for(GuBaseInfo guBaseInfo : guBaseInfoList){
            Map<String, Long> lastPopByHour =  new HashMap();
            for(SpotCalculated spot1 : spotCalculateds) {
                if (guBaseInfo.getArea_nm().equals(spot1.getArea_Nm())) {
                    lastPopByHour.put("L" + spot1.getThat_Hour(), (long)Double.parseDouble(spot1.getPopulation_By_Hour()));
                }
            }

            Map<String, Long> todayPopByHour = new HashMap<>();
            for(SpotCalculated spot2 : todaySpotCalculatedList) {
                if (guBaseInfo.getArea_nm().equals(spot2.getArea_Nm())) {
                    todayPopByHour.put("T" + spot2.getThat_Hour(), (long)Double.parseDouble(spot2.getPopulation_By_Hour()));
                }
            }
            spotInfoDtoList.add(new SpotInfoDto(guBaseInfo,new HourDataDto(lastPopByHour, todayPopByHour)));
        }
        if(userDetails != null) {
            List<BookmarkResponseDto> bookmarkList = new ArrayList<>();
            List<Bookmark> bookmarks = bookmarkRepository.findBookmarksByAccountId(userDetails.getAccount().getId());
            for (Bookmark bookmark : bookmarks) {
                bookmarkList.add(new BookmarkResponseDto(bookmark));
            }
            return GlobalResponseDto.ok("조회 성공", new BaseGuInfoDto(guBaseInfos, bookmarkList, spotInfoDtoList));
        }

        return GlobalResponseDto.ok("조회 성공", new BaseGuInfoDto(guBaseInfos, spotInfoDtoList));
    }

}

