package com.restaurant.service;

import com.restaurant.dto.response.TableResponse;
import com.restaurant.entity.RestaurantTable;
import com.restaurant.repository.RestaurantTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService {

    private final RestaurantTableRepository tableRepository;

    // Guest scan QR → tìm bàn theo mã QR hardcode
    // QR vật lý chứa URL: http://localhost:3000/scan?qr=TABLE_3
    // Frontend gọi: GET /api/tables/scan?qr=TABLE_3
    public TableResponse scanQr(String qrCode) {
        RestaurantTable table = tableRepository.findByQrCode(qrCode)
                .orElseThrow(() -> new RuntimeException("QR Code không hợp lệ: " + qrCode));

        // Đánh dấu bàn đang có khách
        table.setIsOccupied(true);
        tableRepository.save(table);

        return toResponse(table);
    }

    // Admin xem danh sách tất cả bàn
    public List<TableResponse> getAllTables() {
        return tableRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    // Admin tạo bàn mới (tạo đồng thời mã QR hardcode)
    @Transactional
    public TableResponse createTable(Integer tableNumber) {
        if (tableRepository.existsByTableNumber(tableNumber)) {
            throw new RuntimeException("Bàn số " + tableNumber + " đã tồn tại");
        }

        RestaurantTable table = RestaurantTable.builder()
                .tableNumber(tableNumber)
                .qrCode("TABLE_" + tableNumber)   // hardcode theo số bàn
                .isOccupied(false)
                .build();

        return toResponse(tableRepository.save(table));
    }

    // Admin giải phóng bàn sau khi khách rời đi
    @Transactional
    public void freeTable(Long tableId) {
        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn"));
        table.setIsOccupied(false);
        tableRepository.save(table);
    }

    private TableResponse toResponse(RestaurantTable t) {
        return TableResponse.builder()
                .id(t.getId())
                .tableNumber(t.getTableNumber())
                .qrCode(t.getQrCode())
                .isOccupied(t.getIsOccupied())
                .build();
    }
}
