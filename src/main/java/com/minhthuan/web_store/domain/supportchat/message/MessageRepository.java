package com.minhthuan.web_store.domain.supportchat.message;

import com.minhthuan.web_store.domain.supportchat.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
    List<Message> findAllByConversationId(Long conversationId);
}
