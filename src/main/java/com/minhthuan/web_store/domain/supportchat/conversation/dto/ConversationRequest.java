package com.minhthuan.web_store.domain.supportchat.conversation.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ConversationRequest {
    private Long customerId;
    private Long staffId;
    private String status;
}
