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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText regUsername, regEmail, regPassword, regConfirmPassword;
    Button btnRegister;
    TextView tvBackToLogin;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Ánh xạ
        regUsername = findViewById(R.id.reg_username);
        regEmail = findViewById(R.id.reg_email);
        regPassword = findViewById(R.id.reg_password);
        regConfirmPassword = findViewById(R.id.reg_confirm_password);
        btnRegister = findViewById(R.id.btnRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        dbHelper = new DatabaseHelper(this);

        // Nút Đăng ký
        btnRegister.setOnClickListener(v -> {
            String username = String.valueOf(regUsername.getText()).trim();
            String email = String.valueOf(regEmail.getText()).trim();
            String pass = String.valueOf(regPassword.getText()).trim();
            String confirmPass = String.valueOf(regConfirmPassword.getText()).trim();

            if (username.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else if (!pass.equals(confirmPass)) {
                Toast.makeText(this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
            } else if (dbHelper.getUser(username) != null) {
                Toast.makeText(this, "Tên tài khoản đã tồn tại trên thiết bị!", Toast.LENGTH_SHORT).show();
            } else {
                User user = new User(username, email, pass);
                registerUser(user);
            }
        });

        // Quay lại Login
        tvBackToLogin.setOnClickListener(v -> finish());
    }

    private void registerUser(User user) {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.register(user).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        // Lưu vào SQLite
                        dbHelper.addUser(user);
                        
                        // Lưu vào SharedPreferences để duy trì phiên (tùy chọn)
                        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("username", user.getUsername());
                        editor.putString("email", user.getEmail());
                        editor.apply();

                        Toast.makeText(RegisterActivity.this, "Chúc mừng " + user.getUsername() + ", bạn đã đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Nếu API lỗi (ví dụ chưa có server), vẫn lưu local để test
                    if (dbHelper.getUser(user.getUsername()) == null) {
                        dbHelper.addUser(user);
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công (Lưu nội bộ)!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Tài khoản đã tồn tại nội bộ!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // Lưu local khi offline
                if (dbHelper.getUser(user.getUsername()) == null) {
                    dbHelper.addUser(user);
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công (Chế độ Offline)!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Lỗi kết nối: Tài khoản đã tồn tại nội bộ!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}