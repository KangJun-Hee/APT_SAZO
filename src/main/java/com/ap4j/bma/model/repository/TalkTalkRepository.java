package com.ap4j.bma.model.repository;

import com.ap4j.bma.model.entity.TalkTalk.TalkTalkReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TalkTalkRepository extends JpaRepository<TalkTalkReviewEntity,Integer>{

    @Query("select a from TalkTalkReviewEntity a where a.id =?1")
    TalkTalkReviewEntity aptIdtoReview(Long id);

    @Query("SELECT r FROM TalkTalkReviewEntity r ORDER BY r.create_at DESC")
    List<TalkTalkReviewEntity> findAllDesc();

//    Optional<MemberEntity> findByEmail(String email);   // email로 회원 정보 조회
//    @Override
//    Optional<MemberEntity> findById(Long id);          // id로 회원 정보 조회
//
//    boolean existsByEmail(String email);    // exists : 해당 데이터가 DB에 존재하는지 확인하기 위해 사용
//
//    Optional<MemberEntity> findByNameAndTel(String name, String tel);   // email 찾기
//
//    Optional<MemberEntity> findByEmailAndTel(String email, String tel);   // pwd 찾기
}
