package com.epam.training.gen.ai.chat.controller;

import com.epam.training.gen.ai.chat.prompt.SimplePromptService;
import io.netty.util.internal.StringUtil;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatController {

    private SimplePromptService simplePromptService;

    public Object message(@RequestParam String message) {
        if(StringUtil.isNullOrEmpty(message)) {
            return "Empty user message";
        }
        String response = simplePromptService.getChatCompletions(message);
        return response;
    }
}

