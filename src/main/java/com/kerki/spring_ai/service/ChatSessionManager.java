package com.kerki.spring_ai.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.kerki.spring_ai.dto.StoredMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatSessionManager {

    // message types
    // 1. UserMessage - this is the message from the user
    // 2. AssistantMessage - this is the response from the AI
    // 3. SystemMessage - this is a message from the system
    // 4. ToolResponse/FunctionMessage - this is a response from a tool or function

    private final RedisTemplate<String, StoredMessage> redisTemplate;
    private final static String SESSION_PREFIX = "chat_session:";

    public void addMessage(String sessionId, Message message) {
        var type = message instanceof AssistantMessage ? "assistant" : 
                   message instanceof UserMessage ? "user" :
                   message instanceof SystemMessage ? "system" : "unknown";

        StoredMessage storedMessage = new StoredMessage(message.getText(), type);

        redisTemplate.opsForList().rightPush(SESSION_PREFIX + sessionId, storedMessage);
        redisTemplate.expire(SESSION_PREFIX + sessionId, Duration.ofDays(10));
    }

    public List<Message> getSessionMessages(String sessionId) {
        List<StoredMessage> jsonMessages = redisTemplate
            .opsForList()
            .range(SESSION_PREFIX + sessionId, 0, -1);

        if (jsonMessages == null) return new ArrayList<>();
        return jsonMessages.stream().map(msg -> {
            switch (msg.getType()) {
                case "user":
                    return new UserMessage(msg.getText());
                case "assistant":
                    return new AssistantMessage(msg.getText());
                case "system":
                    return new SystemMessage(msg.getText());
                default:
                    return null;
            }
        }).collect(Collectors.toList());
    }

    public boolean clearSession(String sessionId) {
        return redisTemplate.delete(SESSION_PREFIX + sessionId);
    }

    public List<String> getMessageHistory(String sessionId) {
        return getSessionMessages(sessionId)
                .stream()
                .map(Message::getText)
                .collect(Collectors.toList());
    }
}

