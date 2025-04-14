package com.epam.training.gen.ai.chat.prompt;

import com.epam.training.gen.ai.chat.model.ChatRequest;
import com.epam.training.gen.ai.chat.model.ChatResponse;

public interface PromptService {
    public String getChatCompletions(String questions);
    public ChatResponse getChatCompletionsWithTemp(ChatRequest chatRequest);
    public ChatResponse getChatCompletionsWithModel(ChatRequest chatRequest);
    public ChatResponse getCurrencyExchange(ChatRequest chatRequest);
}
