package com.ap4j.bma.model.entity.meamulReg;

import com.ap4j.bma.model.entity.member.MemberEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MaemulReg")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@DynamicInsert
public class MaemulRegEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //매물 등록 번호
    private String nickname; //등록한 사람 정보 , 유니크키

    //매물 등록 페이지
    private String address; //도로명 주소
    private String APT_name; //아파트 이름

    private String buildingUsage; //건축물 용도

    private Integer numberOfRooms; //방 수

    private Integer numberOfBathrooms; //욕실 수

    private Integer floorNumber; //해당층

    private Integer totalFloors; //건물층
    private Double privateArea; //전용면적
    private Double supplyArea; //공급면적
    private String direction; //방향
    private String heatingType;//난방종류
    private String Elevator;//엘리베이터
    private String Parking;//주차가능여부 (엘베랑 같은 이유)


    private Integer totalParking;//총 주차대수
    private String shortTermRental;//단기임대
    private String availableMoveInDate;//입주가능일

    @ColumnDefault("0")
    private Integer loanAmount;//융자금


    //거래 유형
    private String tradeType;//거래 유형

    //월세
    @ColumnDefault("0")
    private Integer monthlyForRent; //보증금
    @ColumnDefault("0")
    private Integer monthlyRent; //월세


    //전세
    @ColumnDefault("0")
    private Integer depositForLease; //희망 전세

    @ColumnDefault("0")
    private Integer managementFee;//전 월세 관

    //매매
    @Column(columnDefinition = "INT")
    @ColumnDefault("0")
    private Integer SellingPrice;//희망매매가

    //상세정보 페이지
    private String title; //제목
    private String content; //상세정보

    //체크박스
    private String features; //특징
    private String optional;//옵션
    private String security;//보안, 안전시설

    //매물 이미지 추가

    //좌표
    private Double longitude; //경도
    private Double latitude; //위도

    //등록일자
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nickname", referencedColumnName = "nickname", insertable = false, updatable = false)
    private MemberEntity memberEntity;

    //이미지 매핑
//    @OneToMany(mappedBy = "maemulID", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<MaemulPhotoEntity> maemulPhotos = new ArrayList<>();

}