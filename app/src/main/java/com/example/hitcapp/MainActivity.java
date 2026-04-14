package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    TextInputEditText username, email, password;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Ánh xạ
        username = findViewById(R.id.input_username);
        email = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);
        btnLogin = findViewById(R.id.btnlogin);

        btnLogin.setOnClickListener(v -> {

            String user = username.getText().toString().trim();
            String mail = email.getText().toString().trim();
            String pass = password.getText().toString().trim();

            // Check rỗng
            if(user.isEmpty() || mail.isEmpty() || pass.isEmpty()){
                Toast.makeText(this, "Vui lòng nhập đầy đủ!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Dữ liệu đúng (hardcode)
            if(user.equals("admin") &&
                    mail.equals("admin@gmail.com") &&
                    pass.equals("123456")){

                Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(this, "Sai thông tin đăng nhập!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}