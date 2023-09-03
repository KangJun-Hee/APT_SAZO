//package com.ap4j.bma.controller.talktalk;
//
//import com.ap4j.bma.model.entity.TalkTalk.TalkTalkReplyDto;
//import com.ap4j.bma.service.talktalk.ReviewService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//@RequiredArgsConstructor
//public class TalkTalkReplyController {
//
//    @Autowired
//    private final ReplyService replyService;
//
//    @Autowired
//    private TalkTalkRepository talkTalkRepository;
//
//    public TalkTalkReplyController(ReviewService reviewService) {
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
//    @GetMapping("/map/main")
//    public String getAllReplysByReview(@PathVariable int board_no, Model model) {
//        String content ="내용";
//        String writer ="홍길동";
//
//        model.addAttribute("title", title);
//        model.addAttribute("content", content);
//        model.addAttribute("writer", writer);
//
//
//        List<TalkTalkReplyDto> replys = replyService.getAllReplysByReview(board_no);
//        model.addAttribute("replys", replys);
//        return "/kakaomap/markerCluster";
//
//
//        //서비스에서 생성한 리스트를 list라는 이름으로 반환하겠다.
//        model.addAttribute("list", ReviewService.boardList());
//        return "boardlist";
//    }
//}
