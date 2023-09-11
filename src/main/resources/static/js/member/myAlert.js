$(document).ready(function() {
    // 회원가입 페이지
    // 회원가입 취소 버튼
    $("#oJoinCancelBtn").click(function() {
        let cancelA = confirm('회원가입을 취소하시겠습니까? 메인화면으로 이동합니다.');
        if(cancelA) {
            location.href = '/';
            return true;
        } else { return false; }
    });

    // 내정보수정 취소 버튼
    $('#oCancelBtn').click(function() {
        let result = confirm('내정보를 수정을 취소하시겠습니까?\n내정보화면으로 이동합니다.');
        if(result) {
            location.href = '/member/qMyPage';
            return true;
        } else { return false; }
    });

    // 회원탈퇴 버튼 클릭 시 모달 열기
    $('#leaveBtn').click(function() {
        $('#pwdLeave').val(''); // 모달 열 때 입력한 비밀번호 초기화
        $('#pwdCheckModal').modal('show');
    });

     // 확인 버튼 클릭 시
     $('#confirmBtn').click(function() {
        var password = $('#pwdLeave').val().trim();
        if(password === '') {
            alert('비밀번호를 입력해주세요.');
            return;
        }
        // AJAX를 사용하여 비밀번호를 컨트롤러로 전송
        $.ajax({
           type: 'POST',
           url: '/member/qLeaveMember2',
           data: {
               password: password
           },
           success: function(result) {
               if (result === 1) {
                   alert('탈퇴가 완료되었습니다.');
                   window.location.href = '/';
               } else {
                   alert('아이디 또는 비밀번호를 다시 확인해주세요.');
               }
           },
           error: function() {
               alert('오류');
           }
        });
        // 모달 창 닫기
        $('#pwdCheckModal').modal('hide');
     });
});

// SNS계정회원 탈퇴 submit 전 확인 취소
function oDeleteCheck() {
   let answer = confirm('정말 탈퇴하시겠습니까?\nSNS계정은 즉시 탈퇴처리됩니다.');
   return answer;
}

// 로그인 페이지
// 로그인 및 유효성(공백) 체크 (form 태그에서 onsubmit으로 호출해 submit 하기 전 체크)
function oLoginCheck() {
    let email = $('#email').val();
    let pwd = $('#pwd').val();
    let emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;

    if(email === '') {
        alert('이메일을 입력해주세요.');
        return false;
    }
    if(pwd === '') {
        alert('비밀번호를 입력해주세요.');
        return false;
    }
    if(!emailPattern.test(email)) {
        alert('올바른 이메일형식이 아닙니다.');
        return false;
    }
}