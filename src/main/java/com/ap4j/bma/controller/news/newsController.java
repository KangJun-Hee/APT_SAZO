package com.ap4j.bma.controller.news;

import com.ap4j.bma.service.news.NewsApiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Slf4j
@Controller
public class newsController {

    private final NewsApiService newsApiService;

    public newsController(NewsApiService newsApiService) {
        this.newsApiService = newsApiService;
    }

    @RequestMapping("news")
    public String newsPage(Model model) {
        String defaultSearchQuery = "아파트"; // 기본 검색어 설정

        return search(defaultSearchQuery, 1, 1, model); // 기본 검색어와 함께 search 메서드 호출
    }


@RequestMapping("news/newsPage")
public String search(
        @RequestParam("query") String searchQuery,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "start", defaultValue = "1") int start,
        Model model) {
    String searchResult = newsApiService.searchNews(searchQuery, start);

    ObjectMapper objectMapper = new ObjectMapper();
    try {
        JsonNode jsonNode = objectMapper.readTree(searchResult);
        JsonNode items = jsonNode.get("items");

        List<Map<String, String>> newsList = new ArrayList<>();
        for (JsonNode item : items) {
            String title = item.get("title").asText();
            String link = item.get("link").asText();
            String description = item.get("description").asText();

            Map<String, String> newsItem = new HashMap<>();
            newsItem.put("title", removeHTMLTags(title));
            newsItem.put("link", link);
            newsItem.put("description", removeHTMLTags(description));
            newsList.add(newsItem);

        }
        int totalResults = jsonNode.get("total").asInt(); // 뉴스의 전체 개수

        int itemsPerPage = 10; // 페이지당 뉴스 항목 수
        int startIndex = (page - 1) * itemsPerPage;

        int newStartIndex = startIndex + 1; // start 값을 조정

        int totalPages = (int) Math.ceil((double) totalResults / itemsPerPage);

        int endIndex = Math.min(startIndex + itemsPerPage, newsList.size());

// 마지막 페이지에서의 endIndex 조정
        if (page == totalPages) {
            endIndex = newsList.size() ;
        }

        List<Map<String, String>> paginatedNewsList = newsList.subList(0, endIndex);

        model.addAttribute("newsList", paginatedNewsList);



        log.info(String.valueOf(totalPages));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("searchQuery", searchQuery);

        model.addAttribute("start", newStartIndex);
    } catch (IOException e) {
        e.printStackTrace();
    }

    log.info(searchResult);
    return "news/newsPage";
}
    public static String removeHTMLTags(String input) {

        String cleanText = input
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&apos;", "'")
                .replace("&amp;", "&")
                .replaceAll("<b>", "")
                .replaceAll("</b>", "");
        return cleanText;
    }
}