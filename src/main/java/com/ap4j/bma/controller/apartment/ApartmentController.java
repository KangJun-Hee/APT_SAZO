package com.ap4j.bma.controller.apartment;

import com.ap4j.bma.service.apartment.ApartmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ApartmentController {

    @Autowired
    ApartmentServiceImpl aptServiceImpl;

    /** DB에서 js로 데이터 넘겨줘서 좌표 검색하기 (최초 1회 후 DB에 좌표값 저장하면 실행 할 필요 없음)*/
    @GetMapping("/getCoordinates")
    public String aptTransactionTest(Model model) {
        model.addAttribute("aptList", aptServiceImpl.aptList());
        return "kakaoMap/aptMain";
    }

    /** 가져온 좌표값 DB에 추가하기 (따로 호출할 필요 없이 getCoordinates 호출하면 자동으로 호출됨)*/
    /** DB에 좌표값 추가하는 이유 : 화면 좌표 범위의 DB값만 선택해서 가지고와서 마커 찍기 위해서 (성능향상) */
    @PostMapping("/saveCoordinates")
    public String saveTransaction(String roadName, Double latitude, Double longitude) {
        aptServiceImpl.updateApt(roadName, latitude, longitude);
        return "redirect:/getCoordinates";
    }

}
