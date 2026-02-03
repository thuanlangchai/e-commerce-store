package com.minhthuan.web_store.domain.supportchat.message;

import com.minhthuan.web_store.common.excpetion.CustomException;
import com.minhthuan.web_store.common.excpetion.ErrorCode;
import com.minhthuan.web_store.common.response.ApiResponse;
import com.minhthuan.web_store.common.utils.JwtUtil;
import com.minhthuan.web_store.domain.store.user.entity.Role;
import com.minhthuan.web_store.domain.store.user.entity.User;
import com.minhthuan.web_store.domain.supportchat.conversation.ConversationRepository;
import com.minhthuan.web_store.domain.supportchat.conversation.entity.Conversation;
import com.minhthuan.web_store.domain.supportchat.message.dto.MessageRequest;
import com.minhthuan.web_store.domain.supportchat.message.dto.MessageResponse;
import com.minhthuan.web_store.domain.supportchat.message.entity.Message;
import com.minhthuan.web_store.domain.supportchat.message.entity.Type;
import com.minhthuan.web_store.domain.store.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final JwtUtil jwtUtil;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    public ApiResponse<MessageResponse> saveMessage(MessageRequest messageRequest) {
        Message message = messageMapper.toMessage(messageRequest);
        Conversation conversation = conversationRepository.findById(messageRequest.getConversationId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        User user;
        if (messageRequest.getSenderId() != null) {
            user = userRepository.findById(messageRequest.getSenderId())
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        } else {
            user = jwtUtil.getCurrentUser();
        }
        message.setConversation(conversation);
        message.setSender(user);
        if (user.getRole() == Role.USER) {
            message.setType(Type.CUSTOMER);
        }else
            message.setType(Type.STAFF);

        MessageResponse messageResponse = messageMapper.toMessageResponse(messageRepository.save(message));
        return new ApiResponse<>(messageResponse, "Message saved successfully");
    }

    public ApiResponse<List<MessageResponse>> getMessagesByConversationId(Long conversationId) {
        List<Message> messages = messageRepository.findAllByConversationId(conversationId);
        List<MessageResponse> messageResponses = messages.stream()
                .map(messageMapper::toMessageResponse)
                .toList();
        return new ApiResponse<>(messageResponses, "Get messages successfully");
    }
}
