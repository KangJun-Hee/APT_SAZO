document.addEventListener("DOMContentLoaded", function () {
    /** 모달 기능 */
    const modalButton = document.getElementById("e-modalButton");
    const modal = document.querySelector(".e-modalContent");
    const closeButton = document.getElementById("e-close");
    let isClicked = false; // 버튼 클릭 여부

    // 처음에 모달 컨텐츠를 숨김
    modal.style.display = "none";

    // 모달 버튼 클릭 시 모달 창 토글
    modalButton.addEventListener("click", function () {
        modal.style.display = modal.style.display === "block" ? "none" : "block"; //
        isClicked = !isClicked;
        if (isClicked) {
            modalButton.style.transform = "scale(1.0)";
        } else {
            modalButton.style.transform = "scale(0.9)";
        }
    });

    // x 버튼 클릭시 모달 닫기
    closeButton.addEventListener("click", function () {
        modal.style.display = "none";
    });

    /** 계산기 상환방법 버튼 */
    const button1 = document.getElementById("e-button1");
    const button2 = document.getElementById("e-button2");
    const button3 = document.getElementById("e-button3");

    const buttons = [button1, button2, button3]; // 버튼들을 배열로 저장

    function toggleButtonStyle(button, color) {
        button.classList.toggle("clicked");

        if (button.classList.contains("clicked")) {
            button.style.backgroundColor = color;
            button.style.borderColor = color;
            button.style.color = "white";
        } else {
            button.style.backgroundColor = "white";
            button.style.borderColor = "lightgray"
            button.style.color = "black";
        }
    }

    // 모든 버튼에 대한 클릭 이벤트 핸들러 추가
    buttons.forEach((button, index) => {
        button.addEventListener("click", function () {
            buttons.forEach((otherButton, otherIndex) => {
                if (otherIndex !== index) { // 다른 버튼들 초기화
                    otherButton.classList.remove("clicked");
                    otherButton.style.backgroundColor = "white";
                    otherButton.style.borderColor = "lightgray";
                    otherButton.style.color = "black";
                }
            });
            toggleButtonStyle(button, "#26abed"); // 선택한 버튼 스타일 토글
        });
    });

    /** 계산기 기능 */
    const inputAmount = document.getElementById("e-inputAmount"); // 원금
    const inputRate = document.getElementById("e-inputRate"); // 이자율
    const inputPeriod = document.getElementById("e-inputPeriod"); // 상환기간
    const buttonSubmit = document.getElementById("e-buttonSubmit"); // 계산하기 버튼
    const buttonReset = document.getElementById("e-buttonReset"); // 초기화 버튼

    // 대출금액 입력시 숫자3자리 단위로 , 붙여주기
    inputAmount.addEventListener("input", function () {
        const inputValue = inputAmount.value.replace(/,/g, ''); // 이미 있는 쉼표 제거
        const formattedValue = formatNumberWithCommas(inputValue);
        inputAmount.value = formattedValue;
    });


    buttonSubmit.addEventListener("click", function () {
        // 사용자 입력 값은 문자열 형태로 받아오기 때문에, parseFloat 함수를 사용하여 숫자로 변환하여 계산에 사용한다.
        // 원금
        const principal = parseFloat(inputAmount.value.replace(/,/g, '')); // 쉼표 제거
        // 이자율
        const interestRate = parseFloat(inputRate.value);
        // 상환기간
        const loanPeriod = parseFloat(inputPeriod.value);

        if (isNaN(principal) || isNaN(interestRate) || isNaN(loanPeriod)) {
            alert("유효한 숫자를 입력하세요.");
            return;
        }
        const monthlyInterestRate = (interestRate / 100) / 12; // 월 이자율
        const numberOfPayments = loanPeriod * 12; // 상환기간(월 단위)

        const repaymentMethod = document.querySelector(".e-button.clicked");
        let totalInterest = 0;  // 총 상환금액
        let monthlyPayment = 0; // 1회차 월 상환금액

        if (repaymentMethod.id === "e-button1") { // 원리금균등
            monthlyPayment = (principal * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, numberOfPayments)) /
                (Math.pow(1 + monthlyInterestRate, numberOfPayments) - 1);
            totalInterest = (monthlyPayment * numberOfPayments) - principal;
            document.querySelector(".e-result4 th").textContent = "1회차 월 상환금액";
        } else if (repaymentMethod.id === "e-button2") { // 원금균등

            let test = parseFloat(inputAmount.value.replace(/,/g, ''));
            let totaliza = 0;

            for(let i = 0; i < numberOfPayments; i++){
                let iza = Math.round(test * monthlyInterestRate); // 월 이자
                let wolmoney = Math.ceil( principal / numberOfPayments); // 월 상환액

                test = test - wolmoney;
                totaliza += iza;
            }

            totalInterest = totaliza;
            
            monthlyPayment = principal / numberOfPayments + Math.ceil(principal * monthlyInterestRate);

            document.querySelector(".e-result4 th").textContent = "1회차 월 상환금액";
        } else if (repaymentMethod.id === "e-button3") { // 만기일시
            totalInterest = principal *  loanPeriod * (interestRate / 100);
            monthlyPayment = principal + totalInterest;
            document.querySelector(".e-result4 th").textContent = "총 상환금액";
        }

        const totalAmount = principal + totalInterest;

        // 결과 필드에 형식화된 값으로 업데이트
        document.querySelector(".e-result1 td").textContent = formatNumberWithCommas(principal) + "원";
        document.querySelector(".e-result2 td").textContent = formatNumberWithCommas(totalInterest.toFixed(0)) + "원";
        document.querySelector(".e-result3 td").textContent = formatNumberWithCommas(totalAmount.toFixed(0)) + "원";
        document.querySelector(".e-result4 td").textContent = formatNumberWithCommas(monthlyPayment.toFixed(0)) + "원";
    });

    /** 초기화 버튼 */
    buttonReset.addEventListener("click", function () {
        // 입력 필드 초기화
        inputAmount.value = "";
        inputRate.value = "";
        inputPeriod.value = "";

        // 결과 필드 초기화
        document.querySelector(".e-result1 td").textContent = "원";
        document.querySelector(".e-result2 td").textContent = "원";
        document.querySelector(".e-result3 td").textContent = "원";
        document.querySelector(".e-result4 th").textContent = "1회차 총 상환금액";
        document.querySelector(".e-result4 td").textContent = "원";

        // 선택된 상환방법 초기화
        buttons.forEach(button => {
            button.classList.remove("clicked");
            button.style.backgroundColor = "white";
            button.style.borderColor = "lightgray";
            button.style.color = "black";
        });
    });
});

function formatNumberWithCommas(number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

