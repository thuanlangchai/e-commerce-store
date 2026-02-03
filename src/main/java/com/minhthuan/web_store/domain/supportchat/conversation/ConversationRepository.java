package com.minhthuan.web_store.domain.supportchat.conversation;

import com.minhthuan.web_store.domain.supportchat.conversation.entity.Conversation;
import com.minhthuan.web_store.domain.supportchat.conversation.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findAllByStatus(Status status);

}
