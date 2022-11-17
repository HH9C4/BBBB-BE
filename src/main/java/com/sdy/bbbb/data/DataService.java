package com.sdy.bbbb.data;

import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.entity.Spot;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataService {

    private final TestRepo testRepo;
    private final SpotRepository spotRepository;

    @Transactional
    public GlobalResponseDto<List<PopulationChangesDto>> getPopulationChanges(){
        List<PopulationDto> popList = testRepo.getPopulation();
        List<PopulationChangesDto> dtoList = new ArrayList<>();
        for(PopulationDto pop : popList){
            dtoList.add(new PopulationChangesDto(pop));
        }
        return GlobalResponseDto.ok("조회성공", dtoList);

    }


    @Transactional
    @Scheduled(cron = "0 2/5 * * * *")
    public void call() throws Exception {


        List<Spot> spots = spotRepository.findAll();

        for (int i = 0; i < 49; i++) {
            // 1. URL을 만들기 위한 StringBuilder.
            StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088/4644556a44616e6639346c4c4a4175/xml/citydata"); /*URL*/

            // 2. 오픈 API의요청 규격에 맞는 파라미터 생성, 발급받은 인증키.
            urlBuilder.append("/" + "1");
            urlBuilder.append("/" + "5");
            urlBuilder.append("/" + encoding(spots.get(i).getSpot()));

            // 3. URL 객체 생성.
            String url2 = urlBuilder.toString();

            // 4. 요청하고자 하는 URL과 통신하기 위한 Connection 객체 생성.
            Document documentInfo;
            documentInfo = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url2);
            String ppltn_rate_10 = documentInfo.getElementsByTagName("PPLTN_RATE_10").item(0).getTextContent();
            String area_congest_msg = documentInfo.getElementsByTagName("AREA_CONGEST_MSG").item(0).getTextContent();


//            SpotData spotData = new SpotData(documentInfo.getElementsByTagName("PPLTN_RATE_10").item(0).getTextContent(),
//                    documentInfo.getElementsByTagName("AREA_CONGEST_MSG").item(0).getTextContent());
//            testRepo.save(spotData);

            SpotData spotData = SpotData.builder()
                    .areaNm(getElementText(documentInfo, "AREA_NM"))
                    .areaCongestLvl(getElementText(documentInfo, "AREA_CONGEST_LVL"))
                    .areaCongestMsg(getElementText(documentInfo, "AREA_CONGEST_MSG"))
                    .areaPpltnMin(getElementText(documentInfo, "AREA_PPLTN_MIN"))
                    .areaPpltnMax(getElementText(documentInfo, "AREA_PPLTN_MAX"))
                    .malePpltnRate(getElementText(documentInfo, "MALE_PPLTN_RATE"))
                    .femalePpltnRate(getElementText(documentInfo, "FEMALE_PPLTN_RATE"))
                    .ppltnRate10(getElementText(documentInfo, "PPLTN_RATE_10"))
                    .ppltnRate20(getElementText(documentInfo, "PPLTN_RATE_20"))
                    .ppltnRate30(getElementText(documentInfo, "PPLTN_RATE_30"))
                    .ppltnRate40(getElementText(documentInfo, "PPLTN_RATE_40"))
                    .ppltnRate50(getElementText(documentInfo, "PPLTN_RATE_50"))
                    .ppltnTime(getElementText(documentInfo, "PPLTN_TIME"))
                    .temp(getElementText(documentInfo, "TEMP"))
                    .sensibleTemp(getElementText(documentInfo, "SENSIBLE_TEMP"))
                    .maxTemp(getElementText(documentInfo, "MAX_TEMP"))
                    .minTemp(getElementText(documentInfo, "MIN_TEMP"))
                    .humidity(getElementText(documentInfo, "HUMIDITY"))
                    .precipitation(getElementText(documentInfo, "PRECIPITATION"))
                    .precptType(getElementText(documentInfo, "PRECPT_TYPE"))
                    .pcpMsg(getElementText(documentInfo, "PCP_MSG"))
                    .uvIndex(getElementText(documentInfo, "UV_INDEX"))
                    .uvMsg(getElementText(documentInfo, "UV_MSG"))
                    .pm25Index(getElementText(documentInfo, "PM25_INDEX"))
                    .pm25(getElementText(documentInfo, "PM25"))
                    .pm10Index(getElementText(documentInfo, "PM10_INDEX"))
                    .pm10(getElementText(documentInfo, "PM10"))
                    .airIdx(getElementText(documentInfo, "AIR_IDX"))
                    .airMsg(getElementText(documentInfo, "AIR_MSG"))
                    .weatherTime(getElementText(documentInfo, "WEATHER_TIME"))
                    .strdDt(getElementText(documentInfo, "STRD_DT"))
                    .guNm(getElementText(documentInfo, "GU_NM"))
                    .guConfirmed(getElementText(documentInfo, "GU_CONFIRMED"))
                    .guAdded(getElementText(documentInfo, "GU_ADDED"))
                    .build();

            testRepo.save(spotData);
        }

//        System.out.println(documentInfo.getElementsByTagName("PPLTN_RATE_10").item(0).getTextContent());
//        System.out.println(documentInfo.getElementsByTagName("AREA_CONGEST_MSG").item(0).getTextContent());

        //태그접근
//        Element root = documentInfo.getDocumentElement();
//        NodeList nList = root.getElementsByTagName("LIVE_PPLTN_STTS").item(0).getChildNodes();
//        System.out.println(nList.item(0));
//        System.out.println(nList.item(0).getNodeValue());
//        System.out.println(nList.item(0).getNodeName());

    }

    private String encoding(String toEncode) {
        String result = "";
        try {
            result = URLEncoder.encode(toEncode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new CustomException(ErrorCode.FailDecodeString);
        }
        return result;
    }

    private String getElementText(Document document, String tag) {
        Document documentInfo = document;
        String result = documentInfo.getElementsByTagName(tag).item(0).getTextContent();
        return result;
    }
}

