package com.minhthuan.web_store.domain.supportchat.message;

import com.minhthuan.web_store.common.response.ApiResponse;
import com.minhthuan.web_store.domain.supportchat.message.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/support-chat/messages")
public class MessageController {
    private final MessageService messageService;

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getMessagesByConversation(@PathVariable Long conversationId) {
        return ResponseEntity.ok(messageService.getMessagesByConversationId(conversationId));
    }
}
