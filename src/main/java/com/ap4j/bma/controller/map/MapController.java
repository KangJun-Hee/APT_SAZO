package com.ap4j.bma.controller.map;

import com.ap4j.bma.model.entity.apt.AptDTO;
import com.ap4j.bma.model.entity.apt.AptRealTradeDTO;
import com.ap4j.bma.model.entity.member.MemberDTO;
import com.ap4j.bma.service.apartment.ApartmentServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SessionAttributes("loginMember")
@Slf4j
@Controller
@RequestMapping("map")
public class MapController {


    @Autowired
    ApartmentServiceImpl aptServiceImpl;

    /** 화면 좌표 범위의 DB값 데이터 보내주기 (클라이언트가 사용할 페이지)*/
    @PostMapping("/main")
    public ResponseEntity<Map<String, Object>> getMarker(Double southWestLat, Double southWestLng, Double northEastLat, Double northEastLng, String roadName, String keyword) {

        // 화면 좌표값에 따른 아파트 리스트
        List<AptDTO> aptList = aptServiceImpl.findAptListBounds(southWestLat, southWestLng, northEastLat, northEastLng);
        // 도로명에 따른 실거래가 리스트
        List<AptRealTradeDTO> aptRealTradeDTOList = aptServiceImpl.findByRoadName(roadName);
        // 도로명, 구주소, 아파트명으로 검색시 해당 아파트 정보
        AptDTO aptSearch = aptServiceImpl.findByKeyword(keyword);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("aptList", aptList);
        responseData.put("aptRealTradeDTOList", aptRealTradeDTOList);

        // 검색했을때만 aptSearch 객체를 전송한다.
        if(aptSearch != null) {
            responseData.put("aptSearch", aptSearch);
        }

        return ResponseEntity.ok(responseData);
    }
    @GetMapping("/main")
    public String getMarker() {
        return "kakaoMap/markerCluster";
    }

//    @GetMapping("/main")
//    public String getOtherData(Model model) {
//        // 다른 데이터 가져오기
//        // 예를 들어, 다른 데이터를 가져오는 로직을 여기에 구현하고, 모델에 추가하세요.
//        // List<OtherDataDTO> otherDataList = aptServiceImpl.getOtherData();
//        // model.addAttribute("otherDataList", otherDataList);
//
//        return "kakaoMap/markerCluster";  // 다른 데이터를 표시할 뷰 페이지로 리턴
//    }

    @RequestMapping("map")
    public String map(){
        log.info("MapController.map.execute");
        return "map/map";
    }
}