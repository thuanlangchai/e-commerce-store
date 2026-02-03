package com.minhthuan.web_store.domain.supportchat.message;

import com.minhthuan.web_store.domain.supportchat.message.dto.MessageRequest;
import com.minhthuan.web_store.domain.supportchat.message.dto.MessageResponse;
import com.minhthuan.web_store.domain.supportchat.message.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    Message toMessage(MessageRequest messageRequest);
    @Mapping(source = "conversation.id", target = "conversationId")
    @Mapping(source = "sender.id", target = "senderId")
    MessageResponse toMessageResponse(Message message);

}
