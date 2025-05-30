package com.project.sns.repository;

import com.project.sns.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> findTopByRoom_RoomIdOrderByCreatedAtDesc(Long roomId);
    Page<Message> findByRoom_RoomIdOrderByCreatedAtAsc(Long roomId, Pageable pageable);
}
