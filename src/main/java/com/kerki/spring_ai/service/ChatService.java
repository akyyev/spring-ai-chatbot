package com.kerki.spring_ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatClient chatClient;
    private final ChatSessionManager chatSessionManager;

    public String sendMessage(String sessionId, UserMessage message) {
        // Add user message to session history
        chatSessionManager.addMessage(sessionId, message);

        // Retrieve full message history for context
        var history = chatSessionManager.getSessionMessages(sessionId);

        // Send prompt to chat client and get response
        var response = chatClient.prompt(new Prompt(history)).call();

        // Extract assistant's reply
        String assistantReply = response.chatResponse().getResult().getOutput().getText();

        // Add assistant's reply to session history
        chatSessionManager.addMessage(sessionId, new AssistantMessage(assistantReply));

        return assistantReply;
    }

    public boolean clearSession(String sessionId) {
        return chatSessionManager.clearSession(sessionId);
    }

}
