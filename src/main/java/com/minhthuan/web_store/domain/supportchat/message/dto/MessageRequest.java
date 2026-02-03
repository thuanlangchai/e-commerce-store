package com.minhthuan.web_store.domain.supportchat.message.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MessageRequest {
    private Long conversationId;
    private Long senderId;
    private String message;
}
