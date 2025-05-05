package com.epam.training.gen.ai.chat.controller;


import com.epam.training.gen.ai.chat.model.ChatRequest;
import com.epam.training.gen.ai.service.RAGService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/gen-ai-bootcamp/rag")
public class RAGController {

    private final RAGService ragService;

    public RAGController(RAGService ragService) {
        this.ragService = ragService;
    }

    @PostMapping(value = "/upload/url", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> storeContextFromUrl(@RequestParam("url") String url) {
        String content = null;
        try {
            content = fetchWebPageContent(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ragService.storeKnowledge(url, content);
        return ResponseEntity.ok("Context Added Successfully.");
    }

    @GetMapping("/prompt")
    public String getPromptChatResponse(@RequestBody ChatRequest chatRequest) {
        return ragService.getPromptResponse(chatRequest);
    }

    private String fetchWebPageContent(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        return doc.text();
    }
}
