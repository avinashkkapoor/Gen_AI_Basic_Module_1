package com.epam.training.gen.ai.chat.model;


public class ChatResponse {
    private String input;
    private String answer;


    public ChatResponse(String input, String answer) {
        this.input = input;
        this.answer = answer;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
