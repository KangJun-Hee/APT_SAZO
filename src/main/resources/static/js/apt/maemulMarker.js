/** 매물 마커 생성 js */

// 카카오 지도 불러오기
var container = document.getElementById('map');
var options = {
    center: new kakao.maps.LatLng(37.535814, 127.008644), // 초기 맵 중심 좌표
    level: 5 // 초기 줌 레벨
};
var map = new kakao.maps.Map(container, options);

// 새로운 div 엘리먼트를 생성하여 줌 컨트롤 역할을 할 컨테이너를 만듭니다
var zoomControlContainer = document.getElementById('zoomControl');

var zoomInButton = document.getElementById('buttonp');
zoomInButton.textContent = '+';
zoomInButton.addEventListener('click', function () {
    map.setLevel(map.getLevel() - 1, { animate: true });
});

// 줌 아웃 버튼을 만듭니다
var zoomOutButton = document.getElementById('buttonm');
zoomOutButton.textContent = '-';
zoomOutButton.addEventListener('click', function () {
    map.setLevel(map.getLevel() + 1, { animate: true });
});


// 컨테이너에 버튼을 추가합니다
zoomControlContainer.appendChild(zoomInButton);
zoomControlContainer.appendChild(zoomOutButton);


// 클러스터
var clusterer = new kakao.maps.MarkerClusterer({
    map: map,
    gridSize: 300,
    averageCenter: false,
    minLevel: 14
});

// 생성된 마커 저장 객체
var existingMarkers = {};

// 로그인 시 회원정보, 관심매물데이터 정보 가져오기위한 변수
var loginMember = {};
var likedEntityList = {};

// 마커 생성 함수
function createMarker(position, markerContent, responseData) {
    var markerKey = position.toString();

    var imageSrc = '/img/mapDetailAndAPTList/houseMarker.png',
        imageSize = new kakao.maps.Size(32, 32),
        imageOption = { offset: new kakao.maps.Point(20, 20) };
    var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);

    // 마커가 생성되어 있지 않다면 마커 생성
    if (!existingMarkers[markerKey]) {
        var marker = new kakao.maps.Marker({
            position: position,
            image: markerImage,
            clickable: true
        });

        marker.responseData = responseData; // 첫 화면에서 사이드바 띄워줄 때 사용함

        var overlayContent = markerContent;

        // 오버레이 생성
        var overlay = new kakao.maps.CustomOverlay({
            content: overlayContent,
            map: map,
            position: marker.getPosition(),
            zIndex: 9999
        });

        marker.overlay = overlay;
        marker.overlay.setMap(null); // 오버레이는 초기에 닫혀있는 상태

        // 마커 클릭시 오버레이 열리는 이벤트
        kakao.maps.event.addListener(marker, 'click', function () {
            closeOtherOverlays(); // 다른 오버레이 닫기
            openOverlay(marker.overlay); // 오버레이 열기
            clearSidebar(); // 사이드바 초기화

            var bounds = map.getBounds();
            var southWest = bounds.getSouthWest();
            var northEast = bounds.getNorthEast();
            var currentZoomLevel = map.getLevel(); // 현재 줌 레벨 가져오기

            var dataToSend = {
                southWestLat: southWest.getLat(),
                southWestLng: southWest.getLng(),
                northEastLat: northEast.getLat(),
                northEastLng: northEast.getLng(),
                zoomLevel: currentZoomLevel,
                address : responseData.address
            }
            $.ajax({
                type: 'POST',
                url: '/map/map',
                data: dataToSend,
                success: function (response) {
                    likedEntityList = response.likedEntityList;
                    if(response.maemulClickList) {
                        updateSidebar(response.maemulClickList);
                    }
                }

            });

        });

        clusterer.addMarker(marker); // 클러스터러에 마커 추가
        existingMarkers[markerKey] = marker; // 생성된 마커를 마커리스트에 저장
    }
}

/* 거래유형 옵션 */
var tradeType = [];

$("#flexCheckAll").change(function () {
    updateSelectedTradeTypes(null, this.checked);
    sendToServer();
});

$("#flexCheckSale").change(function () {
    updateSelectedTradeTypes("매매", this.checked);
    sendToServer();
});

$("#flexCheckLease").change(function () {
    updateSelectedTradeTypes("전세", this.checked);
    sendToServer();
});

$("#flexCheckMonthly").change(function () {
    updateSelectedTradeTypes("월세", this.checked);
    sendToServer();
});

$("#flexCheckShortTerm").change(function () {
    updateSelectedTradeTypes("단기임대", this.checked);
    sendToServer();
});

function updateSelectedTradeTypes(type, isChecked) {
    if (isChecked) {
        // 체크된 경우 배열에 추가
        tradeType.push(type);
    } else {
        // 체크 해제된 경우 배열에서 제거
        tradeType = tradeType.filter(item => item !== type);
    }

}

/* 방개수 옵션 */
var roomCount;

$('input[name="searchRoomCount"]').on('change', function () {
    roomCount = $(this).val();
    sendToServer();
});

/* 욕실 수 옵션 */
var bathRoomCount;
$('input[name="searchBathRoomCount"]').on('change', function () {
    bathRoomCount = $(this).val();
    sendToServer();
});

/* 층 수 옵션 */
var floorCount;
$('input[name="searchFloorCount"]').on('change', function () {
    floorCount = $(this).val();
    sendToServer();
});

/* 관리비 옵션 */
var manageFee;
$('input[name="searchMaintenance"]').on('change', function () {
    manageFee = $(this).val();
    sendToServer();
});

/* 엘리베이터 옵션 */
var elevator;
$('input[name="searchElevator"]').on('change', function () {
    var selectedValue = $(this).val();
    elevator = selectedValue === '' ? null : selectedValue;
    sendToServer();
});

/* 방향 옵션 */
var direction = [];

$("#searchDirectionAll").change(function () {
    updateSelectedDirection(null, this.checked);
    sendToServer();
});

$("#maesearchDirection_C00702").change(function () {
    updateSelectedDirection("동향", this.checked);
    sendToServer();
});

$("#maesearchDirection_C00703").change(function () {
    updateSelectedDirection("서향", this.checked);
    sendToServer();
});

$("#maesearchDirection_C00704").change(function () {
    updateSelectedDirection("남향", this.checked);
    sendToServer();
});

$("#maesearchDirection_C00705").change(function () {
    updateSelectedDirection("북향", this.checked);
    sendToServer();
});

$("#maesearchDirection_C00706").change(function () {
    updateSelectedDirection("남동향", this.checked);
    sendToServer();
});
$("#maesearchDirection_C00707").change(function () {
    updateSelectedDirection("남서향", this.checked);
    sendToServer();
});
$("#maesearchDirection_C00708").change(function () {
    updateSelectedDirection("북동향", this.checked);
    sendToServer();
});
$("#maesearchDirection_C00709").change(function () {
    updateSelectedDirection("북서향", this.checked);
    sendToServer();
});
function updateSelectedDirection(type, isChecked) {
    if (isChecked) {
        // 체크된 경우 배열에 추가
        direction.push(type);
    } else {
        // 체크 해제된 경우 배열에서 제거
        direction = direction.filter(item => item !== type);
    }

}

/* 주차가능 옵션 */
var parking;
$('input[name="searchParkCount"]').on('change', function () {
    var selectedValue = $(this).val();
    parking = selectedValue === '' ? null : selectedValue;
    sendToServer();
});

/* 단기임대 옵션 */
var rental;
$('input[name="rentalCount"]').on('change', function () {
    var selectedValue = $(this).val();
    rental = selectedValue === '' ? null : selectedValue;
    sendToServer();
});

/* 매매가 옵션 */
var maemaeStart;
var maemaeEnd;
$(".searchMaemaeButton").click(function() {

    maemaeStart = $("input[name='maemaeStart']").val();
    maemaeEnd = $("input[name='maemaeEnd']").val();

    sendToServer();

    maemaeStart = null;
    maemaeEnd = null;

});

/* 전세가 옵션 */
var jeonseStart;
var jeonseEnd;
$(".searchJeonseButton").click(function() {

    jeonseStart = $("input[name='jeonseStart']").val();
    jeonseEnd = $("input[name='jeonseEnd']").val();

    sendToServer();

    jeonseStart = null;
    jeonseEnd = null;

});

/* 보증금 옵션 */
var bozugStart;
var bozugEnd;
$(".searchBozugButton").click(function() {

    bozugStart = $("input[name='bozugStart']").val();
    bozugEnd = $("input[name='bozugEnd']").val();

    sendToServer();

    bozugStart = null;
    bozugEnd = null;

});


/* 면적 옵션 */
var selectedValues = [];
var minValue;
var maxValue;
$('.btn-check').on('change', function() {
    if (this.checked) {
        selectedValues.push(Number(this.value)); // 선택된 값을 배열에 추가
    } else {
        // 체크가 해제된 경우 배열에서 제거
        var index = selectedValues.indexOf(Number(this.value));
        if (index !== -1) {
            selectedValues.splice(index, 1);
        }
    }

    if (selectedValues.length === 1) {
        // 1개 선택된 경우, 선택된 값 서버로 전송
        maxValue = selectedValues[0];
        sendToServer();
        maxValue = null;

    } else if (selectedValues.length >= 2) {
        // 2개 이상 선택된 경우, 최솟값과 최댓값 찾아서 서버로 전송
        minValue = Math.min.apply(null, selectedValues);
        maxValue = Math.max.apply(null, selectedValues);
        sendToServer();
        minValue = null;
        maxValue = null;
    } else if(selectedValues.length === 0){
        sendToServer();

    }
});

/* 리스트 상단 최신순 가격순 면적순 필터 */

var orderType;
$(".nav-link").click(function () {
    // 선택한 버튼의 data-order 속성 값을 가져옴
    orderType = $(this).data("order");
    sendToServer();

});

// 맵 최초 로드시 마커 생성 해주는 함수
var onlyOneStart = false; // 한 번만 실행하기 위한 변수
// 맵 로드가 완료되면 실행
kakao.maps.event.addListener(map, 'tilesloaded', function () {
    // 이미 실행된 경우 함수 종료
    if (onlyOneStart) {
        return;
    }
    onlyOneStart = true; // 변수 업데이트

    var tradeTypeString = tradeType.join(",");
    var directionString = direction.join(",");

    var keyword = document.querySelector('.aSearchInput').value.replaceAll(' ', '');

    var bounds = map.getBounds();
    var southWest = bounds.getSouthWest();
    var northEast = bounds.getNorthEast();
    var currentZoomLevel = map.getLevel(); // 현재 줌 레벨 가져오기
    console.log("실행시 줌레벨 : " + currentZoomLevel);
    console.log(tradeType);
    var dataToSend = {
        southWestLat: southWest.getLat(),
        southWestLng: southWest.getLng(),
        northEastLat: northEast.getLat(),
        northEastLng: northEast.getLng(),
        zoomLevel: currentZoomLevel,
        tradeType: tradeTypeString,
        numberOfRooms: roomCount,
        numberOfBathrooms: bathRoomCount,
        floorNumber: floorCount,
        managementFee: manageFee,
        Elevator: elevator,
        direction: directionString,
        Parking: parking,
        shortTermRental: rental,
        keyword: keyword,
        rowSellingPrice: maemaeStart,
        highSellingPrice: maemaeEnd,
        rowDepositForLease: jeonseStart,
        highDepositForLease: jeonseEnd,
        rowMonthlyForRent: bozugStart,
        highMonthlyForRent: bozugEnd,
        minPrivateArea: minValue,
        maxPrivateArea: maxValue,
        orderType: orderType
    };

    $.ajax({
        type: 'POST',
        url: '/map/map',
        data: dataToSend,
        success: function (response) {
            loginMember = response.loginMember;
            likedEntityList = response.likedEntityList;

            if(response.maenulList) {
                response.maenulList.forEach(function (maemul) {
                    var markerPosition = new kakao.maps.LatLng(maemul.latitude, maemul.longitude);
                    var markerKey = markerPosition.toString();
                    var markerContent = "<div class='e-marker'>" +
                        "<div class='e-markerTitle'>" +
                        "<h3>" + maemul.apt_name + "</h3>" +
                        "</div>" +
                        "<div class='e-markerContent'>" +
                        "<p>" + maemul.address + "</p>" +
                        "</div>" +
                        "</div>";

                    if (!existingMarkers[markerKey]) {
                        createMarker(markerPosition, markerContent, maemul);
                    }

                });
                updateSidebar(response.maenulList);
            }
        }
    });
});
// 맵 이동시 현재 맵의 경계를 기준으로 데이터 요청하고 그 범위에 속하는 행정동 또는 아파트 데이터의 마커 생성
kakao.maps.event.addListener(map, 'idle', function () {
    // 행정동 오버레이 초기화
    clearHJDOverlays();
    // 마커 오버레이 초기화
    closeOtherOverlays();
    // 사이드바 초기화
    clearSidebar();

    var tradeTypeString = tradeType.join(",");
    var directionString = direction.join(",");

    var keyword = document.querySelector('.aSearchInput').value.replaceAll(' ', '');

    var bounds = map.getBounds();
    var southWest = bounds.getSouthWest();
    var northEast = bounds.getNorthEast();
    var currentZoomLevel = map.getLevel(); // 현재 줌 레벨 가져오기

    var dataToSend = {
        southWestLat: southWest.getLat(),
        southWestLng: southWest.getLng(),
        northEastLat: northEast.getLat(),
        northEastLng: northEast.getLng(),
        zoomLevel: currentZoomLevel,
        tradeType: tradeTypeString,
        numberOfRooms: roomCount,
        numberOfBathrooms: bathRoomCount,
        floorNumber: floorCount,
        managementFee: manageFee,
        Elevator: elevator,
        direction: directionString,
        Parking: parking,
        shortTermRental: rental,
        keyword: keyword,
        rowSellingPrice: maemaeStart,
        highSellingPrice: maemaeEnd,
        rowDepositForLease: jeonseStart,
        highDepositForLease: jeonseEnd,
        rowMonthlyForRent: bozugStart,
        highMonthlyForRent: bozugEnd,
        minPrivateArea: minValue,
        maxPrivateArea: maxValue,
        orderType: orderType
    };

    var likedBoolean = true;

    $.ajax({
        type: 'POST',
        url: '/map/map',
        data: dataToSend,
        success: function (response) {
            if(likedBoolean) {
                likedEntityList = response.likedEntityList;
                likedBoolean = false;
                console.log("화면이동시 likedEntityList :" + likedEntityList);
            }

            if (response.maenulList && currentZoomLevel <= 5) {
                response.maenulList.forEach(function (maemul) {

                    var markerPosition = new kakao.maps.LatLng(maemul.latitude, maemul.longitude);
                    var markerKey = markerPosition.toString();
                    var markerContent = "<div class='e-marker'>" +
                        "<div class='e-markerTitle'>" +
                        "<h3>" + maemul.apt_name + "</h3>" +
                        "</div>" +
                        "<div class='e-markerContent'>" +
                        "<p>" + maemul.address + "</p>" +
                        "</div>" +
                        "</div>";

                    if (!existingMarkers[markerKey]) {
                        createMarker(markerPosition, markerContent, maemul);
                    }

                });
                updateSidebar(response.maenulList);
            }
            if (currentZoomLevel >= 6) {  // 위 if문뒤에 else로 조건 주면 오류남 따로 조건 줘야함
                // 줌레벨 6이상시 마커 삭제
                clusterer.clear();
                for (var key in existingMarkers) {
                    if (existingMarkers.hasOwnProperty(key)) {
                        var marker = existingMarkers[key];
                        if (marker.overlay) {
                            marker.overlay.setMap(null);
                        }
                    }
                }
                existingMarkers = {};

                // 행정동으로 클러스터 생성
                response.hjdList.forEach(function (hjd) {
                    var overlayPosition = new kakao.maps.LatLng(hjd.latitude, hjd.longitude);
                    var overlayContent = "<div class='e-hjdOverlay'>";
                    if (hjd.siDo && hjd.siGunGu && hjd.eupMyeonDong && hjd.eupMyeonRiDong) {
                        overlayContent += "<p>" + hjd.eupMyeonRiDong + "</p>";
                    } else if (hjd.siDo && hjd.siGunGu && hjd.eupMyeonDong) {
                        overlayContent += "<p>" + hjd.eupMyeonDong + "</p>";
                    } else if (hjd.siDo && hjd.siGunGu) {
                        overlayContent = "<div class='e-hdjOverlaySiGunGu'>" +
                            "<p>" + hjd.siGunGu + "</p>";
                    } else if (hjd.siDo) {
                        overlayContent = "<div class='e-hdjOverlaySiDo'>" +
                            "<p>" + hjd.siDo + "</p>";
                    }
                    overlayContent += "</div>";

                    var hjdOverlay = new kakao.maps.CustomOverlay({
                        position: overlayPosition,
                        content: overlayContent,
                        map: map
                    });
                    // 생성된 오버레이를 배열에 추가
                    hjdOverlays.push(hjdOverlay);
                });

            }
        }
    });
});

// 오버레이 열기
function openOverlay(overlay) {
    if (overlay) {
        overlay.setMap(map);
    }
}

// 다른 오버레이 닫아주는 함수
function closeOtherOverlays() {
    for (var key in existingMarkers) {
        if (existingMarkers.hasOwnProperty(key)) {
            var marker = existingMarkers[key];
            if (marker.overlay) {
                marker.overlay.setMap(null);
            }
        }
    }
}

// 행정동 오버레이 초기화 함수 정의
function clearHJDOverlays() {
    // 모든 오버레이 삭제
    hjdOverlays.forEach(function (hjdOverlay) {
        hjdOverlay.setMap(null);
    });
    hjdOverlays = []; // 오버레이 배열 초기화
}
// 행정동 오버레이 배열 초기화
var hjdOverlays = [];


// 사이드바 정보 업데이트
function updateSidebar(responseData) {

    // 사이드바 컨테이너
    var sidebarContainer = document.querySelector(".sideContents ul.list-group");

    // responseData 배열을 순회하며 사이드바 항목 생성
    responseData.forEach(function (maemul) {

        // 보증금
        var monthlyForRent = null; // 이 변수 사용하면됨
        var monthlyForRentString = maemul.monthlyForRent.toString();
        var monthlyForRentSliceUk = null;
        var monthlyForRentSliceMan = null;
        if (monthlyForRentString.length <= 4) {
            monthlyForRentSliceMan = monthlyForRentString.slice(0);
        } else if (monthlyForRentString.length === 5) {
            monthlyForRentSliceUk = monthlyForRentString.charAt(0);
            monthlyForRentSliceMan = monthlyForRentString.slice(1);
        } else if (monthlyForRentString.length === 6) {
            monthlyForRentSliceUk = monthlyForRentString.slice(0,2);
            monthlyForRentSliceMan = monthlyForRentString.slice(2);
        } else if (monthlyForRentString.length === 7) {
            monthlyForRentSliceUk = monthlyForRentString.charAt(0,3);
            monthlyForRentSliceMan = monthlyForRentString.slice(3);
        }
        if (monthlyForRentSliceUk != null) {
            if (monthlyForRentSliceMan.charAt(0) != '0') {
                monthlyForRent = "월세 " + monthlyForRentSliceUk + "억 " + monthlyForRentSliceMan + " / ";
            } else {
                monthlyForRent = "월세 " + monthlyForRentSliceUk + "억 / ";
            }
        } else {
            monthlyForRent = "월세 " + monthlyForRentSliceMan + " / ";
        }
        // 월세
        var monthlyRent = maemul.monthlyRent; // 이 변수 사용하면됨


        // 전세
        var depositForLease = null; // 이 변수 사용하면됨
        var depositForLeaseString = maemul.depositForLease.toString();
        var depositForLeaseSliceUk = null;
        var depositForLeaseSliceMan = null;
        if(depositForLeaseString.length === 5) {
            depositForLeaseSliceUk = depositForLeaseString.charAt(0);
            depositForLeaseSliceMan = depositForLeaseString.slice(1);
        } else if (depositForLeaseString.length === 6) {
            depositForLeaseSliceUk = depositForLeaseString.slice(0,2);
            depositForLeaseSliceMan = depositForLeaseString.slice(2);
        } else if (depositForLeaseString.length === 7) {
            depositForLeaseSliceUk = depositForLeaseString.slice(0,3);
            depositForLeaseSliceMan = depositForLeaseString.slice(3);
        }
        if(depositForLeaseSliceUk != null) {
            if(depositForLeaseSliceMan.charAt(0) != '0') {
                depositForLease = "전세 " + depositForLeaseSliceUk + "억 " + depositForLeaseSliceMan + "만원";
            } else {
                depositForLease = "전세 " + depositForLeaseSliceUk + "억";
            }
        } else {
            depositForLease = "전세 " + depositForLeaseSliceMan + "만원";
        }


        // 매매
        var sellingPrice = null;
        var sellingPriceString = maemul.sellingPrice.toString();
        var sellingPriceSliceUk = null;
        var sellingPriceSliceMan = null;
        if(sellingPriceString.length === 5) {
            sellingPriceSliceUk = sellingPriceString.charAt(0);
            sellingPriceSliceMan = sellingPriceString.slice(1);
        } else if (sellingPriceString.length === 6) {
            sellingPriceSliceUk = sellingPriceString.slice(0,2);
            sellingPriceSliceMan = sellingPriceString.slice(2);
        } else if (sellingPriceString.length === 7) {
            sellingPriceSliceUk = sellingPriceString.slice(0,3);
            sellingPriceSliceMan = sellingPriceString.slice(3);
        } else if (sellingPriceString.length === 8) {
            sellingPriceSliceUk = sellingPriceString.slice(0,4);
            sellingPriceSliceMan = sellingPriceString.slice(4);
        }
        if(sellingPriceSliceUk != null) {
            if(sellingPriceSliceMan.charAt(0) != '0') {
                sellingPrice = "매매 " + sellingPriceSliceUk + "억 " + sellingPriceSliceMan + "만원";
            } else {
                sellingPrice = "매매 " + sellingPriceSliceUk + "억";
            }
        } else {
            sellingPrice = "매매 " + sellingPriceSliceMan + "만원";
        }

        // 새로운 li 요소 생성
        var listItem = document.createElement("li");
        listItem.className = "list-group-item a";
        listItem.style = "";

        // details 페이지에 넘겨줄 닉네임
        var nickname = null;
        if (loginMember) {
            nickname = loginMember.nickname;
        }

        // 새로운 a 요소 생성
        var anchor = document.createElement("a");
        anchor.href = "/details/" + maemul.id + "?nickname=" + nickname;
        anchor.className = "abox";
        anchor.target = "";

        // a 요소 내부의 내용 생성
        anchor.innerHTML = `
            <div class="aimg_area">
                <div class="athumb">
                    <img src="https://www.bdsplanet.com/common/thumbs.ytp?filepath=/COMP_20190619150536000003/upload/realty/RCODE_20230822163534000002/&amp;filename=1692689858337_106.jpg" alt="">
                </div>
            </div>
            <div class="ainfo_area">
                <div class="in">
                    <h5 class="ii loc_title">
                        <span class="payf_num_b">
                            ${
            maemul.monthlyForRent != 0 ? `${monthlyForRent}${monthlyRent}` :
                maemul.depositForLease != 0 ? depositForLease :
                    sellingPrice
        }
                        </span>
                    </h5>
                    <div class="ii loc_ii01">
                        <span class="loc">${maemul.apt_name}</span>
                    </div>
                    <div class="ii etc_txt">
                        <span class="txt01">${maemul.supplyArea}/${maemul.privateArea}㎡</span>
                        <span class="txt01">${maemul.floorNumber}/${maemul.totalFloors}층</span>
                        <span class="txt01">방${maemul.numberOfRooms}/욕실${maemul.numberOfBathrooms}</span>
                    </div>
                    <div class="ii etc_info">
                        <span class="txt01">${maemul.title}</span>
                    </div>
                </div>
                <div class="bd_hashtag">

                </div>
            </div>
        `;

        // 하트 버튼 추가
        var heartButton = document.createElement("div");
        heartButton.className = "aHeartBtnInList";
        heartButton.innerHTML = `
            <button class="aHeartBtn">
                <img style="width:15px;margin-bottom:2px;" src="/img/mapDetailAndAPTList/aHeartBtn3.png">
            </button>
        `;

        // 로그인 시 관심매물에 등록된 데이터와 비교해서 하트색상 결정
        if(likedEntityList != null) {
            // 해당 회원 관심매물 갯수 카운팅
            likedCount(likedEntityList);
            for (let i = 0; i < likedEntityList.length; i++) {
                    const liked = likedEntityList[i];
                    console.log(liked.maemul_id === maemul.id);
                    if (liked.maemul_id === maemul.id) {
                        heartButton.querySelector("button").setAttribute("data-isButton", "true");
                        heartButton.querySelector("img").setAttribute("src", "/img/mapDetailAndAPTList/aHeartBtn.png"); // 이미지를 바꿔줌
                        break; // 조건이 만족되면 반복문을 종료합니다.
                    } else {
                        heartButton.querySelector("button").setAttribute("data-isButton", "false");
                    }
                }
        } else {
            heartButton.querySelector("button").setAttribute("data-isButton", "false");
        }

        // li 요소에 a 요소와 하트 버튼 추가
        listItem.appendChild(anchor);
        listItem.appendChild(heartButton);

        // li 요소를 사이드바 컨테이너에 추가
        sidebarContainer.appendChild(listItem);
        console.log("리스트 한개 생성 끝");
    });
}

// 사이드바 초기화 함수 정의
function clearSidebar() {
    var sidebarContainer = document.querySelector(".sideContents ul.list-group");
    sidebarContainer.innerHTML = "";
}

// 하트 버튼을 클릭하면 매물 id 전송
$(document).on("click", ".aHeartBtn", function() {
    if (loginMember != null) {
        var $heartButton = $(this); // 클릭한 버튼을 변수에 저장
        var maemulId = $heartButton.closest("li").find("a").attr("href").split("/").pop();
        maemulId = maemulId.split("?")[0];
        console.log("매물 아이디" + maemulId);
        $.ajax({
            url: "/member/qLiked", //
            type: "POST", //
            data: { maemulId: maemulId }, //
            success: function(response) {
                console.log("통신후 버튼상태 변경전 : " + $heartButton.attr("data-isButton"));
                // 버튼 상태 변경
                if ($heartButton.attr("data-isButton") === "true") {
                    $heartButton.attr("data-isButton", "false");
                    $heartButton.find("img").attr("src", "/img/mapDetailAndAPTList/aHeartBtn2.png");
                } else if ($heartButton.attr("data-isButton") === "false") {
                    $heartButton.attr("data-isButton", "true");
                    $heartButton.find("img").attr("src", "/img/mapDetailAndAPTList/aHeartBtn.png");
                }
                console.log("통신후 버튼상태 변경후 : " + $heartButton.attr("data-isButton"));
                // 관심매물 갯수 최신화
                var bounds = map.getBounds();
                var southWest = bounds.getSouthWest();
                var northEast = bounds.getNorthEast();
                var currentZoomLevel = map.getLevel(); // 현재 줌 레벨 가져오기
                var dataToSend = {
                    southWestLat: southWest.getLat(),
                    southWestLng: southWest.getLng(),
                    northEastLat: northEast.getLat(),
                    northEastLng: northEast.getLng(),
                    zoomLevel: currentZoomLevel
                }
                $.ajax({
                    url: "/map/map",
                    type: "POST",
                    data: dataToSend,
                    success: function(response) {
                        likedEntityList = response.likedEntityList;
                        likedCount(likedEntityList); // 관심매물 갯수 카운팅
                    },
                    error: function(xhr, status, error) {
                        console.error("Ajax 요청 실패: " + error);
                    }
                });
            },
            error: function(xhr, status, error) {
                console.error("Ajax 요청 실패: " + error);
            }
        });
    } else {
        alert("로그인 후 다시 시도해주세요.")
    }
});


// keyword 입력 후 Enter 누르면 검색되는 함수
function checkEnter(event) {
    if (event.key === 'Enter') {
        clearHJDOverlays(); // 행정동 오버레이 닫기
        closeOtherOverlays(); // 열려있는 매물 오버레이 닫기
        // 입력한 키워드 공백 제거
        var keyword = document.querySelector('.aSearchInput').value.replaceAll(' ', '');
        var setZoomLevel = 5;
        var bounds = map.getBounds();
        var southWest = bounds.getSouthWest();
        var northEast = bounds.getNorthEast();
        var currentZoomLevel = map.getLevel(); // 현재 줌 레벨 가져오기

        $.ajax({
            type: 'POST',
            url: '/map/map',
            data: {
                keyword: keyword,
                zoomLevel: setZoomLevel,
                southWestLat: southWest.getLat(),
                southWestLng: southWest.getLng(),
                northEastLat: northEast.getLat(),
                northEastLng: northEast.getLng()
            },
            success: function (response) {
                likedEntityList = response.likedEntityList;
                // 검색 결과에 따라 마커를 생성하고 지도에 표시하기
                if (response.maemulKeywordList) {
                    var result = response.maemulKeywordList; // 키워드 검색후 전송받은 해당 아파트 데이터
                    var newCenter = new kakao.maps.LatLng(result[0].latitude, result[0].longitude);

                    map.setLevel(setZoomLevel); // 줌레벨 변경
                    map.setCenter(newCenter); // 해당 아파트 위치로 센터 변경
                    var currentZoomLevel = map.getLevel(); // 이동시 줌레벨 5로 설정 (줌레벨 안바뀐채로 이동되는 경우 있어서 방지차원)

                    // var markerPosition = new kakao.maps.LatLng(result.latitude, result.longitude);
                    // var markerKey = markerPosition.toString();
                    // var markerContent = "<div class='e-marker'>" +
                    //     "<div class='e-markerTitle'>" +
                    //     "<h3>" + result.APT_name + "</h3>" +
                    //     "</div>" +
                    //     "<div class='e-markerContent'>" +
                    //     "<p>" + result.address + "</p>" +
                    //     "</div>" +
                    //     "</div>";
                    // createMarker(markerPosition, markerContent, result);
                    // var marker = existingMarkers[markerKey];
                    // openOverlay(marker.overlay);
                    // updateSidebar(result);
                    // console.log("클릭시 마커생성");
                }
            }
        });
    }
}

/* 주거용 상업용 필터*/

function showValue(value) {
    console.log("전송할 데이터: ", value);
    clearHJDOverlays(); // 행정동 오버레이 닫기
    closeOtherOverlays(); // 열려있는 매물 오버레이 닫기

    var setZoomLevel = 5;
    var bounds = map.getBounds();
    var southWest = bounds.getSouthWest();
    var northEast = bounds.getNorthEast();
    var currentZoomLevel = map.getLevel(); // 현재 줌 레벨 가져오기

    $.ajax({
        type: 'POST',
        url: '/map/map',
        data: {
            value: value,
            zoomLevel: setZoomLevel,
            southWestLat: southWest.getLat(),
            southWestLng: southWest.getLng(),
            northEastLat: northEast.getLat(),
            northEastLng: northEast.getLng()
        },

        success: function (response) {
            likedEntityList = response.likedEntityList;

            console.log("전송된 데이터: ", value); // 보낸 파라미터 확인
            console.log("서버 응답: ", response);

            console.log(response.maemulButtonList);
            // 검색 결과에 따라 마커를 생성하고 지도에 표시하기
            if (response.maemulButtonList) {
                var result = response.maemulButtonList; // 키워드 검색후 전송받은 해당 아파트 데이터
                console.log(result);
                var newCenter = new kakao.maps.LatLng(result[0].latitude, result[0].longitude);

                map.setLevel(setZoomLevel); // 줌레벨 변경
                map.setCenter(newCenter); // 해당 아파트 위치로 센터 변경
                var currentZoomLevel = map.getLevel(); // 이동시 줌레벨 5로 설정 (줌레벨 안바뀐채로 이동되는 경우 있어서 방지차원)

            }
        }
    });

}

/** 필터 라디오버튼 클릭시 실시간 통신 */
function sendToServer() {
    var tradeTypeString = tradeType.join(",");
    var directionString = direction.join(",");

    var keyword = document.querySelector('.aSearchInput').value.replaceAll(' ', '');

    var bounds = map.getBounds();
    var southWest = bounds.getSouthWest();
    var northEast = bounds.getNorthEast();
    var currentZoomLevel = map.getLevel();

    var data = {
        southWestLat: southWest.getLat(),
        southWestLng: southWest.getLng(),
        northEastLat: northEast.getLat(),
        northEastLng: northEast.getLng(),
        zoomLevel: currentZoomLevel,
        tradeType: tradeTypeString,
        numberOfRooms: roomCount,
        numberOfBathrooms: bathRoomCount,
        floorNumber: floorCount,
        managementFee: manageFee,
        Elevator: elevator,
        direction: directionString,
        Parking: parking,
        shortTermRental: rental,
        keyword: keyword,
        rowSellingPrice: maemaeStart,
        highSellingPrice: maemaeEnd,
        rowDepositForLease: jeonseStart,
        highDepositForLease: jeonseEnd,
        rowMonthlyForRent: bozugStart,
        highMonthlyForRent: bozugEnd,
        minPrivateArea: minValue,
        maxPrivateArea: maxValue,
        orderType: orderType
    };

    $.ajax({
        type: "POST",
        url: "/map/map",
        data: data,
        success: function(response) {
            clearSidebar();

            if (response.maenulList) {

                clusterer.clear();
                for (var key in existingMarkers) {
                    if (existingMarkers.hasOwnProperty(key)) {
                        var marker = existingMarkers[key];
                        if (marker.overlay) {
                            marker.overlay.setMap(null);
                        }
                    }
                }
                existingMarkers = {};

                response.maenulList.forEach(function (maemul) {
                    // 행정동 오버레이 초기화
                    clearHJDOverlays();
                    // 마커 오버레이 초기화
                    closeOtherOverlays();
                    // 사이드바 초기화
                    clearSidebar();

                    var markerPosition = new kakao.maps.LatLng(maemul.latitude, maemul.longitude);
                    var markerKey = markerPosition.toString();
                    var markerContent = "<div class='e-marker'>" +
                        "<div class='e-markerTitle'>" +
                        "<h3>" + maemul.apt_name + "</h3>" +
                        "</div>" +
                        "<div class='e-markerContent'>" +
                        "<p>" + maemul.address + "</p>" +
                        "</div>" +
                        "</div>";

                    if (!existingMarkers[markerKey]) {
                        createMarker(markerPosition, markerContent, maemul);
                    }

                });
                updateSidebar(response.maenulList);
            }
        }
    });
}

function likedCount(likedEntityList) {
    var favorityCountElement = document.getElementById("rFavorityCount");
    var newFavorityCount = likedEntityList.length;
    favorityCountElement.textContent = newFavorityCount;
}