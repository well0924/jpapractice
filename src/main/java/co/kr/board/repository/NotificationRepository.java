package co.kr.board.repository;

import co.kr.board.domain.Dto.NoticeDto;
import co.kr.board.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification,Integer> {
    //회원별 알림 목록
    @Query(value = "select n from Notification  n where n.member.username =:username and n.isRead = false")
    List<NoticeDto>findAllByNotification(@Param("username") String username);
    
    //알림 단일 조회
    @Query(value = "select n from Notification n where n.member.username = :username and n.id = :id")
    Optional<NoticeDto>findByUsername(String username,Integer id);
}
