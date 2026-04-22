package com.example.hitcapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    TextView txtProfileName, txtProfileEmail;
    Button btnLogout;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        loadUserData();
        setupBottomNav();

        btnLogout.setOnClickListener(v -> {
            // Xử lý đăng xuất
            // Thường thì sẽ xóa trạng thái đăng nhập nếu có dùng session/token
            // Ở đây đơn giản là quay lại màn hình Login và xóa activity stack
            Toast.makeText(this, "Đã đăng xuất thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void initViews() {
        txtProfileName = findViewById(R.id.txtProfileName);
        txtProfileEmail = findViewById(R.id.txtProfileEmail);
        btnLogout = findViewById(R.id.btnLogout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void loadUserData() {
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", "Người dùng");
        String email = sharedPref.getString("email", "user@example.com");

        txtProfileName.setText(username);
        txtProfileEmail.setText(email);
    }

    private void setupBottomNav() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_info);
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
            } else if (id == R.id.navigation_info) {
                return true;
            }
            return false;
        });
    }
}
