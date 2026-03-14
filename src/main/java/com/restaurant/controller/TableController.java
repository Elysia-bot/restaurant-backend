package com.restaurant.controller;

import com.restaurant.dto.response.TableResponse;
import com.restaurant.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    /**
     * GET /api/tables/scan?qr=TABLE_3
     * Guest scan QR → nhận tableId + tableNumber để lưu ở client
     * Frontend lưu tableId vào localStorage, dùng cho mọi request sau
     */
    @GetMapping("/scan")
    public ResponseEntity<TableResponse> scan(@RequestParam String qr) {
        return ResponseEntity.ok(tableService.scanQr(qr));
    }

    /**
     * GET /api/tables
     * Admin xem tất cả bàn + trạng thái occupied
     */
    @GetMapping
    public ResponseEntity<List<TableResponse>> getAllTables() {
        return ResponseEntity.ok(tableService.getAllTables());
    }

    /**
     * POST /api/tables
     * Admin tạo bàn mới
     * Body: { "tableNumber": 5 }
     */
    @PostMapping
    public ResponseEntity<TableResponse> createTable(@RequestParam Integer tableNumber) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tableService.createTable(tableNumber));
    }

    /**
     * PUT /api/tables/{id}/free
     * Admin giải phóng bàn sau khi khách đi về
     */
    @PutMapping("/{id}/free")
    public ResponseEntity<Void> freeTable(@PathVariable Long id) {
        tableService.freeTable(id);
        return ResponseEntity.noContent().build();
    }
}
