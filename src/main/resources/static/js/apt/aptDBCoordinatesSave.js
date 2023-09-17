/** 아파트 실거래가 DB에서 주소값을 가져와서 그에 맞는 좌표(경도, 위도)를 검색해서 DB에 다시 저장해주는 javascript파일 입니다.*/


/** 주소검색 가능하게 해주는 kakaoMapApi 객체 */
var geocoder = new kakao.maps.services.Geocoder();
var pageSize = 50; // 한 페이지당 처리할 데이터의 개수
var totalPages = Math.ceil(aptList.length / pageSize); // 전체 페이지 수 계산

/** 페이지별로 데이터 처리 */
async function processPagesSequentially(page) {
    if (page > totalPages) {
        console.log("모든 페이지가 성공적으로 처리되었습니다.");
        return;
    }

    await processPage(page);

    // 다음 페이지로 이동 (setTimeout 함수를 사용해서 딜레이를 걸어줘야 kakaoMapApi 트래픽 오류가 나지 않는다)
    await new Promise((resolve) => {
        setTimeout(resolve, 4000); // 4초 대기
    });

    await processPagesSequentially(page + 1);
}

/** 페이지별로 처리하는 함수 */
async function processPage(page) {
    var startIndex = (page - 1) * pageSize;
    var endIndex = Math.min(startIndex + pageSize, aptList.length);

    for (var i = startIndex; i < endIndex; i++) {
        var apt = aptList[i];
        var roadName = apt.roadName;
        searchAddress(roadName);
    }
}

function searchAddress(roadName) {
    return new Promise((resolve) => {
        geocoder.addressSearch(roadName, function(result, status) {
            if (status === kakao.maps.services.Status.OK) {
                var latitude = result[0].y; // 위도
                var longitude = result[0].x; // 경도
                // AJAX 요청을 통해 데이터 서버로 전송
                $.ajax({
                    url: "/saveCoordinates",
                    method: "POST",
                    data: {
                        latitude: latitude,
                        longitude: longitude,
                        roadName: roadName
                    },
                    success: function(response) {
                        resolve();
                    },
                    error: function(error) {
                        resolve();
                    }
                });
            } else {
                resolve();
            }
        });
    });
}

// 첫 번째 페이지부터 시작
processPagesSequentially(1);