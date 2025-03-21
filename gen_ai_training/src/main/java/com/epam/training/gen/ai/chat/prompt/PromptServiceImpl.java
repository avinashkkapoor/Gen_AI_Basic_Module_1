package com.epam.training.gen.ai.chat.prompt;

import com.epam.training.gen.ai.chat.model.ChatRequest;
import com.epam.training.gen.ai.chat.model.ChatResponse;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for generating chat completions using Azure OpenAI.
 * <p>
 * This service interacts with the Azure OpenAI API to generate chat completions
 * based on a static greeting message. It retrieves responses from the AI model
 * and logs them.
 */
@Slf4j
@Service
@AllArgsConstructor
public class PromptServiceImpl implements PromptService {

    private final ChatCompletionService chatCompletionService;
    private final Kernel kernel;
    private final InvocationContext invocationContext;


    @Override
    public String getChatCompletions(String questions) {
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.addUserMessage(questions);
        List<ChatMessageContent<?>> results = chatCompletionService
                .getChatMessageContentsAsync(chatHistory, null, invocationContext)
                .block();
        String response =
                results.stream()
                        .filter(
                                chatMessageContent ->
                                        chatMessageContent.getAuthorRole() == AuthorRole.ASSISTANT
                                                && chatMessageContent.getContent() != null)
                        .map(ChatMessageContent::getContent)
                        .findFirst().get();

        chatHistory.addAssistantMessage(response);
        return response;
    }

    @Override
    public ChatResponse getChatCompletionsWithTemp(ChatRequest chatRequest) {
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.addUserMessage(chatRequest.question());
        double temperature = Double.parseDouble(chatRequest.temperature());

        //Use for temperature
        InvocationContext invocationContext1 = InvocationContext.builder().withPromptExecutionSettings(PromptExecutionSettings.builder().withTemperature(temperature).build()).build();

        List<ChatMessageContent<?>> results = chatCompletionService
                .getChatMessageContentsAsync(chatHistory, null, invocationContext1)
                .block();
        String response =
                results.stream()
                        .filter(
                                chatMessageContent ->
                                        chatMessageContent.getAuthorRole() == AuthorRole.ASSISTANT
                                                && chatMessageContent.getContent() != null)
                        .map(ChatMessageContent::getContent)
                        .findFirst().get();

        //Adding chat history
        chatHistory.addAssistantMessage(response);
        return new ChatResponse(chatRequest.question(), response);
    }
}
