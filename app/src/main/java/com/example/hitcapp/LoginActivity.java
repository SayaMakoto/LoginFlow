package com.example.hitcapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText username, password;
    Button btnLogin;
    TextView tvGoToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ
        username = findViewById(R.id.input_username);
        password = findViewById(R.id.input_password);
        btnLogin = findViewById(R.id.btnlogin);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);

        // Nút Đăng nhập
        btnLogin.setOnClickListener(v -> {
            String user = String.valueOf(username.getText()).trim();
            String pass = String.valueOf(password.getText()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy dữ liệu từ SharedPreferences
            SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String savedUser = sharedPref.getString("username", "");
            String savedPass = sharedPref.getString("password", "");

            // Kiểm tra (Thêm tài khoản admin mặc định cho tiện test)
            if ((user.equals(savedUser) && pass.equals(savedPass)) || (user.equals("admin") && pass.equals("123456"))) {
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Sai tên tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
            }
        });

        // Chuyển sang trang Đăng ký
        tvGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}