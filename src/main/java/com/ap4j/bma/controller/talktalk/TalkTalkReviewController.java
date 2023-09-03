package com.ap4j.bma.controller.talktalk;


import com.ap4j.bma.model.entity.TalkTalk.TalkTalkReviewEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


//import com.ap4j.bma.service.talktalk.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

@Controller
public class TalkTalkReviewController {

//    @GetMapping("/map/main")
//    //게시물 작성 폼으로 이동하는 컨트롤러
//    public String boardWriteForm(){
//        //return 되는 정적 템플릿을 열어준다.
//        return "/kakaomap/markerCluster";
//    }

    @PostMapping("/board/writepro")
    public String boardwritePro(@RequestParam("content") String content) {
        System.out.println("내용: " + content);
        return "success"; // 성공 페이지로 이동하도록 수정
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


        //서비스에서 생성한 리스트를 list라는 이름으로 반환하겠다.
//        model.addAttribute("list", reviewService.reviewList());
        return "reviewlist";
    }
}
