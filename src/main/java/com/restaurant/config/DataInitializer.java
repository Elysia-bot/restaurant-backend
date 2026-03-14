package com.restaurant.config;

import com.restaurant.entity.Admin;
import com.restaurant.entity.RestaurantTable;
import com.restaurant.repository.AdminRepository;
import com.restaurant.repository.RestaurantTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final RestaurantTableRepository tableRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initAdmin();
        initTables();
    }

    // Tạo admin mặc định nếu chưa có
    private void initAdmin() {
        if (adminRepository.findByUsername("admin").isEmpty()) {
            Admin admin = Admin.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))  // đổi pass sau khi deploy!
                    .fullName("Quản trị viên")
                    .build();
            adminRepository.save(admin);
            log.info("✅ Đã tạo admin mặc định — username: admin / password: admin123");
        }
    }

    // Tạo sẵn 10 bàn nếu DB chưa có bàn nào
    private void initTables() {
        if (tableRepository.count() == 0) {
            for (int i = 1; i <= 10; i++) {
                RestaurantTable table = RestaurantTable.builder()
                        .tableNumber(i)
                        .qrCode("TABLE_" + i)  // QR hardcode: TABLE_1, TABLE_2...
                        .isOccupied(false)
                        .build();
                tableRepository.save(table);
            }
            log.info("✅ Đã tạo 10 bàn mặc định (TABLE_1 → TABLE_10)");
        }
    }
}
