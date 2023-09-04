package com.ap4j.bma.service.apartment;

import com.ap4j.bma.model.entity.apt.*;

import com.ap4j.bma.model.repository.AptRealTradeRepository;
import com.ap4j.bma.model.repository.AptRepository;

import com.ap4j.bma.model.repository.HangJeongDongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ApartmentServiceImpl implements ApartmentService {

    @Autowired
    private AptRepository aptRepository;
    @Autowired
    private AptRealTradeRepository aptRealTradeRepository;
    @Autowired
    private HangJeongDongRepository hangJeongDongRepository;

    /**
     * DB값 가져와서 js에 넘겨주기 (경도 위도 검색해서 값 가져오기 위해서)
     */
    public List<AptDTO> aptList() {
        List<AptDTO> AptDTOList = new ArrayList<>();
        List<AptEntity> aptEntityList = aptRepository.findAll();

        for (AptEntity aptEntity : aptEntityList) {
            AptDTO aptDTO = AptDTO.builder().
                    apartmentName(aptEntity.getApartmentName()).
                    roadName(aptEntity.getRoadName()).
                    district(aptEntity.getDistrict()).
                    address(aptEntity.getAddress()).
                    build();
            AptDTOList.add(aptDTO);
        }
        return AptDTOList;
    }

    /**
     * 가져온 좌표값 DB에 저장하기
     */
    @Transactional
    public void updateApt(String roadName, Double latitude, Double longitude) {
        AptEntity aptEntity = aptRepository.findByRoadName(roadName);
        if (aptEntity != null) {
            AptEntity updateApt = AptEntity.builder().
                            id(aptEntity.getId()).
                            district(aptEntity.getDistrict()).
                            address(aptEntity.getAddress()).
                            apartmentName(aptEntity.getApartmentName()).
                            roadName(aptEntity.getRoadName()).
                            latitude(latitude).
                            longitude(longitude).
                            build();
            aptRepository.save(updateApt);

        }
    }
    @Transactional
    public Long updateAptForReview(String roadName, Double latitude, Double longitude) {
        AptEntity aptEntity = aptRepository.findByRoadName(roadName);
        if (aptEntity != null) {
            AptEntity updateApt = AptEntity.builder().
                    id(aptEntity.getId()).
                    district(aptEntity.getDistrict()).
                    address(aptEntity.getAddress()).
                    apartmentName(aptEntity.getApartmentName()).
                    roadName(aptEntity.getRoadName()).
                    latitude(latitude).
                    longitude(longitude).
                    build();
            aptRepository.save(updateApt);

            return updateApt.getId();// 리뷰작성시 가져갈 아파트아이디
        }
        return null;//아파트못찾으면 널 리턴
    }

    /**
     * ajax통신으로 가져온 화면의 좌표 범위의 아파트 데이터만 가져오는 메서드
     */
    public List<AptDTO> findAptListBounds(Double southWestLat, Double southWestLng, Double northEastLat, Double northEastLng) {

        List<AptDTO> aptDTOList = new ArrayList<>();
        List<AptEntity> aptEntityList = aptRepository.findAptListBounds(southWestLat, southWestLng, northEastLat, northEastLng);

        for (AptEntity aptEntity : aptEntityList) {
            AptDTO aptDTO = AptDTO.builder().
                    apartmentName(aptEntity.getApartmentName()).
                    district(aptEntity.getDistrict()).
                    address(aptEntity.getAddress()).
                    roadName(aptEntity.getRoadName()).
                    longitude(aptEntity.getLongitude()).
                    latitude(aptEntity.getLatitude()).
                    build();
            aptDTOList.add(aptDTO);
        }
        return aptDTOList;
    }

    /**
     * 도로명에 맞는 실거래가 데이터 가져오는 메서드
     */
    public List<AptRealTradeDTO> findByRoadName(String roadName) {
        List<AptRealTradeDTO> aptRealTradeDTOList = new ArrayList<>();
        List<AptRealTradeEntity> aptRealTradeEntityList = aptRealTradeRepository.findByRoadName(roadName);

        for (AptRealTradeEntity aptRealTradeEntity : aptRealTradeEntityList) {
            AptRealTradeDTO aptRealTradeDTO = AptRealTradeDTO.builder().
                    roadName(aptRealTradeEntity.getRoadName()).
                    area(aptRealTradeEntity.getArea()).
                    transactionAmount(aptRealTradeEntity.getTransactionAmount()).
                    contractYearMonth(aptRealTradeEntity.getContractYearMonth()).
                    contractDate(aptRealTradeEntity.getContractDate()).
                    floor(aptRealTradeEntity.getFloor()).
                    constructionYear(aptRealTradeEntity.getConstructionYear()).
                    build();
            aptRealTradeDTOList.add(aptRealTradeDTO);
        }
        return aptRealTradeDTOList;
    }

    /**
     * 아파트명 또는 도로명으로 검색시 아파트 정보 가져오기
     */
    public AptDTO findByKeyword(String keyword) {
        AptEntity aptEntity = aptRepository.findByKeyword(keyword);

        AptDTO aptKeyword = null;

        if(aptEntity != null) {
            aptKeyword = AptDTO.builder().
                    apartmentName(aptEntity.getApartmentName()).
                    district(aptEntity.getDistrict()).
                    address(aptEntity.getAddress()).
                    roadName(aptEntity.getRoadName()).
                    longitude(aptEntity.getLongitude()).
                    latitude(aptEntity.getLatitude()).
                    build();
        }

        return aptKeyword;
    }

    /**
     * ajax통신으로 가져온 화면의 좌표 범위의 행정동 데이터만 가져오는 메서드
     * */
    public List<HangJeongDongDTO> findHJDListBounds(Double southWestLat, Double southWestLng, Double northEastLat, Double northEastLng, Integer zoomLevel) {
        List<HangJeongDongDTO> hjdList = new ArrayList<>();
        List<HangJeongDongEntity> hjdEntityList = new ArrayList<>();

        if (zoomLevel == null || zoomLevel <= 5) {
            return hjdList; // 빈 리스트 반환
        }

        if (zoomLevel <= 6) { // 동
            hjdEntityList = hangJeongDongRepository.findHJDListZoomLevel6(southWestLat, southWestLng, northEastLat, northEastLng);
        } else if (zoomLevel <= 8) { // 구
            hjdEntityList = hangJeongDongRepository.findHJDListZoomLevel7(southWestLat, southWestLng, northEastLat, northEastLng);
        } else if (zoomLevel >= 9) { // 시도
            hjdEntityList = hangJeongDongRepository.findHJDListZoomLevel8();
        }

        if (hjdEntityList != null) {
            for (HangJeongDongEntity hjdEntity : hjdEntityList) {
                HangJeongDongDTO hjdDTO = HangJeongDongDTO.builder().
                        siDo(hjdEntity.getSiDo()).
                        siGunGu(hjdEntity.getSiGunGu()).
                        eupMyeonDong(hjdEntity.getEupMyeonDong()).
                        eupMyeonRiDong(hjdEntity.getEupMyeonRiDong()).
                        longitude(hjdEntity.getLongitude()).
                        latitude(hjdEntity.getLatitude()).
                        build();
                hjdList.add(hjdDTO);
            }
        }
        return hjdList;
    }

}
