package com.restaurant.controller;

import com.restaurant.dto.request.SupportMessageRequest;
import com.restaurant.dto.response.SupportMessageResponse;
import com.restaurant.service.SupportMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/support-messages")
@RequiredArgsConstructor
public class SupportMessageController {

    private final SupportMessageService supportMessageService;

    /**
     * POST /api/support-messages
     * Public (Guest) - Gửi tin nhắn hỗ trợ qua bubble chat
     *
     * Body:
     * {
     *   "tableId": 3,
     *   "message": "Bàn mình cần thêm đũa"
     * }
     */
    @PostMapping
    public ResponseEntity<SupportMessageResponse> createMessage(
            @Valid @RequestBody SupportMessageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(supportMessageService.createMessage(request));
    }

    /**
     * GET /api/support-messages
     * Admin only - Xem tất cả tin nhắn, mới nhất lên đầu
     */
    @GetMapping
    public ResponseEntity<List<SupportMessageResponse>> getAllMessages() {
        return ResponseEntity.ok(supportMessageService.getAllMessages());
    }

    /**
     * PUT /api/support-messages/{id}/read
     * Admin only - Đánh dấu đã đọc tin nhắn
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        supportMessageService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/support-messages/unread-count
     * Admin only - Số tin chưa đọc, dùng cho badge notification
     */
    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount() {
        return ResponseEntity.ok(Map.of("unreadCount", supportMessageService.countUnread()));
    }
}
