package com.minhthuan.web_store.domain.supportchat.conversation.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ConversationResponse {
    private Long id;
    private Long customerId;
    private Long staffId;
    private String status;
}
