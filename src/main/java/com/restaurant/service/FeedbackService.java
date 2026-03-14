package com.restaurant.service;

import com.restaurant.dto.request.FeedbackRequest;
import com.restaurant.dto.response.FeedbackResponse;
import com.restaurant.entity.Feedback;
import com.restaurant.entity.RestaurantTable;
import com.restaurant.repository.FeedbackRepository;
import com.restaurant.repository.RestaurantTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final RestaurantTableRepository tableRepository;

    // Guest gửi đánh giá sau bữa ăn
    public FeedbackResponse createFeedback(FeedbackRequest request) {
        RestaurantTable table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn"));

        Feedback feedback = Feedback.builder()
                .table(table)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        return toResponse(feedbackRepository.save(feedback));
    }

    // Admin xem tất cả feedback
    public List<FeedbackResponse> getAllFeedback() {
        return feedbackRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::toResponse).toList();
    }

    // Admin xem rating trung bình
    public Double getAverageRating() {
        return feedbackRepository.findAverageRating();
    }

    private FeedbackResponse toResponse(Feedback f) {
        return FeedbackResponse.builder()
                .id(f.getId())
                .tableId(f.getTable().getId())
                .tableNumber(f.getTable().getTableNumber())
                .rating(f.getRating())
                .comment(f.getComment())
                .createdAt(f.getCreatedAt())
                .build();
    }
}
