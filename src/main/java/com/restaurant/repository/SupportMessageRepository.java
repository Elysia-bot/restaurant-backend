package com.restaurant.repository;

import com.restaurant.entity.SupportMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportMessageRepository extends JpaRepository<SupportMessage, Long> {

    List<SupportMessage> findAllByOrderByCreatedAtDesc();

    // Số tin chưa đọc — admin dùng để hiển thị badge
    long countByIsReadFalse();
}
