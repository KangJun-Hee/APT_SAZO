



//// 더보기 버튼을 만드는 함수
//function createLoadMoreButton(data) {
//    var tableBody = $(".tbl tbody");
//    var loadMoreButton = $("#aload-more-button");
//
//    // 초기에는 더보기 버튼을 숨김
//    loadMoreButton.hide();
//
//    // 더보기 버튼 클릭 시 처리
//    loadMoreButton.on("click", function () {
//        var startIndex = tableBody.find("tr").length; // 현재 테이블에 표시된 데이터 개수
//        var endIndex = Math.min(startIndex + 5, data.length); // 다음 5개의 데이터를 가져옴
//
//        for (var i = startIndex; i < endIndex; i++) {
//            var detailItem = data[i];
//            // 나머지 데이터를 테이블에 추가
//            var amount = detailItem.transactionAmount.toString();
//            var amountSliceFirst = amount.slice(0, amount.length - 1);
//            var amountSliceLast = amount.slice(-1)
//            var reformatAmount = null;
//
//            // 테이블 행 생성 및 데이터 추가
//            var row = $("<tr>");
//            $("<td>").text(detailItem.area).appendTo(row);
//            $("<td>").text(detailItem.floor).appendTo(row);
//            $("<td>").text(detailItem.contractYearMonth).appendTo(row);
//
//            if (amountSliceLast != 0) {
//                reformatAmount = amountSliceFirst + "억 " + amountSliceLast + "000만원";
//            } else {
//                reformatAmount = amountSliceFirst + "억";
//            }
//
//            $("<td>").text(reformatAmount).appendTo(row);
//
//            // 테이블에 행 추가
//            tableBody.append(row);
//        }
//
//        // 모든 데이터를 표시했다면 더보기 버튼을 숨김
//        if (endIndex === data.length) {
//            loadMoreButton.hide();
//        }
//    });
//
//    // 처음에는 6개 이상 데이터가 있을 때만 더보기 버튼을 보이게 함
//    if (data.length > 6) {
//        loadMoreButton.show();
//    }
//}




//const main = {
//init : function() {
//const _this = this;
//
//// 댓글 저장
//$('#btn-comment-save').on('click', function () {
//_this.commentSave();
//});
//
//},
// /** 댓글 저장 */
//commentSave : function () {
//const data = {
//postsId: $('#postsId').val(),
//comment: $('#comment').val()
//}
//
//// 공백 및 빈 문자열 체크
//if (!data.comment || data.comment.trim() === "") {
//alert("공백 또는 입력하지 않은 부분이 있습니다.");
//return false;
//} else {
//$.ajax({
//type: 'POST',
//url: '/api/posts/' + data.postsId + '/comments',
//dataType: 'JSON',
//                contentType: 'application/json; charset=utf-8',
//                data: JSON.stringify(data)
//            }).done(function () {
//                alert('댓글이 등록되었습니다.');
//                window.location.reload();
//            }).fail(function (error) {
//                alert(JSON.stringify(error));
//            });
//        }
//    }
//};
//
//main.init();



//// 데이터 배열 (크기에 따른 값을 포함)
//var data = [
//    { x: 1, y: 5 },
//    { x: 2, y: 8 },
//    { x: 3, y: 3 },
//    { x: 4, y: 1 },
//    { x: 5, y: 10 },
//    { x: 6, y: 4 },
//    { x: 7, y: 6 },
//    { x: 8, y: 3 },
//    { x: 9, y: 10 },
//    { x: 10, y: 3 },
//    // ... 더 많은 데이터 포인트
//];
//
//// SVG 요소 생성
//var svg = d3.select("#realpricePercentChart")
//    .append("svg")
//    .attr("width", 300)
//    .attr("height", 150);
//
//// 스케일 설정 (x축, y축)
//var xScale = d3.scaleLinear()
//    .domain([1, data.length])
//    .range([0, 200]);//표의 가로 길이 중 어디까지를 x선으로 사용할지인 듯
//
//var graphHeight = 150; // 그래프의 높이를 조절해보세요
//
//var yScale = d3.scaleLinear()
//    .domain([0, d3.max(data, d => d.y)])
//    .range([0, graphHeight]);
//
//
//// 점 그리기
//svg.selectAll("circle")
//    .data(data)
//    .enter()
//    .append("circle")
//    .attr("cx", d => xScale(d.x))
//    .attr("cy", d => graphHeight - yScale(d.y)) // 수정된 부분
//    .attr("r", 4) // 점의 반지름 조절
//    .attr("fill", "#129EED"); // 점의 색상 조절
//
//// 라인 그리기
//var line = d3.line()
//    .x(d => xScale(d.x))
//    .y(d => graphHeight - yScale(d.y)); // 수정된 부분
//
//
//svg.append("path")
//    .datum(data)
//    .attr("d", line)
//    .attr("fill", "none")
//    .attr("stroke", "#129EED")
//    .attr("stroke-width", 2);//선 굵기
//
//// x 축 생성 함수
//var xAxis = d3.axisBottom(xScale);
//
//// x 축 그리기
//svg.append("g")
//    .attr("class", "x-axis")
//    .attr("transform", `translate(0, ${graphHeight})`)
//    .call(xAxis)
//    .selectAll("path, line")
//    .attr("stroke-width", 2)  // x축 선 두께 조절
//    .attr("stroke", "#B7D2E7");  // x축 선 색상 조절
//
//// y 축 생성 함수
//var yAxis = d3.axisLeft(yScale);
//
//// y 축 그리기
//svg.append("g")
//    .attr("class", "y-axis")
//    .call(yAxis)
//    .selectAll("path, line")
//    .attr("stroke-width", 2)  // y축 선 두께 조절
//    .attr("stroke", "#B7D2E7");  // y축 선 색상 조절
//
//// y 축 눈금 위치 지정
//var yAxisTicks = [0, 2, 4, 6]; // 원하는 y축의 눈금 위치
//var yAxisTickPositions = yAxisTicks.map(tick => graphHeight - yScale(tick));
//
//// y 축 범위 표시 선 그리기
//svg.selectAll(".y-axis-line")
//   .data(yAxisTickPositions)
//       .enter()
//       .append("g")
//       .attr("class", "y-axis-line")
//       .each(function(d, i) {
//           // 선 그리기
//           d3.select(this)
//               .append("line")
//               .attr("x1", 0)
//               .attr("x2", 300) // x축의 끝까지
//               .attr("y1", d)
//               .attr("y2", d)
//               .attr("stroke", "#ccc")
//               .attr("stroke-dasharray", "4");
//
//           // 텍스트 추가
//           d3.select(this)
//               .append("text")
//               .attr("x", -5) // 선 왼쪽에 위치
//               .attr("y", d - 5) // 선 위쪽에 위치
//               .attr("text-anchor", "end")
//               .attr("dominant-baseline", "middle")
//               .attr("font-size", "10px")
//               .text(yAxisTicks[i] + "천만원"); // 텍스트 내용 d
//       });





//document.addEventListener('DOMContentLoaded', function() {
//        // 모달 버튼 선택
//        var modalButton = document.getElementById('aSaveBtn');
//
//        // 모달 창 선택
//        var modal = document.getElementById('aSaveToMine');
//
//        // 모달 버튼 클릭 이벤트 리스너 추가
//                modalButton.addEventListener('click', function() {
//                    // 모달이 열려있을 때와 닫혀있을 때를 번갈아가며 처리
//                    if (modal.style.display === 'block') {
//                        modal.style.display = 'none';
//                        modal.setAttribute('aria-hidden', 'true');
//                    } else {
//                        modal.style.display = 'block';
//                        modal.setAttribute('aria-hidden', 'false');
//                    }
//                });
//    });
