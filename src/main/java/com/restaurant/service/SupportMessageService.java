package com.restaurant.service;

import com.restaurant.dto.request.SupportMessageRequest;
import com.restaurant.dto.response.SupportMessageResponse;
import com.restaurant.entity.RestaurantTable;
import com.restaurant.entity.SupportMessage;
import com.restaurant.repository.RestaurantTableRepository;
import com.restaurant.repository.SupportMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportMessageService {

    private final SupportMessageRepository supportMessageRepository;
    private final RestaurantTableRepository tableRepository;

    // Guest gửi tin nhắn hỗ trợ
    public SupportMessageResponse createMessage(SupportMessageRequest request) {
        RestaurantTable table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn"));

        SupportMessage message = SupportMessage.builder()
                .table(table)
                .message(request.getMessage())
                .isRead(false)
                .build();

        return toResponse(supportMessageRepository.save(message));
    }

    // Admin xem tất cả tin nhắn
    public List<SupportMessageResponse> getAllMessages() {
        return supportMessageRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::toResponse).toList();
    }

    // Admin đánh dấu đã đọc
    @Transactional
    public void markAsRead(Long id) {
        SupportMessage message = supportMessageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tin nhắn"));
        message.setIsRead(true);
        supportMessageRepository.save(message);
    }

    // Số tin chưa đọc — dùng cho badge notification phía admin
    public long countUnread() {
        return supportMessageRepository.countByIsReadFalse();
    }

    private SupportMessageResponse toResponse(SupportMessage m) {
        return SupportMessageResponse.builder()
                .id(m.getId())
                .tableId(m.getTable().getId())
                .tableNumber(m.getTable().getTableNumber())
                .message(m.getMessage())
                .isRead(m.getIsRead())
                .createdAt(m.getCreatedAt())
                .build();
    }
}
