package com.epam.training.gen.ai.chat.controller;

import com.epam.training.gen.ai.chat.model.ChatRequest;
import com.epam.training.gen.ai.chat.model.ChatResponse;
import com.epam.training.gen.ai.chat.prompt.PromptServiceImpl;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatapplicaiton")
public class ChatController {

    @Autowired
    private PromptServiceImpl promptService;

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
}

