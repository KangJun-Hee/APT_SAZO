package com.ap4j.bma.service.talktalk;

//import com.ap4j.bma.model.entity.TalkTalk.TalkTalkReplyDto;
//import com.ap4j.bma.model.entity.TalkTalk.TalkTalkReviewDto;
import com.ap4j.bma.model.entity.TalkTalk.TalkTalkReviewEntity;
import com.ap4j.bma.model.repository.TalkTalkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


//서비스 -> 컨트롤러에서 이용
@Service
public class ReviewService {
    @Autowired
    private TalkTalkRepository talktalkRepository;

//    public ReviewService(TalkTalkRepository talktalkRepository){
//        this.talktalkRepository = talktalkRepository;
//    }
//
//    public void saveReview(TalkTalkReviewDto talkTalkReviewDto) {
//        return talktalkRepository.save(boardDto.toEntity()).getId();
//    }
//    @Transactional
//    public List<TalkTalkReviewDto> getReviewList() {
//        List<Review> reviews = TalkTalkRepository.findAll();
//        List<BoardDto> boardDtoList = new ArrayList<>();
//
//        for(Board board : boardList) {
//            BoardDto boardDto = BoardDto.builder()
//                    .id(board.getId())
//                    .author(board.getAuthor())
//                    .title(board.getTitle())
//                    .content(board.getContent())
//                    .createdDate(board.getCreatedDate())
//                    .build();
//            boardDtoList.add(boardDto);
//        }
//        return reviews.stream();
//        .map(review -> new TalkTalkReplyDto(review.getBoard_no(), review.getId()))
//                .collect(Collectors.toList());
//    }
//
//
//
//    public List<TalkTalkReviewDto> list(){
//        return talktalkRepository.findAll();
//    } //게시글 목록
//    public void insert(TalkTalkReviewDto dto)throws Exception{
//        //파일 저장 처리
//        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
//        UUID uuid = UUID.randomUUID();
//        String fileName = uuid + "_" + file.getOriginalFilename();
//        File savaFile = new File(projectPath, fileName);
//        file.transferTo(savaFile);
//
//        //파일을 DB에 저장
//        //board.setFilename(fileName);
//        //board.setFilepath("/files/" + fileName);
//
//        TalkTalkRepository.save(review);
//    };    //게시글 추가
//    public GuestbookDTO view(int idx);    //게시글 상세화면
//    public void update(GuestbookDTO dto);    //게시글 수정
//    public void delete(int idx);            //게시글 삭제



    public void write(TalkTalkReviewEntity talkTalkReviewEntity){
        //엔티티를 이 안에 넣어주는 것
        talktalkRepository.save(talkTalkReviewEntity);
    }

//    게시글을 불러올 메소드 생성
    public List<TalkTalkReviewEntity> reviewList(){
        return talktalkRepository.findAll();
    }


//    public boolean registerBoard(BoardDTO params);
//    public BoardDTO getBoardDetail(Long idx);
//    public boolean deleteBoard(Long idx);
//    public List<BoardDTO> getBoardList();
//    public boolean cntPlus(Long idx);
}
