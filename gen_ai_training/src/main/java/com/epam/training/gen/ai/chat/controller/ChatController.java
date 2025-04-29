package com.epam.training.gen.ai.chat.controller;

import com.epam.training.gen.ai.chat.model.ChatRequest;
import com.epam.training.gen.ai.chat.model.ChatResponse;
import com.epam.training.gen.ai.chat.prompt.PromptServiceImpl;
import com.epam.training.gen.ai.vector.SimpleVectorActions;
import io.netty.util.internal.StringUtil;
import io.qdrant.client.grpc.Points;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/chatapplicaiton")
public class ChatController {

    @Autowired
    private PromptServiceImpl promptService;

    @Autowired
    private SimpleVectorActions simpleVectorActions;

    @GetMapping("/message")
    public ResponseEntity message(@RequestParam(required = false) String text) {
        if(StringUtil.isNullOrEmpty(text)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad requst");
        }

        ChatResponse response = new ChatResponse(text, promptService.getChatCompletions(text));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/messageWithTemp")
    public ResponseEntity<ChatResponse> messageWithTemp(@RequestBody ChatRequest chatRequest) {
        ChatResponse chatResponse = promptService.getChatCompletionsWithTemp(chatRequest);
        return new ResponseEntity<>(chatResponse, HttpStatus.OK);
    }

    @PostMapping("/messageWithModel")
    public ResponseEntity<ChatResponse> messageWithModel(@RequestBody ChatRequest chatRequest) {
        ChatResponse chatResponse = promptService.getChatCompletionsWithModel(chatRequest);
        return new ResponseEntity<>(chatResponse, HttpStatus.OK);
    }

    @PostMapping("/saveText")
    public ResponseEntity saveVectorText(@RequestParam(required = false) String text) {
        if(StringUtil.isNullOrEmpty(text)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad requst");
        }
        try {
            simpleVectorActions.processAndSaveText(text);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Exception occurred while saving text");
        }
        return ResponseEntity.status(HttpStatus.OK).body("saving text");
    }

    @GetMapping("/searchText")
    public ResponseEntity searchVectorText(@RequestParam(required = false) String text) {
        if(StringUtil.isNullOrEmpty(text)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad requst");
        }
        try {
            List<Points.ScoredPoint> scoredPointList = simpleVectorActions.search(text);
            return ResponseEntity.status(HttpStatus.OK).body(scoredPointList.toString());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Exception occurred while saving text");
        }
    }
}

