package com.ap4j.bma.controller.talktalk;


import com.ap4j.bma.model.entity.TalkTalk.TalkTalkReviewDto;
import com.ap4j.bma.model.entity.TalkTalk.TalkTalkReviewEntity;
import com.ap4j.bma.model.entity.apt.AptDTO;
import com.ap4j.bma.model.entity.apt.AptEntity;
import com.ap4j.bma.model.entity.member.MemberDTO;
import com.ap4j.bma.service.apartment.ApartmentService;
import com.ap4j.bma.service.apartment.ApartmentServiceImpl;
import com.ap4j.bma.service.talktalk.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


//import com.ap4j.bma.service.talktalk.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@SessionAttributes("loginMember")
@Controller
public class TalkTalkReviewController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ApartmentService aptService;
    @Autowired
    ApartmentServiceImpl aptServiceImpl; // 아파트 서비스 주입
    @Autowired
    private AptEntity aptEntity;

    @GetMapping("/board/list")
    public String reviewList(Model model){
//        String content ="내용";
//        String writer ="홍길동";
//
//        model.addAttribute("title", title);
//        model.addAttribute("content", content);
//        model.addAttribute("writer", writer);
//
//
//        List<TalkTalkReviewDto> reviews = reviewService.getAllReviews();
//        model.addAttribute("reviews", reviews);
//        return "/kakaomap/markerCluster";

        List<TalkTalkReviewEntity> list = reviewService.reviewList();
        log.info(list.toString());
        //서비스에서 생성한 리스트를 list라는 이름으로 반환하겠다.
        model.addAttribute("list", list);
        return "reviewlist";
    }


    @PostMapping("/board/writepro")
    public String boardwritePro(@RequestParam("content") String content, HttpSession session) {

        log.info("리뷰컨트롤러 boardWritePro실행, content: " + content);
        TalkTalkReviewEntity reviewEntity = new TalkTalkReviewEntity();


//        AptEntity aptEntity = aptService.aptList();
//        List<AptDTO> aptDTOList = aptService.aptList();
//        int aptId = aptDTOList.get
        List<AptDTO> aptEntity = aptService.aptList();
        for (AptDTO aptDTO: aptEntity){
            aptDTO.getId();
        }
//        reviewEntity.setId(99);
//        AptDTO aptInfo = aptServiceImpl.getAptInfoById(apartmentId);



        reviewEntity.setBoard_no(99);

        MemberDTO dto = (MemberDTO) session.getAttribute("loginMember");
        String email = dto.getEmail();
        reviewEntity.setEmail(email);

        reviewEntity.setContent(content);

        reviewService.write(reviewEntity);
        log.info(reviewEntity.toString());
        return "redirect:/map/main";
    }


}
