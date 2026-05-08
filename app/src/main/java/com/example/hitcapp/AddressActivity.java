package com.example.hitcapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class AddressActivity extends AppCompatActivity {

    private ImageButton btnBackAddress;
    private TextInputEditText editFullName, editPhoneNumber, editAddressDetail;
    private Button btnSaveAddress;
    private DatabaseHelper dbHelper;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        dbHelper = new DatabaseHelper(this);
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        currentUsername = sharedPref.getString("username", "");

        initViews();
        loadExistingData();

        btnBackAddress.setOnClickListener(v -> finish());

        btnSaveAddress.setOnClickListener(v -> {
            saveAddress();
        });
    }

    private void initViews() {
        btnBackAddress = findViewById(R.id.btnBackAddress);
        editFullName = findViewById(R.id.editFullName);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        editAddressDetail = findViewById(R.id.editAddressDetail);
        btnSaveAddress = findViewById(R.id.btnSaveAddress);
    }

    private void loadExistingData() {
        if (!currentUsername.isEmpty()) {
            User user = dbHelper.getUser(currentUsername);
            if (user != null) {
                // Giả sử username là họ tên nếu chưa có field riêng, hoặc lấy từ email
                editFullName.setText(user.getUsername());
                if (user.getPhone() != null) editPhoneNumber.setText(user.getPhone());
                if (user.getAddress() != null) editAddressDetail.setText(user.getAddress());
            }
        }
    }

    private void saveAddress() {
        String name = editFullName.getText().toString().trim();
        String phone = editPhoneNumber.getText().toString().trim();
        String address = editAddressDetail.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!currentUsername.isEmpty()) {
            User user = dbHelper.getUser(currentUsername);
            if (user != null) {
                user.setPhone(phone);
                user.setAddress(address);
                dbHelper.updateUser(user);
                Toast.makeText(this, "Đã lưu địa chỉ!", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy người dùng!", Toast.LENGTH_SHORT).show();
        }
    }
}
