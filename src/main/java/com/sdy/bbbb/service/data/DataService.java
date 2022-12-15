package com.sdy.bbbb.service.data;

import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.dto.response.data.*;
import com.sdy.bbbb.entity.data.JamOfWeek;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import com.sdy.bbbb.redis.RedisData;
import com.sdy.bbbb.redis.RedisDataRepository;
import com.sdy.bbbb.repository.BookmarkRepository;
import com.sdy.bbbb.repository.GuRepository;
import com.sdy.bbbb.repository.data.DataRepository;
import com.sdy.bbbb.repository.data.JamRepository;
import com.sdy.bbbb.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
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

    private final RedisDataRepository redisDataRepository;


    // 데이터 1 - 주말 데이터 저장 로직
    @Scheduled(cron = "0 2 1 * * MON")
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

    }

    // 데이터 1 - 주말 데이터 레디스 저장 로직
    @Scheduled(cron = "0 0 1 * * MON")
    @Transactional
    public void saveWeekendData2() {

//        //새 주말 데이터
//        List<JamDto> jamDtos = dataRepository.getJamWeekendFromDb();
//
//        List<JamTop5Dto> jamTop5DtoList = new ArrayList<>();
//
//        // 오래된 주말 데이터
//        List<JamOfWeek> oldList = jamRepository.findByIsWeekend(true);
//        // 새 데이터 업데이트
//        for (int i = 0; i < jamDtos.size(); i++) {
//            oldList.get(i).update(jamDtos.get(i));
//        }

        List<JamTop5Dto> jamTop5Dtos = new ArrayList<>();
        List<JamDto> jamDtos = dataRepository.getJamWeekendFromDb();
        Long j = 1L;
        for (JamDto jamDto : jamDtos){
            jamTop5Dtos.add(new JamTop5Dto(j++, jamDto, true));
        }
        RedisData redisData2 = new RedisData("weekend", jamTop5Dtos);
        redisDataRepository.save(redisData2);

    }

    // 데이터 1 - 주중 데이터 저장 로직
    @Transactional
    @Scheduled(cron = "0 0 1 * * SAT")
    public void saveWeekdayData() {

//        // 새 주중 데이터
//        List<JamDto> jamWeekdays = dataRepository.getJamWeekDayFromDb();
//        // 오래된 주중 데이터
//        List<JamOfWeek> oldList = jamRepository.findByIsWeekend(false);
//        // 새 데이터 업데이트
//        for (int i = 0; i < oldList.size(); i++) {
//            oldList.get(i).update(jamWeekdays.get(i));
//        }

        List<JamTop5Dto> jamTop5Dtos = new ArrayList<>();
        List<JamDto> jamDtos = dataRepository.getJamWeekDayFromDb();
        Long i = 1L;
        for (JamDto jamDto : jamDtos){
            jamTop5Dtos.add(new JamTop5Dto(i++, jamDto, false));
        }
        RedisData redisData = new RedisData("weekday", jamTop5Dtos);
        redisDataRepository.save(redisData);

    }


    // 데이터 조회
    @Transactional(readOnly = true)
    public GlobalResponseDto<DataResponseDto> getMainInfo() {

        // data 1
        List<JamOfWeek> jamList = jamRepository.findAll();
        List<JamTop5Dto> jamDtoList = new ArrayList<>();
        for (JamOfWeek jam : jamList) {
            jamDtoList.add(new JamTop5Dto(jam));
        }

        // data 1 from redis
        RedisData weekdayData = redisDataRepository.findById("weekday").orElseThrow(() -> new CustomException(ErrorCode.NotFoundGu));
        RedisData weekendData = redisDataRepository.findById("weekend").orElseThrow(() -> new CustomException(ErrorCode.NotFoundGu));

        for(JamTop5Dto jam : weekendData.getJamTop5DtoList()){
            jam.setWeekend(true);
        }

        List<JamTop5Dto> jamTop5Dtos = new ArrayList<>();
        jamTop5Dtos.addAll(weekdayData.getJamTop5DtoList());
        jamTop5Dtos.addAll(weekendData.getJamTop5DtoList());

        // data 2
        List<PopulationDto> popList = dataRepository.getPopulationFromDb();
        List<PopulationChangesDto> dtoList = new ArrayList<>();
        int i = 1;
        for (PopulationDto pop : popList) {
            dtoList.add(new PopulationChangesDto(pop, i++));
        }

        return GlobalResponseDto.ok("조회 성공", new DataResponseDto(jamTop5Dtos, dtoList));
    }

    // 데이터 삭제 로직
    @Transactional
    @Scheduled(cron = "0 0 3 * * *")
    public void deleteSpotData() {
        int affectedRows = dataRepository.clearDb();
        log.info(affectedRows + " 건의 데이터 삭제 완료");
    }


    // 구별 데이터 조회
    public GlobalResponseDto<BaseGuInfoDto> getGuInformation(String gu, UserDetailsImpl userDetails) {
        gu = ServiceUtil.decoding(gu);

        List<GuBaseInfo> guBaseInfoList = dataRepository.getGuBaseInfo(gu);

        GuBaseInfo guBaseInfos;
        if (guBaseInfoList.size() != 0) {
            guBaseInfos = guBaseInfoList.get(0);
        } else {
            throw new CustomException(ErrorCode.NotReadyForData);
        }

        //지난주 0요일
        List<SpotCalculated> spotCalculateds = dataRepository.getGuInfo(gu);
        //오늘
        List<SpotCalculated> todaySpotCalculatedList = dataRepository.getGuInfoToday(gu);

        // 기존
        List<SpotInfoDto> spotInfoDtoList = new ArrayList<>();
        for (GuBaseInfo guBaseInfo : guBaseInfoList) {
            Map<String, Long> lastPopByHour = new HashMap();
            for (SpotCalculated spot1 : spotCalculateds) {
                if (guBaseInfo.getArea_nm().equals(spot1.getArea_Nm())) {
                    lastPopByHour.put("L" + spot1.getThat_Hour(), (long) Double.parseDouble(spot1.getPopulation_By_Hour()));
                }
            }

            Map<String, Long> todayPopByHour = new HashMap<>();
            for (SpotCalculated spot2 : todaySpotCalculatedList) {
                if (guBaseInfo.getArea_nm().equals(spot2.getArea_Nm())) {
                    todayPopByHour.put("T" + spot2.getThat_Hour(), (long) Double.parseDouble(spot2.getPopulation_By_Hour()));
                }
            }
            spotInfoDtoList.add(new SpotInfoDto(guBaseInfo, new HourDataDto(lastPopByHour, todayPopByHour)));
        }
        if (userDetails != null) {
            boolean isBookMarked = bookmarkRepository.existsByGu_GuNameAndAccount(gu, userDetails.getAccount());
            return GlobalResponseDto.ok("조회 성공", new BaseGuInfoDto(guBaseInfos, isBookMarked, spotInfoDtoList));
        }

        return GlobalResponseDto.ok("조회 성공", new BaseGuInfoDto(guBaseInfos, spotInfoDtoList));
    }

}

