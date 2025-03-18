package com.epam.training.gen.ai.chat.controller;

import com.epam.training.gen.ai.chat.prompt.SimplePromptService;
import io.netty.util.internal.StringUtil;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatapplicaiton")
public class ChatController {

    @Autowired
    private SimplePromptService simplePromptService;

    @GetMapping("/message")
    public String message(@RequestParam(required = false) String text) {
        if(StringUtil.isNullOrEmpty(text)) {
            return "Empty user input";
        }
        String response = simplePromptService.getChatCompletions(text);
        return response;
    }
}

