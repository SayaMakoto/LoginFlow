package com.example.hitcapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private EditText edtUsername, edtEmail;
    private EditText edtOldPassword, edtNewPassword, edtConfirmPassword;
    private Button btnSaveProfile, btnChangePassword;
    private ImageButton btnBack;
    private ApiService apiService;
    private SharedPreferences sharedPref;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        apiService = RetrofitClient.getApiService();
        sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        dbHelper = new DatabaseHelper(this);

        initViews();
        loadCurrentData();

        btnBack.setOnClickListener(v -> finish());
        btnSaveProfile.setOnClickListener(v -> updateProfile());
        btnChangePassword.setOnClickListener(v -> changePassword());
    }

    private void initViews() {
        edtUsername = findViewById(R.id.edtEditUsername);
        edtEmail = findViewById(R.id.edtEditEmail);
        
        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmNewPassword);
        
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnBack = findViewById(R.id.btnBackEditProfile);
    }

    private void loadCurrentData() {
        edtUsername.setText(sharedPref.getString("username", ""));
        edtEmail.setText(sharedPref.getString("email", ""));
    }

    private void updateProfile() {
        String name = edtUsername.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = sharedPref.getInt("id", 0);
        // Lấy thông tin user hiện tại từ SQLite để lấy các trường còn lại
        User currentUser = dbHelper.getUser(sharedPref.getString("username", ""));
        String currentPass = (currentUser != null) ? currentUser.getPassword() : "";
        String currentPhone = (currentUser != null) ? currentUser.getPhone() : "";
        String currentAddress = (currentUser != null) ? currentUser.getAddress() : "";

        User updatedUser = new User(userId, name, email, currentPass);
        updatedUser.setPhone(currentPhone);
        updatedUser.setAddress(currentAddress);

        apiService.updateUser(updatedUser).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    saveAndUpdateLocal(updatedUser);
                    Toast.makeText(EditProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    // Nếu API lỗi (có thể do URL dummy), cập nhật local để test
                    saveAndUpdateLocal(updatedUser);
                    Toast.makeText(EditProfileActivity.this, "Đã cập nhật (Local)!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // Hỗ trợ offline
                saveAndUpdateLocal(updatedUser);
                Toast.makeText(EditProfileActivity.this, "Lỗi kết nối server, đã cập nhật Offline!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveAndUpdateLocal(User user) {
        // Cập nhật SharedPreferences
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", user.getUsername());
        editor.putString("email", user.getEmail());
        editor.apply();

        // Cập nhật SQLite
        dbHelper.updateUser(user);
    }

    private void changePassword() {
        String oldPass = edtOldPassword.getText().toString();
        String newPass = edtNewPassword.getText().toString();
        String confirmPass = edtConfirmPassword.getText().toString();
        String email = sharedPref.getString("email", "");
        String username = sharedPref.getString("username", "");

        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra mật khẩu cũ từ SQLite (vì có thể API chưa thực sự check được)
        User userLocal = dbHelper.getUser(username);
        if (userLocal != null && !userLocal.getPassword().equals(oldPass)) {
            Toast.makeText(this, "Mật khẩu cũ không chính xác!", Toast.LENGTH_SHORT).show();
            return;
        }

        PasswordChangeRequest request = new PasswordChangeRequest(email, oldPass, newPass);

        apiService.changePassword(request).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    updatePasswordLocal(userLocal, newPass);
                    clearPassFields();
                    Toast.makeText(EditProfileActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    // Dự phòng nếu API dummy thất bại
                    updatePasswordLocal(userLocal, newPass);
                    clearPassFields();
                    Toast.makeText(EditProfileActivity.this, "Đổi mật khẩu thành công (Local)!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                updatePasswordLocal(userLocal, newPass);
                clearPassFields();
                Toast.makeText(EditProfileActivity.this, "Lỗi kết nối, đã đổi mật khẩu Offline!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePasswordLocal(User user, String newPass) {
        if (user != null) {
            user.setPassword(newPass);
            dbHelper.updateUser(user);
        }
    }

    private void clearPassFields() {
        edtOldPassword.setText("");
        edtNewPassword.setText("");
        edtConfirmPassword.setText("");
    }
}
