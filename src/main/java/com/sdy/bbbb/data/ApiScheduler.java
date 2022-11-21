package com.sdy.bbbb.data;

import com.sdy.bbbb.entity.Spot;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ApiScheduler {

    private final SpotRepository spotRepository;
    private final DataRepository dataRepository;

    @Value("${seoul.open.api.url}")
    private StringBuilder url;

    @Transactional
    @Scheduled(cron = "0 2/5 * * * *")
    public void call() throws Exception {


        List<Spot> spots = spotRepository.findAll();
        List<SpotData> spotDataList = new ArrayList<>();
        for (int i = 0; i < spots.size(); i++) {
            // 1. URL을 만들기 위한 StringBuilder.
//            StringBuilder urlBuilder = new StringBuilder(""); /*URL*/
//            String s = "";
            // 2. 오픈 API의요청 규격에 맞는 파라미터 생성, 발급받은 인증키.
//            urlBuilder.append("/" + "1");
//            urlBuilder.append("/" + "5");

//            url.append("/" + encoding(spots.get(i).getSpot()));
            String url2 = url + ("/" + encoding(spots.get(i).getSpot()));
            // 3. URL 객체 생성.
//            String url2 = urlBuilder.toString();

            Document documentInfo;
            documentInfo = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url2);
//            String ppltn_rate_10 = documentInfo.getElementsByTagName("PPLTN_RATE_10").item(0).getTextContent();
//            String area_congest_msg = documentInfo.getElementsByTagName("AREA_CONGEST_MSG").item(0).getTextContent();


//            SpotData spotData = new SpotData(documentInfo.getElementsByTagName("PPLTN_RATE_10").item(0).getTextContent(),
//                    documentInfo.getElementsByTagName("AREA_CONGEST_MSG").item(0).getTextContent());
//            testRepo.save(spotData);

            SpotData spotData = SpotData.builder()
                    .areaNm(getElementText(documentInfo, DataTagName.AREA_NM.name()))
                    .areaCongestLvl(getElementText(documentInfo, DataTagName.AREA_CONGEST_LVL.name()))
//                    .areaCongestMsg(getElementText(documentInfo, "AREA_CONGEST_MSG"))
                    .areaPpltnMin(getElementText(documentInfo, DataTagName.AREA_PPLTN_MIN.name()))
                    .areaPpltnMax(getElementText(documentInfo, DataTagName.AREA_PPLTN_MAX.name()))
                    .malePpltnRate(getElementText(documentInfo, DataTagName.MALE_PPLTN_RATE.name()))
                    .femalePpltnRate(getElementText(documentInfo, DataTagName.FEMALE_PPLTN_RATE.name()))
                    .ppltnRate10(getElementText(documentInfo, DataTagName.PPLTN_RATE_10.name()))
                    .ppltnRate20(getElementText(documentInfo, DataTagName.PPLTN_RATE_20.name()))
                    .ppltnRate30(getElementText(documentInfo, DataTagName.PPLTN_RATE_30.name()))
                    .ppltnRate40(getElementText(documentInfo, DataTagName.PPLTN_RATE_40.name()))
                    .ppltnRate50(getElementText(documentInfo, DataTagName.PPLTN_RATE_50.name()))
                    .ppltnTime(getElementText(documentInfo, DataTagName.PPLTN_TIME.name()))
                    .temp(getElementText(documentInfo,DataTagName.TEMP.name()))
//                    .sensibleTemp(getElementText(documentInfo, "SENSIBLE_TEMP"))
                    .maxTemp(getElementText(documentInfo, DataTagName.MAX_TEMP.name()))
                    .minTemp(getElementText(documentInfo, DataTagName.MIN_TEMP.name()))
//                    .humidity(getElementText(documentInfo, "HUMIDITY"))
//                    .precipitation(getElementText(documentInfo, "PRECIPITATION"))
//                    .precptType(getElementText(documentInfo, "PRECPT_TYPE"))
                    .pcpMsg(getElementText(documentInfo, DataTagName.PCP_MSG.name()))
//                    .uvIndex(getElementText(documentInfo, "UV_INDEX"))
//                    .uvMsg(getElementText(documentInfo, "UV_MSG"))
                    .pm25Index(getElementText(documentInfo,DataTagName.PM25_INDEX.name() ))
                    .pm25(getElementText(documentInfo, DataTagName.PM25.name()))
                    .pm10Index(getElementText(documentInfo, DataTagName.PM10_INDEX.name()))
                    .pm10(getElementText(documentInfo, DataTagName.PM10.name()))
//                    .airIdx(getElementText(documentInfo, "AIR_IDX"))
                    .airMsg(getElementText(documentInfo, DataTagName.AIR_MSG.name()))
                    .weatherTime(getElementText(documentInfo, DataTagName.WEATHER_TIME.name()))
//                    .strdDt(getElementText(documentInfo, "STRD_DT"))
                    .guNm(getElementText(documentInfo, DataTagName.GU_NM.name()))
                    .guConfirmed(getElementText(documentInfo, DataTagName.GU_CONFIRMED.name()))
                    .guAdded(getElementText(documentInfo, DataTagName.GU_ADDED.name()))
                    .build();

            spotDataList.add(spotData);
        }

        Iterable<SpotData> dataList = spotDataList;
        dataRepository.saveAll(dataList);

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
