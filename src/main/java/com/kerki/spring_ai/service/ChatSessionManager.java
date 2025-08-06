package com.kerki.spring_ai.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatSessionManager {

    private final RedisTemplate<String, String> redisTemplate;
    private final static String SESSION_PREFIX = "chat_session:";

    public void addMessage(String sessionId, Message message) {
        redisTemplate.opsForList().rightPush(SESSION_PREFIX + sessionId, message.getText());
        redisTemplate.expire(SESSION_PREFIX + sessionId, Duration.ofDays(10));
    }

    public List<Message> getSessionMessages(String sessionId) {
        List<String> jsonMessages = redisTemplate.opsForList().range(SESSION_PREFIX + sessionId, 0, -1);
        if (jsonMessages == null) return new ArrayList<>();
        return jsonMessages.stream().map(json -> new AssistantMessage(json)).collect(Collectors.toList());
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

