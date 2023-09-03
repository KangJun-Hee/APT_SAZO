package com.ap4j.bma.controller.talktalk;


import com.ap4j.bma.model.entity.TalkTalk.TalkTalkReviewDto;
import com.ap4j.bma.model.entity.TalkTalk.TalkTalkReviewEntity;
import com.ap4j.bma.model.entity.member.MemberDTO;
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
//    @GetMapping("/map/main")
//    //게시물 작성 폼으로 이동하는 컨트롤러
//    public String boardWriteForm(){
//        //return 되는 정적 템플릿을 열어준다.
//        return "/kakaomap/markerCluster";
//    }

    @PostMapping("/board/writepro")
    public String boardwritePro(@RequestParam("content") String content, HttpSession session) {

        log.info("내용: " + content);
        TalkTalkReviewEntity test = new TalkTalkReviewEntity();
        test.setId(99);
        test.setBoard_no(99);
        MemberDTO dto = (MemberDTO) session.getAttribute("loginMember");
        String email = dto.getEmail();
        test.setEmail(email);
        test.setContent(content);

        reviewService.write(test);
        log.info(test.toString());
        return "redirect:/map/main";
//        return "success"; // 성공 페이지로 이동하도록 수정
    }

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

//    @Autowired
//    private final ReviewService reviewService;
//
//    @Autowired
//    private TalkTalkRepository talkTalkRepository;
//
//    public TalkTalkReviewController(ReviewService reviewService) {
//        this.reviewService = reviewService;
//    }
//
//    @GetMapping("/")
//    public String list() {
//        return "board/list.html";
//    }
//
//    @GetMapping("/post")
//    public String post() {
//        return "board/post.html";
//    }
//
//    @PostMapping("/post")
//    public String write(BoardDto boardDto) {
//        boardService.savePost(boardDto);
//        return "redirect:/";
//    }
//
//
//    // 게시글 작성 페이지
//    @GetMapping("/kakaomap/write.do")
//    public String openPostWrite(Model model) {
//        return "post/write";
//    }
//
}
