package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private ListView lvNotifications;
    private ArrayList<Notification> notificationList;
    private NotificationAdapter adapter;
    private ImageButton btnBack;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initViews();
        setupData();
        setupBottomNavigation();

        btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        lvNotifications = findViewById(R.id.lvNotifications);
        btnBack = findViewById(R.id.btnBackNotification);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void setupData() {
        notificationList = new ArrayList<>();
        
        // Thêm dữ liệu mẫu
        notificationList.add(new Notification(
                "Đơn hàng đã đặt thành công",
                "Chúc mừng! Đơn hàng #12345 của bạn đã được tiếp nhận và đang trong quá trình xử lý.",
                "20/05/2024",
                android.R.drawable.ic_dialog_info
        ));

        notificationList.add(new Notification(
                "Giao hàng thành công",
                "Đơn hàng sách 'Đắc Nhân Tâm' đã được giao thành công đến địa chỉ của bạn.",
                "19/05/2024",
                android.R.drawable.ic_dialog_info
        ));

        notificationList.add(new Notification(
                "Sách mới cực hot!",
                "Siêu phẩm 'Muôn Kiếp Nhân Sinh' đã có mặt tại cửa hàng. Khám phá ngay!",
                "18/05/2024",
                android.R.drawable.ic_dialog_alert
        ));

        notificationList.add(new Notification(
                "Khuyến mãi đặc biệt",
                "Giảm giá 20% cho tất cả các loại sách khoa học trong tuần này.",
                "17/05/2024",
                android.R.drawable.ic_menu_today
        ));

        adapter = new NotificationAdapter(this, R.layout.item_notification, notificationList);
        lvNotifications.setAdapter(adapter);
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_message);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (id == R.id.navigation_books) {
                startActivity(new Intent(this, BookActivity.class));
                finish();
                return true;
            } else if (id == R.id.navigation_cart) {
                startActivity(new Intent(this, CartActivity.class));
                finish();
                return true;
            } else if (id == R.id.navigation_message) {
                return true;
            } else if (id == R.id.navigation_info) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
}
