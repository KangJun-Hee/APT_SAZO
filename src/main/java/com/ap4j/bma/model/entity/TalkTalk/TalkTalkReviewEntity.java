package com.ap4j.bma.model.entity.TalkTalk;

import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//Entity란? Entity는 데이터베이스에 테이블과 같은 의미로 쓰이며 자바에서는 테이블을 엔티티라고 한다.


@NoArgsConstructor
////@AllArgsConstructor
////@Builder
//@Getter
//@Setter
@Table(name="APARTMENT_REVIEW")
@Entity
@Data
public class TalkTalkReviewEntity{


        /*
         * @GeneratedValue(strategy = GenerationType.IDENTITY)
         * - 기본 키가 자동으로 할당되도록 설정하는 어노테이션이다.
         * - 기본 키 할당 전략을 선택할 수 있는데, 키 생성을 데이터베이스에 위임하는 IDENTITY 전략 사용
         */
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer board_no;

        @Column
        private String email;

        @Column
        private int id;

        @Column(name = "CONTENT", nullable = false,length = 1000)
        private String content;

        @CreationTimestamp
        private LocalDateTime create_at;

//        @ManyToOne(fetch = FetchType.LAZY)
//        @JoinColumn(name = "user_id")
//        private User user;
//
//        @OneToMany(mappedBy = "posts", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
//        @OrderBy("id asc") // 댓글 정렬
//        private List<Comment> comments;

        @Builder
        public TalkTalkReviewEntity(int board_no,String email,int id,String content,LocalDateTime create_at) {
                this.board_no = board_no;
                this.email = email;
                this.id = id;
                this.content = content;
                this.create_at = create_at;
        }
//
//        public TalkTalkReviewDto toDTO(){
//                return TalkTalkReviewDto.builder()
//                        .board_no(board_no)
//                        .email(email)
//                        .id(id)
//                        .content(content)
//                        .create_at(create_at)
//                        .build();
//        }
}
