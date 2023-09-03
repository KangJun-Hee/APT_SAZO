package com.ap4j.bma.model.entity.TalkTalk;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 게시글 정보를 리턴할 응답(Response) 클래스
 * Entity 클래스를 생성자 파라미터로 받아 데이터를 Dto로 변환하여 응답
 * 별도의 전달 객체를 활용해 연관관계를 맺은 엔티티간의 무한참조를 방지
 */
@Data
@NoArgsConstructor
public class TalkTalkReviewDto {

    private int board_no;
    private String email;//회원아이디를 위한 이멜
    private int id; //아파트 id번호
    private String content;
    private LocalDateTime create_at;

//    private List<CommentResponseDto> comments;

    public TalkTalkReviewEntity toEntity() {
        return TalkTalkReviewEntity.builder()
                .board_no(board_no)
                .email(email)
                .id(id)
                .content(content)
                .create_at(create_at)
                .build();
    }



//    /* Entity -> Dto*/
//    @Builder
//    public TalkTalkReviewDto(int board_no, String email, int id, String content, Date create_at) {
//        this.board_no = board_no;
//        this.email = email;
//        this.id = id;
//        this.content = content;
//        this.create_at = create_at;
//    }
}