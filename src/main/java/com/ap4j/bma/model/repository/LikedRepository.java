package com.ap4j.bma.model.repository;

import com.ap4j.bma.model.entity.meamulReg.MaemulRegEntity;
import com.ap4j.bma.model.entity.member.LikedEntity;
import com.ap4j.bma.model.entity.member.MemberDTO;
import com.ap4j.bma.model.entity.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikedRepository extends JpaRepository<LikedEntity, Long> {

    @Query("SELECT l FROM LikedEntity l " +
            "JOIN MaemulRegEntity mr ON l.nickname = mr.nickname " +
            "JOIN MemberEntity m ON l.nickname = m.nickname " +
            "WHERE l.nickname = :nickname")
    List<LikedEntity> findLikedByNickname(@Param("nickname") String nickname);

    @Query("SELECT mr FROM MaemulRegEntity mr " +
            "JOIN LikedEntity l ON mr.nickname = l.nickname " +
            "JOIN MemberEntity m ON mr.nickname = m.nickname " +
            "WHERE l.road_name = :roadName")
    List<MaemulRegEntity> findMaemulByRoadName(@Param("roadName") String roadName);

    // 사용자 닉네임과 도로 주소를 이용한 중복 체크 쿼리
    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END " +
            "FROM LikedEntity l " +
            "WHERE l.nickname = :nickname AND l.road_name = :roadName")
    boolean existsByNicknameAndRoadName(@Param("nickname") String nickname, @Param("roadName") String roadName);
}
