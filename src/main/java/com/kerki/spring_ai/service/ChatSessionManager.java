package com.kerki.spring_ai.service;

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

    public void addMessage(String sessionId, Message message) {
        redisTemplate.opsForList().rightPush("chat_session:" + sessionId, message.getText());
    }

    public List<Message> getSessionMessages(String sessionId) {
        List<String> jsonMessages = redisTemplate.opsForList().range("chat_session:" + sessionId, 0, -1);
        if (jsonMessages == null) return new ArrayList<>();
        return jsonMessages.stream().map(json -> new AssistantMessage(json)).collect(Collectors.toList());
    }

    public boolean clearSession(String sessionId) {
        return redisTemplate.delete("chat_session:" + sessionId);
    }
}

