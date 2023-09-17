$(document).ready(function() {
    // 초기 상태: 왼쪽 탭의 내용 표시
    $(".o-tab-content").hide();
    $("#o-tab-left").show();

    // 왼쪽 탭 클릭 이벤트 핸들러
    $("#o-liked-link").click(function(e) {
        e.preventDefault(); // 기본 링크 동작 방지

        // 오른쪽 탭의 내용 숨김
        $("#o-tab-right").hide();

        // 왼쪽 탭의 내용 표시
        $("#o-tab-left").show();

        $("#o-recent-link").css("color", "#999999");
        $("#o-liked-link").css("color", "#25AAE1");
    });

    // 오른쪽 탭 클릭 이벤트 핸들러
    $("#o-recent-link").click(function(e) {
        e.preventDefault(); // 기본 링크 동작 방지

        // 왼쪽 탭의 내용 숨김
        $("#o-tab-left").hide();

        // 오른쪽 탭의 내용 표시
        $("#o-tab-right").show();

        $("#o-recent-link").css("color", "#25AAE1");
        $("#o-liked-link").css("color", "#999999");
    });

    // 탭상태에 따른 전체보기 클릭 시 링크 이동
     $("#o-allView").click(function(e) {
        e.preventDefault(); // 기본 링크 동작 방지

        if($("#o-tab-left").is(":visible")) {
            window.location.href = "/member/liked";
        } else  if($("#o-tab-right").is(":visible")) {
            window.location.href = "/member/qRecent";
        }
    });
});

// 모달 열기
//function openModal() {
//    var modal = document.getElementById("myModal");
//    modal.style.display = "block";
//}
//
//// 모달 닫기
//function closeModal() {
//    var modal = document.getElementById("myModal");
//    modal.style.display = "none";
//}
//
//// 파일 업로드 기능 추가 (이 부분은 서버와 연동해야 함)
//function uploadFile() {
//    var fileInput = document.getElementById("fileInput");
//    var selectedFile = fileInput.files[0];
//
//    if (selectedFile) {
//        // 여기에 파일 업로드 로직을 추가하세요
//        console.log("선택한 파일:", selectedFile);
//        // 서버로 파일을 업로드하거나 다른 작업을 수행할 수 있습니다.
//    } else {
//        alert("파일을 선택하세요.");
//    }
//
//    closeModal(); // 업로드 후 모달 닫기
//}

// 모달 열기
document.getElementById("openModal").addEventListener("click", function() {
    document.getElementById("imageModal").style.display = "block";
});

// 모달 닫기
function closeModal() {
    document.getElementById("imageModal").style.display = "none";
}

document.getElementsByClassName("close")[0].addEventListener("click", closeModal);


// 선택된 이미지를 담을 변수
var selectedImage = null;

// 이미지를 클릭할 때 선택 표시
var galleryImages = document.getElementsByClassName("gallery-image");
for (var i = 0; i < galleryImages.length; i++) {
    galleryImages[i].addEventListener("click", function() {
        for (var j = 0; j < galleryImages.length; j++) {
            galleryImages[j].classList.remove("selected");
        }
        this.classList.add("selected");
        selectedImage = this.src;
    });
}

// "선택 완료" 버튼 클릭 시 선택된 이미지를 원래 화면에 표시 후 모달 닫기
document.getElementById("selectButton").addEventListener("click", function() {
    if (selectedImage) {
        var selectedImageElement = document.createElement("img");
        selectedImageElement.src = selectedImage;
        document.getElementById("selectedImage").innerHTML = "";
        document.getElementById("selectedImage").appendChild(selectedImageElement);

        const selectedImageSrc = selectedImage.src;

        var myImages = document.getElementsByClassName("myImg");
        for (var i = 0; i < myImages.length; i++) {
            myImages[i].style.display = "none";
        }
        sessionStorage.setItem('selectedImageSrc', selectedImageSrc);   // 세션에 저장

        closeModal(); // 모달 닫기
    }
});


// 선택된 이미지를 담을 변수
var selectedImage = null;

// 이미지를 클릭할 때 선택 표시
var galleryImages = document.getElementsByClassName("gallery-image");
for (var i = 0; i < galleryImages.length; i++) {
    galleryImages[i].addEventListener("click", function() {
        for (var j = 0; j < galleryImages.length; j++) {
            galleryImages[j].classList.remove("selected");
        }
        this.classList.add("selected");
        selectedImage = this.src;
    });
}


// 세션에서 이미지 URL을 가져와서 화면에 표시
//const savedImageSrc = sessionStorage.getItem('selectedImageSrc');
//if (savedImageSrc) {
//  const selectedImageElement = document.getElementById('selectedImage');
//  selectedImageElement.src = savedImageSrc;
//  selectedImageElement.style.display = 'block';
//}
