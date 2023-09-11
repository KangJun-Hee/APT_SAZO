/** 매물 등록시 도로명 주소 가져와서 자동으로 좌표값 검색후 데이터 베이스에 저장하는 js입니다.  */
console.log(maemulRegEntity);
var geocoder = new kakao.maps.services.Geocoder();
var roadName = maemulRegEntity.address;
var maemulId = maemulRegEntity.id;

console.log("주소 : " + roadName);
console.log("매물번호 : " + maemulId);

searchAddress(roadName, maemulId);

function searchAddress(roadName , maemulId) {

    geocoder.addressSearch(roadName, function(result, status) {
        if (status === kakao.maps.services.Status.OK) {
            var latitude = result[0].y; // 위도
            var longitude = result[0].x; // 경도
             console.log("주소 : " + roadName);
                console.log("위도 : " + latitude);
                console.log("경도 : " + longitude);
            // AJAX 요청을 통해 데이터 서버로 전송
            $.ajax({
                url: "/confirmation",
                method: "POST",
                data: {
                    latitude: latitude,
                    longitude: longitude,
                    maemulId: maemulId
                },
                success: function(response) {
                    console.log(latitude);
                    console.log(longitude);
                    console.log(maemulId);
                },
                error: function(error) {
                    console.log("전송실패");
                    console.log("HTTP 상태 코드: " + error.status); // HTTP 상태 코드 출력
                    console.log("오류 메시지: " + error.statusText); // 오류 메시지 출력
                    console.log("응답 내용: " + error.responseText); // 응답 내용 출력
                }
            });
        } else {
            console.log("데이터 없음");
        }
    });

}
