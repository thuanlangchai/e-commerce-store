package com.minhthuan.web_store.domain.supportchat.conversation;

import com.minhthuan.web_store.common.response.ApiResponse;
import com.minhthuan.web_store.common.utils.JwtUtil;
import com.minhthuan.web_store.domain.store.user.entity.User;
import com.minhthuan.web_store.domain.supportchat.conversation.dto.ConversationResponse;
import com.minhthuan.web_store.domain.supportchat.conversation.entity.Conversation;
import com.minhthuan.web_store.domain.supportchat.conversation.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private  final ConversationRepository conversationRepository;
    private final ConversationMapper conversationMapper;
    private final JwtUtil jwtUtil;

    public ApiResponse<ConversationResponse> createConversation() {
        User user = jwtUtil.getCurrentUser();
        Conversation conversation = Conversation.builder()
                .customer(user)
                .status(Status.PENDING)
                .build();
        // Persist to obtain generated ID
        Conversation saved = conversationRepository.save(conversation);
        ConversationResponse conversationResponse = conversationMapper.toConversationResponse(saved);
        return new ApiResponse<>(conversationResponse, "Create conversation successfully");
    }

    public ApiResponse<ConversationResponse> assignSupportStaff(Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        User supportStaff = jwtUtil.getCurrentUser();
        conversation.setStaff(supportStaff);
        conversation.setStatus(Status.ACTIVE);
        conversationRepository.save(conversation);
        ConversationResponse conversationResponse = conversationMapper.toConversationResponse(conversation);
        return new ApiResponse<>(conversationResponse, "Conversation is active now");
    }

    public ApiResponse<List<ConversationResponse>> getAllConversations() {
        List<Conversation> conversations = conversationRepository.findAll();
        List<ConversationResponse> responses = conversations.stream()
                .map(conversationMapper::toConversationResponse)
                .toList();
        return new ApiResponse<>(responses, "Get conversations successfully");
    }
}
