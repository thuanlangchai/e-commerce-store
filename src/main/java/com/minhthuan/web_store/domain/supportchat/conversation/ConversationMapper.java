package com.minhthuan.web_store.domain.supportchat.conversation;

import com.minhthuan.web_store.domain.supportchat.conversation.dto.ConversationResponse;
import com.minhthuan.web_store.domain.supportchat.conversation.entity.Conversation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConversationMapper {
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "staff.id", target = "staffId")
    @Mapping(source = "status", target = "status")
    ConversationResponse toConversationResponse(Conversation conversation);
}
