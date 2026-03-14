package com.restaurant.service;

import com.restaurant.dto.request.MenuItemRequest;
import com.restaurant.dto.response.MenuItemResponse;
import com.restaurant.entity.Category;
import com.restaurant.entity.MenuItem;
import com.restaurant.repository.CategoryRepository;
import com.restaurant.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;

    // Guest dùng: search + filter kết hợp
    public List<MenuItemResponse> searchMenuItems(String name,
                                                   Long categoryId,
                                                   BigDecimal minPrice,
                                                   BigDecimal maxPrice) {
        return menuItemRepository
                .searchMenuItems(name, categoryId, minPrice, maxPrice)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Guest xem chi tiết món (trang detail)
    public MenuItemResponse getById(Long id) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy món ăn"));
        return toResponse(item);
    }

    // Admin thêm món
    @Transactional
    public MenuItemResponse createMenuItem(MenuItemRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy category"));

        MenuItem item = MenuItem.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .category(category)
                .isAvailable(request.getIsAvailable())
                .build();

        return toResponse(menuItemRepository.save(item));
    }

    // Admin sửa món
    @Transactional
    public MenuItemResponse updateMenuItem(Long id, MenuItemRequest request) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy món ăn"));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy category"));

        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setImageUrl(request.getImageUrl());
        item.setCategory(category);
        item.setIsAvailable(request.getIsAvailable());

        return toResponse(menuItemRepository.save(item));
    }

    // Admin xóa món
    @Transactional
    public void deleteMenuItem(Long id) {
        menuItemRepository.deleteById(id);
    }

    // Admin toggle ẩn/hiện món (tạm hết hàng)
    @Transactional
    public MenuItemResponse toggleAvailability(Long id) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy món ăn"));
        item.setIsAvailable(!item.getIsAvailable());
        return toResponse(menuItemRepository.save(item));
    }

    private MenuItemResponse toResponse(MenuItem m) {
        return MenuItemResponse.builder()
                .id(m.getId())
                .name(m.getName())
                .description(m.getDescription())
                .price(m.getPrice())
                .imageUrl(m.getImageUrl())
                .categoryId(m.getCategory().getId())
                .categoryName(m.getCategory().getName())
                .isAvailable(m.getIsAvailable())
                .build();
    }
}
