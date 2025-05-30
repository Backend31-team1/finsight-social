package com.project.sns.repository;

import com.project.sns.entity.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

    @Query("SELECT cp.chatRoom.roomId FROM ChatParticipant cp WHERE cp.userId = :userId")
    List<Long> findRoomIdsByUserId(Long myUserId);

    List<ChatParticipant> findByChatRoom_RoomId(Long roomId);
}
