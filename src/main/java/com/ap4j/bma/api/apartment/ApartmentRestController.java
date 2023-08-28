package com.ap4j.bma.api.apartment;

import com.ap4j.bma.model.entity.apt.AptDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


@RestController
public class ApartmentRestController {
	//	private final WebClient webClient;
	//
	//	public ApartmentRestController() {
	//		// WebClient 빌더를 사용하여 WebClient 인스턴스 생성
	//		this.webClient = WebClient.builder()
	//				.baseUrl("") // 외부 API의 base URL 설정
	//				.build();
	//	}
	//
	//	@GetMapping("/ApartmentApiList")
	//	public Flux<AptDTO> getApartmentApiList() {
	//		String apiUrl = "http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTradeDev?serviceKey=yCNsY08RNGte7rDDtjdpN8LRA4FXNGAd1mcw2cy59VfwpAA4cffShZy3neeemWjVJ1LMwHQAWaRk%2FuiX%2Fescrw%3D%3D&pageNo=1&numOfRows=10&LAWD_CD=11110&DEAL_YMD=201512";
	//		// WebClient를 사용하여 API 호출 및 데이터 가져오기
	//		return webClient.get()
	//				.uri(apiUrl)
	//				.accept(MediaType.APPLICATION_JSON)
	//				.retrieve()
	//				.bodyToFlux(AptDTO.class);
	//	}

	//		String apiUrl = "";

	private final String apiUrl = "http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTradeDev?serviceKey=yCNsY08RNGte7rDDtjdpN8LRA4FXNGAd1mcw2cy59VfwpAA4cffShZy3neeemWjVJ1LMwHQAWaRk%2FuiX%2Fescrw%3D%3D&pageNo=1&numOfRows=10&LAWD_CD=11110&DEAL_YMD=201512";

	@GetMapping("/getApartments")
	public List<AptDTO> fetchApartments() throws Exception {
		int pageNo = 1; // 시작 페이지
		int numOfRows = 10; // 한 페이지에 표시할 항목 수

		int startYear = 2006; // 시작 년도
		int startMonth = 1;   // 시작 월
		int endYear = 2023;   // 종료 년도
		int endMonth = 8;     // 종료 월

		int LAWD_CD = 11110;
		List<AptDTO> allApartments = new ArrayList<>();

		for (int year = startYear; year <= endYear; year++) {
			for (int month = startMonth; month <= 12; month++) {
				// URL 생성
				String apiUrl = "http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTradeDev?serviceKey=yCNsY08RNGte7rDDtjdpN8LRA4FXNGAd1mcw2cy59VfwpAA4cffShZy3neeemWjVJ1LMwHQAWaRk%2FuiX%2Fescrw%3D%3D" + "&pageNo=" + pageNo + "&numOfRows=" + numOfRows + "&LAWD_CD=" + LAWD_CD + "&DEAL_YMD=" + year + String.format("%02d", month);
				URL url = new URL(apiUrl);

				// HttpURLConnection을 사용하여 GET 요청 생성
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");

				// 응답 데이터를 읽을 BufferedReader 생성
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder response = new StringBuilder();
				String line;

				while ((line = reader.readLine()) != null) {
					response.append(line);
				}

				// XML 파싱
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(new StringReader(response.toString())));

				NodeList itemList = document.getElementsByTagName("item");
				List<AptDTO> aptList = new ArrayList<>();

				for (int i = 0; i < itemList.getLength(); i++) {
					Node itemNode = itemList.item(i);

					if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) itemNode;
						AptDTO aptDTO = new AptDTO();

					// 거래금액 (Deal Amount)
					aptDTO.setDealAmount(element.getElementsByTagName("거래금액").item(0).getTextContent());

					// 거래유형 (Transaction Type)
					aptDTO.setDealType(element.getElementsByTagName("거래유형").item(0).getTextContent());

					// 건축년도 (Year of Construction)
					aptDTO.setBuildYear(element.getElementsByTagName("건축년도").item(0).getTextContent());

					// 년 (Year)
					aptDTO.setYear(element.getElementsByTagName("년").item(0).getTextContent());

					// 도로명 (Road Name)
					aptDTO.setRoadName(element.getElementsByTagName("도로명").item(0).getTextContent());

					// 도로명건물본번호코드 (Road Name Main Building Number Code)
					aptDTO.setRoadNameMainCode(element.getElementsByTagName("도로명건물본번호코드").item(0).getTextContent());

					// 도로명건물부번호코드 (Road Name Sub-Building Number Code)
					aptDTO.setRoadNameSubCode(element.getElementsByTagName("도로명건물부번호코드").item(0).getTextContent());

					// 도로명시군구코드 (Road Name District Code)
					aptDTO.setRoadNameGuCode(element.getElementsByTagName("도로명시군구코드").item(0).getTextContent());

					// 도로명일련번호코드 (Road Name Serial Number Code)
					aptDTO.setRoadNameSerialCode(element.getElementsByTagName("도로명일련번호코드").item(0).getTextContent());

					// 도로명지상지하코드 (Road Name Ground/Above Ground Code)
					aptDTO.setRoadNameGroundCode(element.getElementsByTagName("도로명지상지하코드").item(0).getTextContent());

					// 도로명코드 (Road Name Code)
					aptDTO.setRoadNameCode(element.getElementsByTagName("도로명코드").item(0).getTextContent());

					// 등기일자 (Registration Date)
					aptDTO.setRegistrationDate(element.getElementsByTagName("등기일자").item(0).getTextContent());

					// 법정동 (Legal Dong)
					aptDTO.setLegalDong(element.getElementsByTagName("법정동").item(0).getTextContent());

					// 법정동본번코드 (Legal Dong Main Building Number Code)
					aptDTO.setLegalDongMainNumberCode(element.getElementsByTagName("법정동본번코드").item(0).getTextContent());

					// 법정동부번코드 (Legal Dong Sub-Building Number Code)
					aptDTO.setLegalDongSubNumberCode(element.getElementsByTagName("법정동부번코드").item(0).getTextContent());

					// 법정동시군구코드 (Legal Dong City Code)
					aptDTO.setLegalDongCityCode(element.getElementsByTagName("법정동시군구코드").item(0).getTextContent());

					// 법정동읍면동코드 (Legal Dong Town Code)
					aptDTO.setLegalDongTownCode(element.getElementsByTagName("법정동읍면동코드").item(0).getTextContent());

					// 법정동지번코드 (Legal Dong Serial Number Code)
					aptDTO.setLegalDongSerialCode(element.getElementsByTagName("법정동지번코드").item(0).getTextContent());

					// 아파트 (Apartment)
					aptDTO.setApartment(element.getElementsByTagName("아파트").item(0).getTextContent());

					// 월 (Month)
					aptDTO.setMonth(element.getElementsByTagName("월").item(0).getTextContent());

					// 일 (Day)
					aptDTO.setDay(element.getElementsByTagName("일").item(0).getTextContent());

					// 일련번호 (Serial Number)
					aptDTO.setSerialNumber(element.getElementsByTagName("일련번호").item(0).getTextContent());

					// 전용면적 (Exclusive Area)
					aptDTO.setExclusiveArea(element.getElementsByTagName("전용면적").item(0).getTextContent());

					// 중개사소재지 (Agent Location)
					aptDTO.setAgentLocation(element.getElementsByTagName("중개사소재지").item(0).getTextContent());

					// 지번 (Land Lot Number)
					aptDTO.setLandLot(element.getElementsByTagName("지번").item(0).getTextContent());

					// 지역코드 (Region Code)
					aptDTO.setRegionCode(element.getElementsByTagName("지역코드").item(0).getTextContent());

					// 층 (Floor)
					aptDTO.setFloor(element.getElementsByTagName("층").item(0).getTextContent());

					// 해제사유발생일 (Release Reason Date)
					aptDTO.setReleaseReasonDate(element.getElementsByTagName("해제사유발생일").item(0).getTextContent());

					// 해제여부 (Release Status)
					aptDTO.setReleaseStatus(element.getElementsByTagName("해제여부").item(0).getTextContent());

						aptList.add(aptDTO);
					}
				}

				allApartments.addAll(aptList);

				// 다음 페이지로 이동
				pageNo++;

				if (month < 12) {
					month++;
				} else {
					if (year < endYear) {
						year++;
					}
					month = 1;
				}

				// 더 이상 데이터가 없을 때 while 루프 종료
				// if (aptList.isEmpty()) {
				//     break;
				// }

				reader.close();
				conn.disconnect();
			}
		}

		return allApartments;
	}
}
