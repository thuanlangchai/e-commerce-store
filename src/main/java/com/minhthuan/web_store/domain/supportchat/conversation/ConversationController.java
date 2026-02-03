package com.minhthuan.web_store.domain.supportchat.conversation;

import com.minhthuan.web_store.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/support-chat")
public class ConversationController {
    private final ConversationService conversationService;

    @PostMapping("/start")
    public ResponseEntity<ApiResponse<?>> startConversation() {
        return ResponseEntity.ok(conversationService.createConversation());
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<ApiResponse<?>> assignStaff(@PathVariable Long id) {
        return ResponseEntity.ok(conversationService.assignSupportStaff(id));
    }

    @GetMapping("/conversations")
    public ResponseEntity<ApiResponse<?>> getConversations() {
        return ResponseEntity.ok(conversationService.getAllConversations());
    }
}
