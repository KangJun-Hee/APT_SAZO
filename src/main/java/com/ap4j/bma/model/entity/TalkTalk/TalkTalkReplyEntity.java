//package com.ap4j.bma.model.entity.TalkTalk;
//
//import com.ap4j.bma.model.entity.member.MemberEntity;
//import lombok.Builder;
//import org.hibernate.annotations.CreationTimestamp;
//import lombok.*;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//
////@Builder
////@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Table(name = "APARTMENT_REPLY")
//@Entity
//public class TalkTalkReplyEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int reply_no;
//
//    @Column
//    private String email;
//
//    @Column(nullable = false)
//    private int board_no;
//
//    @Column
//    private String content;
//
//    @CreationTimestamp
//    private LocalDateTime create_at;
//
//    @ManyToOne
//    @JoinColumn(name = "board_no")
//    private TalkTalkReviewEntity talkTalkReviewEntity;
//
//    @ManyToOne
//    @JoinColumn(name = "email")
//    private MemberEntity memberEntity; // 작성자
//
//
//    @Builder
//    public TalkTalkReplyEntity(int reply_no,String email,int board_no,String content) {
//        this.reply_no = reply_no;
//        this.email = email;
//        this.board_no = board_no;
//        this.content = content;
//    }
//
//    public TalkTalkReplyDto toDTO(){
//        return TalkTalkReplyDto.builder()
//                .reply_no(reply_no)
//                .email(email)
//                .board_no(board_no)
//                .content(content)
//                .create_at(create_at)
//                .build();
//    }
//}