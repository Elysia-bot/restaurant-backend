package com.restaurant.controller;

import com.restaurant.dto.request.FeedbackRequest;
import com.restaurant.dto.response.FeedbackResponse;
import com.restaurant.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    /**
     * POST /api/feedback
     * Public (Guest) - Gửi đánh giá sau bữa ăn
     *
     * Body:
     * {
     *   "tableId": 3,
     *   "rating": 5,
     *   "comment": "Đồ ăn ngon, phục vụ nhanh!"
     * }
     */
    @PostMapping
    public ResponseEntity<FeedbackResponse> createFeedback(
            @Valid @RequestBody FeedbackRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(feedbackService.createFeedback(request));
    }

    /**
     * GET /api/feedback
     * Admin only - Xem tất cả feedback, mới nhất lên đầu
     */
    @GetMapping
    public ResponseEntity<List<FeedbackResponse>> getAllFeedback() {
        return ResponseEntity.ok(feedbackService.getAllFeedback());
    }

    /**
     * GET /api/feedback/average-rating
     * Admin only - Rating trung bình toàn quán
     */
    @GetMapping("/average-rating")
    public ResponseEntity<Map<String, Object>> getAverageRating() {
        Double avg = feedbackService.getAverageRating();
        return ResponseEntity.ok(Map.of(
                "averageRating", avg != null ? avg : 0.0
        ));
    }
}
