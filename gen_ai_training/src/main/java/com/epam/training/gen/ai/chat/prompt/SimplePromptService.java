package com.epam.training.gen.ai.chat.prompt;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.microsoft.semantickernel.Kernel;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.chatcompletion.AuthorRole;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
public class SimplePromptService {
    private final ChatCompletionService chatCompletionService;
    private final Kernel kernel;
    private final InvocationContext invocationContext;


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

        return response;
    }
}
