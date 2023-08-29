package com.ap4j.bma.model.entity.apt;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AptDTO {
    private String district;            // 구주소
    private String address;             // 번지
    private String complexName;         // 아파트 이름
    private String roadName;            // 도로명
    private Double longitude;           // 경도
    private Double latitude;            // 위도
}
