package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.chat.model.ChatRequest;
import com.epam.training.gen.ai.vector.SimpleVectorActions;
import io.qdrant.client.grpc.Points;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class RAGServiceImpl implements RAGService {

    @Autowired
    private SimpleVectorActions simpleVectorActions;

    @Override
    public void storeKnowledge(String contextTitle, String context) {
            try {
                simpleVectorActions.processAndSaveText(context);
                log.info("Vector saved");
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }

    }

    @Override
    public String getPromptResponse(ChatRequest chatRequest) {
        String fullPrompt = "Use the following context to answer the question.\n\n" +
                "Question/prompt: " + chatRequest.question();
        try {
            List<Points.ScoredPoint> scoredPointList = simpleVectorActions.search(fullPrompt);
            return scoredPointList.toString();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
