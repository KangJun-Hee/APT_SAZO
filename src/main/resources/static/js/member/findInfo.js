// 이메일찾기 submit 전에 공백, 형식 체크
//function oFindEmailCheck() {
//    const name = $('#name').val().trim();
//    const tel = $('#tel').val().trim();
//    const telreg = /^\d{10,11}$/;
//
//    if (name === '') {
//        alert('이름을 입력해주세요.');
//        return false;
//    }
//    if (tel === '' || !telreg.test(tel)) {
//        alert('올바른 연락처 형식으로 입력해주세요.');
//        return false;
//    }
//}

$(document).ready(function(){
    // 비밀번호 임시메일 발송
    $("#checkEmail").click(function () {
        const userEmail = $("#userEmail").val().trim();
        const sendEmail = document.forms["sendEmail"];
        const emailreg = /^[A-Za-z0-9_\.]+@[A-Za-z0-9]+\.[A-Za-z0-9]+/;

        if(userEmail === '') {
            alert('이메일을 입력해주세요.');
            return false;
        }
        if(!emailreg.test(userEmail)) {
            alert('이메일 형식으로 입력해주세요.');
            return false;
        }
        $.ajax({
            type: 'post',
            url: 'emailDuplication',
            data: {
                'userEmail': userEmail
            },
            dataType: "text",
            success: function (result) {
                if(result == "no"){
                    // 중복되는 것이 있다면 no == 일치하는 이메일이 있다!
                    alert('임시비밀번호를 전송 했습니다.');
                    sendEmail.submit();
                }else {
                    alert('가입되지 않은 이메일입니다.');
                }
            },error: function () {
                console.log('에러 체크!!')
            }
        })
    });
    // 연락처 입력 창 형식
    $("#tel").keyup(function(){
        let telValue = $("#tel").val().trim();
        telValue = telValue.replace(/[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]|[^\w\s-]/g, "").replace(/-/g, "");
        $("#tel").val(telValue);
    });
});

// 이메일 찾기 (이름 연락처 둘 다 동일한 경우에) ajax
$(document).ready(function() {
    $("form").submit(function(event) {
        event.preventDefault(); // 기본 폼 제출 동작을 막음

        var name = $("#name").val().trim();
        var tel = $("#tel").val().trim();
        const telreg = /^\d{10,11}$/;

        if (name === '') {
            alert('이름을 입력해주세요.');
            return false;
        }
        if (tel === '' || !telreg.test(tel)) {
            alert('올바른 연락처 형식으로 입력해주세요.');
            return false;
        }
        $.ajax({
            type: "POST",
            url: "/member/qFindEmail",
            data: { name: name, tel: tel },
            success: function(response) {
                var resultContainer = $(".ofindResult");
                if (response.findEmail) {
                    resultContainer.html("<p>" + response.findEmail + "</p>");
                } else {
                    resultContainer.html("<p>" + response.findEmailFailed + "</p>");
                }
            },
            error: function(error) {
                console.log(error);
            }
        });
    });
});