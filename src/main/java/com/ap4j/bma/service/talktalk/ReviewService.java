package com.ap4j.bma.service.talktalk;

//import com.ap4j.bma.model.entity.TalkTalk.TalkTalkReplyDto;
//import com.ap4j.bma.model.entity.TalkTalk.TalkTalkReviewDto;

import com.ap4j.bma.model.entity.TalkTalk.TalkTalkReviewEntity;
import com.ap4j.bma.model.repository.TalkTalkRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
//서비스 -> 컨트롤러에서 이용
@Service
public class ReviewService {
    @Autowired
    private TalkTalkRepository talktalkRepository;

    public void write(TalkTalkReviewEntity talkTalkReviewEntity){
        //엔티티를 이 안에 넣어주는 것
        log.info("리뷰엔티티 write 실행");
        log.info("입력받은 내용의 엔티티객체" + talkTalkReviewEntity.toString());
        talktalkRepository.save(talkTalkReviewEntity);
    }

    //    게시글을 불러올 메소드 생성
    public List<TalkTalkReviewEntity> reviewList(){
        log.info("리뷰서비스의 reviewList메서드 실행~ findAll()리턴~");
        return talktalkRepository.findAllDesc();
    }


//    public List<TalkTalkReviewDto> aptIdtoReview(Long id) {
//        return ;
//    }
}
