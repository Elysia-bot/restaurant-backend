package com.restaurant.repository;

import com.restaurant.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findAllByOrderByCreatedAtDesc();

    // Điểm trung bình rating
    @Query("SELECT AVG(f.rating) FROM Feedback f")
    Double findAverageRating();
}
