package com.example.hitcapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    private ImageButton btnBackPayment;
    private LinearLayout layoutAddress, layoutProductItems;
    private TextView txtUserAddress, txtUserPhone, txtSubtotal, txtFinalTotal, txtFooterTotal;
    private Button btnPlaceOrder;
    private DatabaseHelper dbHelper;
    private String currentUsername;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        dbHelper = new DatabaseHelper(this);
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        currentUsername = sharedPref.getString("username", "");

        initViews();
        loadUserData();
        displayOrderItems();
        calculateTotal();

        btnBackPayment.setOnClickListener(v -> finish());

        layoutAddress.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentActivity.this, AddressActivity.class);
            startActivity(intent);
        });

        btnPlaceOrder.setOnClickListener(v -> {
            if (currentUser == null || currentUser.getAddress() == null || currentUser.getAddress().isEmpty()) {
                Toast.makeText(this, "Vui lòng thêm địa chỉ giao hàng!", Toast.LENGTH_SHORT).show();
                return;
            }
            // Xử lý đặt hàng thành công
            Toast.makeText(this, "Đặt hàng thành công! Cảm ơn bạn.", Toast.LENGTH_LONG).show();
            CartManager.getCartItems().clear();
            Intent intent = new Intent(PaymentActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData(); // Refresh address when returning from AddressActivity
    }

    private void initViews() {
        btnBackPayment = findViewById(R.id.btnBackPayment);
        layoutAddress = findViewById(R.id.layoutAddress);
        layoutProductItems = findViewById(R.id.layoutProductItems);
        txtUserAddress = findViewById(R.id.txtUserAddress);
        txtUserPhone = findViewById(R.id.txtUserPhone);
        txtSubtotal = findViewById(R.id.txtSubtotal);
        txtFinalTotal = findViewById(R.id.txtFinalTotal);
        txtFooterTotal = findViewById(R.id.txtFooterTotal);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
    }

    private void loadUserData() {
        if (!currentUsername.isEmpty()) {
            currentUser = dbHelper.getUser(currentUsername);
            if (currentUser != null && currentUser.getAddress() != null && !currentUser.getAddress().isEmpty()) {
                txtUserAddress.setText(currentUser.getAddress());
                txtUserPhone.setText(currentUser.getPhone() != null ? currentUser.getPhone() : "");
                txtUserAddress.setTextColor(getResources().getColor(android.R.color.black));
            } else {
                txtUserAddress.setText("Vui lòng thêm địa chỉ giao hàng");
                txtUserAddress.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                txtUserPhone.setText("");
            }
        }
    }

    private void displayOrderItems() {
        layoutProductItems.removeAllViews();
        List<CartItem> items = CartManager.getCartItems();
        LayoutInflater inflater = LayoutInflater.from(this);
        DecimalFormat formatter = new DecimalFormat("#,###");

        for (CartItem item : items) {
            View itemView = inflater.inflate(R.layout.item_cart, layoutProductItems, false);
            
            // Tùy chỉnh view cho phù hợp với trang payment (ẩn các nút +/- và remove)
            ImageView img = itemView.findViewById(R.id.imgCartBook);
            TextView title = itemView.findViewById(R.id.txtCartTitle);
            TextView price = itemView.findViewById(R.id.txtCartPrice);
            EditText quantity = itemView.findViewById(R.id.edtCartQuantity);
            View btnMinus = itemView.findViewById(R.id.btnMinus);
            View btnPlus = itemView.findViewById(R.id.btnPlus);
            View btnRemove = itemView.findViewById(R.id.btnRemove);
            
            // Ẩn các nút điều chỉnh số lượng và xóa trong trang thanh toán
            btnMinus.setVisibility(View.GONE);
            btnPlus.setVisibility(View.GONE);
            btnRemove.setVisibility(View.GONE);
            quantity.setEnabled(false); // Không cho sửa số lượng ở đây

            img.setImageResource(item.getBook().getImageResource());
            title.setText(item.getBook().getTitle());
            price.setText(item.getBook().getPrice());
            quantity.setText(String.valueOf(item.getQuantity()));

            layoutProductItems.addView(itemView);
        }
    }

    private void calculateTotal() {
        double subtotal = CartManager.getTotalPrice();
        double shipping = 15000;
        double total = subtotal + shipping;

        DecimalFormat formatter = new DecimalFormat("#,###");
        txtSubtotal.setText(formatter.format(subtotal) + "đ");
        txtFinalTotal.setText(formatter.format(total) + "đ");
        txtFooterTotal.setText(formatter.format(total) + "đ");
    }
}
