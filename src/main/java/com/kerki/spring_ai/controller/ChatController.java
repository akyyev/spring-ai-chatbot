package com.kerki.spring_ai.controller;

import java.util.List;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.kerki.spring_ai.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    // This class is currently empty, but it can be used to handle HTTP requests related to asking questions.
    // You can implement methods here to interact with the ChatService and return responses to the client.

    private final ChatService chatService;

    // Example method to handle a POST request for sending a message:
    @PostMapping("/ask")
    public ResponseEntity<String> askQuestion(@RequestParam String sessionId, @RequestBody String question) {
        String response = chatService.sendMessage(sessionId, new UserMessage(question));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clear-session")
    public ResponseEntity<Void> clearSession(@RequestParam String sessionId) {
        boolean cleared = chatService.clearSession(sessionId);
        log.info("Session cleared: {}, id: {}", cleared, sessionId);
        if (cleared) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<String>> getHistory(@RequestParam String sessionId) {
        List<String> history = chatService.getMessageHistory(sessionId);
        return ResponseEntity.ok(history);
    }

}