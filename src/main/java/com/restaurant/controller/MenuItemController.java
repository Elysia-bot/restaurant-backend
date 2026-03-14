package com.restaurant.controller;

import com.restaurant.dto.request.MenuItemRequest;
import com.restaurant.dto.response.MenuItemResponse;
import com.restaurant.service.MenuItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    /**
     * GET /api/menu-items
     * Public - Guest dùng, hỗ trợ filter + search
     *
     * Query params (tất cả optional):
     *   name       = tên món (tìm gần đúng)
     *   categoryId = lọc theo category
     *   minPrice   = giá từ
     *   maxPrice   = giá đến
     *
     * VD: GET /api/menu-items?name=cơm&categoryId=1&maxPrice=50000
     * VD: GET /api/menu-items  (lấy tất cả)
     */
    @GetMapping
    public ResponseEntity<List<MenuItemResponse>> searchMenuItems(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        return ResponseEntity.ok(
                menuItemService.searchMenuItems(name, categoryId, minPrice, maxPrice));
    }

    /**
     * GET /api/menu-items/{id}
     * Public - Guest xem chi tiết món (trang detail)
     */
    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(menuItemService.getById(id));
    }

    /**
     * POST /api/menu-items
     * Admin only - Thêm món mới
     */
    @PostMapping
    public ResponseEntity<MenuItemResponse> createMenuItem(
            @Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(menuItemService.createMenuItem(request));
    }

    /**
     * PUT /api/menu-items/{id}
     * Admin only - Sửa thông tin món
     */
    @PutMapping("/{id}")
    public ResponseEntity<MenuItemResponse> updateMenuItem(
            @PathVariable Long id,
            @Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.ok(menuItemService.updateMenuItem(id, request));
    }

    /**
     * DELETE /api/menu-items/{id}
     * Admin only - Xóa món
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * PATCH /api/menu-items/{id}/toggle-availability
     * Admin only - Toggle ẩn/hiện món (tạm hết hàng)
     */
    @PatchMapping("/{id}/toggle-availability")
    public ResponseEntity<MenuItemResponse> toggleAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(menuItemService.toggleAvailability(id));
    }
}
