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

public class LoginActivity extends AppCompatActivity {

    TextInputEditText username, password;
    Button btnLogin;
    TextView tvGoToRegister;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ
        username = findViewById(R.id.input_username);
        password = findViewById(R.id.input_password);
        btnLogin = findViewById(R.id.btnlogin);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);

        dbHelper = new DatabaseHelper(this);

        // Nút Đăng nhập
        btnLogin.setOnClickListener(v -> {
            String userStr = String.valueOf(username.getText()).trim();
            String passStr = String.valueOf(password.getText()).trim();

            if (userStr.isEmpty() || passStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gọi hàm xử lý đăng nhập
            loginUser(userStr, passStr);
        });

        // Chuyển sang trang Đăng ký
        tvGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser(String username, String password) {
        User loginRequest = new User(username, null, password);
        ApiService apiService = RetrofitClient.getApiService();
        apiService.login(loginRequest).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    User user = response.body().getUser();
                    if (user != null) {
                        saveUserToLocal(user);
                        goToHome();
                    } else {
                        Toast.makeText(LoginActivity.this, "Lỗi: Dữ liệu người dùng trống!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Nếu API thất bại hoặc không kết nối được server thực tế
                    // Thử kiểm tra trong SQLite (để hỗ trợ offline/local test)
                    if (dbHelper.checkUser(username, password)) {
                        User user = dbHelper.getUser(username);
                        saveUserToLocal(user);
                        Toast.makeText(LoginActivity.this, "Đăng nhập local thành công!", Toast.LENGTH_SHORT).show();
                        goToHome();
                    } else if (username.equals("admin") && password.equals("123456")) {
                        // Tài khoản admin mặc định
                        saveUserToLocal(new User("admin", "admin@hit.com", "123456"));
                        goToHome();
                    } else {
                        String msg = (response.body() != null) ? response.body().getMessage() : "Sai tài khoản hoặc mật khẩu!";
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // Khi lỗi kết nối (ví dụ server chưa chạy), cho phép đăng nhập qua SQLite
                if (dbHelper.checkUser(username, password)) {
                    User user = dbHelper.getUser(username);
                    saveUserToLocal(user);
                    Toast.makeText(LoginActivity.this, "Đăng nhập offline thành công!", Toast.LENGTH_SHORT).show();
                    goToHome();
                } else {
                    Toast.makeText(LoginActivity.this, "Lỗi kết nối hoặc tài khoản không tồn tại local!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveUserToLocal(User user) {
        if (user == null) return;
        // Lưu vào SharedPreferences để dùng ở các màn hình khác
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", user.getUsername());
        editor.putString("email", user.getEmail());
        editor.apply();
        
        // Cập nhật hoặc thêm vào SQLite nếu cần
        if (dbHelper.getUser(user.getUsername()) == null) {
            dbHelper.addUser(user);
        }
    }

    private void goToHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
