package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.chat.model.ChatRequest;

public interface RAGService {
    void storeKnowledge(String url, String content);

    String getPromptResponse(ChatRequest chatRequest);
}
