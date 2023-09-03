//package com.ap4j.bma.model.entity.TalkTalk;
//
//import lombok.*;
//
//import java.util.Date;
//
//@Getter
//@Setter
//@ToString
//public class TalkTalkReplyDto {
//
//private int reply_no;
//private String email;
//private int board_no;
//private String content;
//private Date create_at= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
//
////    private List<CommentResponseDto> comments;
//
//        public TalkTalkReplyEntity toEntity() {
//                return TalkTalkReviewEntity.builder();
//                .reply_no(reply_no)
//                        .email(email)
//                        .board_no(board_no)
//                        .content(content)
//                        .create_at();
//        }
//
//
//
//        /* Entity -> Dto*/
//        @Builder
//        public TalkTalkReplyDto(int reply_no, String email, int board_no, String content, Date create_at) {
//                this.reply_no = reply_no;
//                this.email = email;
//                this.board_no = board_no;
//                this.content = content;
//                this.create_at = create_at;
//        }
//}