package com.minhthuan.web_store.domain.supportchat.message;

import com.minhthuan.web_store.common.excpetion.CustomException;
import com.minhthuan.web_store.common.excpetion.ErrorCode;
import com.minhthuan.web_store.domain.supportchat.conversation.ConversationRepository;
import com.minhthuan.web_store.domain.supportchat.conversation.ConversationService;
import com.minhthuan.web_store.domain.supportchat.conversation.entity.Conversation;
import com.minhthuan.web_store.domain.supportchat.message.dto.MessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebSocketMessageController {
    private final MessageService messageService;
    private final ConversationRepository conversationRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/support-chat/message")
    public void sendMessage(MessageRequest messageRequest) {
        messageService.saveMessage(messageRequest);
        Conversation con = conversationRepository.findById(messageRequest.getConversationId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        if (con.getStaff() == null) {
            simpMessagingTemplate.convertAndSend("/topic/support-waiting", messageRequest);
        }

        simpMessagingTemplate.convertAndSend(
                    "/topic/conversation." + messageRequest.getConversationId(),
                    messageRequest
        );
    }
}
