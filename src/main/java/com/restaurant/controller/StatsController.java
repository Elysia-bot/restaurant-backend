package com.restaurant.controller;

import com.restaurant.dto.response.RevenueResponse;
import com.restaurant.dto.response.TopItemResponse;
import com.restaurant.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    /**
     * GET /api/stats/revenue
     * Admin only - Doanh thu theo khoảng ngày
     *
     * Query params (optional):
     *   from = yyyy-MM-dd  (mặc định: hôm nay)
     *   to   = yyyy-MM-dd  (mặc định: hôm nay)
     *
     * VD: GET /api/stats/revenue?from=2025-01-01&to=2025-01-31
     * VD: GET /api/stats/revenue  → doanh thu hôm nay
     */
    @GetMapping("/revenue")
    public ResponseEntity<RevenueResponse> getRevenue(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(statsService.getRevenue(from, to));
    }

    /**
     * GET /api/stats/top-items
     * Admin only - Top món bán chạy nhất (tất cả thời gian)
     */
    @GetMapping("/top-items")
    public ResponseEntity<List<TopItemResponse>> getTopItems() {
        return ResponseEntity.ok(statsService.getTopItems());
    }
}
