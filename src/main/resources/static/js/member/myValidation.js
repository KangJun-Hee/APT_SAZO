$(document).ready(function(){

    // 회원가입 체크박스
    // 전체 선택 체크박스
    const selectAllCheckbox = $("input[name='selectAll']");
    // 개별 체크박스들
    const requiredCheckboxes = $("input[name='agree'][data-required]");
    const choice1Checkbox = $("#choice1");
    const choice2Checkbox = $("#choice2");

    // 개별 체크박스가 변경될 때
    requiredCheckboxes.add(choice1Checkbox).add(choice2Checkbox).on("change", function () {
        updateSelectAllCheckbox();
    });

    // 전체 선택 체크박스가 변경될 때
    selectAllCheckbox.on("change", function () {
        const isChecked = $(this).prop("checked");
        requiredCheckboxes.add(choice1Checkbox).add(choice2Checkbox).prop("checked", isChecked);
    });

    // 개별 체크박스의 상태에 따라 전체 선택 체크박스 상태 갱신
    function updateSelectAllCheckbox() {
        const requiredAllChecked = requiredCheckboxes.filter(":checked").length === requiredCheckboxes.length;
        const choice1Checked = choice1Checkbox.prop("checked");
        const choice2Checked = choice2Checkbox.prop("checked");

        if (requiredAllChecked && choice1Checked && choice2Checked) {
            selectAllCheckbox.prop("checked", true);
        } else if (!requiredAllChecked || !choice1Checked || !choice2Checked) {
            selectAllCheckbox.prop("checked", false);
        }
    }

    // 비밀번호/비밀번호 확인 일치 여부 체크
    $("#pwd, #pwdCheck").on("keyup", function(){
        let pwdValue = $('#pwd').val();
        let pwdCheckValue = $('#pwdCheck').val();
        if (pwdValue !== pwdCheckValue) {
            $("#o-pwdCk").css("display","block");
        } else {
            $("#o-pwdCk").css("display","none");
        }
    });
    // 비밀번호 형식 체크 : 8자 이상, 20자 이하, 숫자와 영문 조합
    $("#pwd").keyup(function(){
        let pwdValue = $(this).val();
        let pwdreg = /^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$/;
        if (pwdValue.length < 8 || pwdValue.length > 20 || !pwdreg.test(pwdValue)) {
            $("#o-pwd").css("display","block");
        } else {
            $("#o-pwd").css("display","none");
        }
    });

    // 이메일 형식 체크
    $("#email").on("input", function() {
        let emailValue = $('#email').val();
        let reg = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/;
        if (reg.test(emailValue) == false) {
            $("#emailErr").text("이메일 형식으로 입력해주세요.");
            $("#emailErr").css("color", "red");
            $("#emailErr").css("display","block");
        } else {
            $("#emailErr").text("");
            $("#emailErr").css("display","none");
        }
    });

    // 연락처 입력 창 형식
    $("#tel").keyup(function(){
        let telValue = $("#tel").val();
        telValue = telValue.replace(/[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]|[^\w\s-]/g, "").replace(/-/g, "");
        $("#tel").val(telValue);
    });

    // 비밀번호 표시 미표시 토글
    $('.form-floating i').on('click',function(){
        $('#pwd').toggleClass('active');
        if($('#pwd').hasClass('active')){
            $(this).attr('class',"fa fa-eye-slash fa-lg")
            .prev('#pwd').attr('type',"text");
            $(".form-floating i").css("color","#26ABED");
        }else{
            $(this).attr('class',"fa fa-eye fa-lg")
            .prev('#pwd').attr('type','password');
            $(".form-floating i").css("color","silver");
        }
    });

    // 닉네임 입력값 변경 체크
    $("#nickname").on("input", function() {
        $('#btnNicknameCheck').attr('class','btn btn-outline-dark');
        $('#btnNicknameCheck').val("중복검사");
    });
});

// 이메일 중복 체크
function checkEmail() {
    let email = $('#email').val().trim();
    let reg = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/;
    if(email === '') {
        alert('이메일을 입력해주세요.');
        return;
    }
    if(!reg.test(email)) {
        alert('이메일 형식으로 입력해주세요.');
        return;
    }
    $.ajax({
        url: '/member/qEmailCheck',
        type: 'post',
        data: {email:email},
        success:function(cnt) {
            if(cnt == 0) {
                $('#btnEmailCheck').attr('class','btn btn-primary');
                $('#btnEmailCheck').val("사용가능");
                alert('이메일 사용이 가능합니다.');
            } else {
                $('#btnEmailCheck').attr('class','btn btn-danger');
                $('#btnEmailCheck').val("사용불가");
                alert('이미 존재하는 이메일입니다.\n이메일을 다시 입력해주세요.');
            //    $('#email').val('');
            }
        },
        error:function() {
            alert("에러입니다.");
        }
    });
};

// 닉네임 중복검사
function checkNickname() {
    let nickname = $('#nickname').val().trim();
    if(nickname === '') {
        alert('닉네임을 입력해주세요.');
        return;
    }
    $.ajax({
        url: '/member/qNicknameDuplicationCheck',
        type: 'post',
        data: {nickname:nickname},
        success:function(cnt) {
            if(cnt == 0) {
                $('#btnNicknameCheck').attr('class','btn btn-primary');
                $('#btnNicknameCheck').val("사용가능");
                alert('닉네임 사용이 가능합니다.');
            } else if(cnt != 0) {
                $('#btnNicknameCheck').attr('class','btn btn-danger');
                $('#btnNicknameCheck').val("사용불가");
                alert('이미 존재하는 닉네임입니다.\n닉네임을 다시 입력해주세요.');
                $('#nickname').val('');
            }
        },
        error:function() {
            alert("에러입니다.");
        }
    });
};

function oJoinCheck() {
    const reg = /^[A-Za-z0-9_\.]+@[A-Za-z0-9]+\.[A-Za-z0-9]+/;
    const telreg = /^\d{10,11}$/;
    const pwdreg = /^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$/;
    let email = $('#email').val().trim();
    let name = $('#name').val().trim();
    let pwd = $('#pwd').val().trim();
    let pwdCheck = $('#pwdCheck').val().trim();
    let nickname = $('#nickname').val().trim();
    let tel = $('#tel').val();
    let emailCheckOk = $("#btnEmailCheck").val();
    let nicknameCheckOk = $("#btnNicknameCheck").val();
    let checkedboxes = $("input.oMustAgree:checked");
    let errorMessage = '';

    if(email === '') {
        errorMessage = '이메일을 입력해주세요.';
    } else if (reg.test(email) == false) {
        errorMessage = '이메일 형식으로 입력해주세요.';
    } else if(name === '') {
        errorMessage = '이름을 입력해주세요.';
    } else if(pwd === '') {
        errorMessage = '비밀번호를 입력해주세요.';
    } else if (!pwdreg.test(pwd)) {
        errorMessage = '비밀번호 형식으로 입력해주세요.';
    } else if(pwdCheck === '') {
        errorMessage = '비밀번호를 확인해주세요.';
    } else if(nickname === '') {
        errorMessage = '닉네임을 입력해주세요.';
    } else if(tel === '') {
        errorMessage = '연락처를 입력해주세요.';
    } else if (telreg.test(tel) == false) {
        errorMessage = '연락처 형식으로 입력해주세요.';
    } else if(checkedboxes.length !== $(".oMustAgree").length) {
        errorMessage = '필수항목을 모두 체크해주세요.';
    } else if(emailCheckOk == '중복검사' || emailCheckOk == '사용불가' ||
                nicknameCheckOk == '중복검사'|| nicknameCheckOk == '사용불가' ) {
        errorMessage = '중복체크를 확인해주세요.';
    }

    if (errorMessage !== '') {
        alert(errorMessage);
        return false;
    }
    return true; // 모든 조건 통과
}
