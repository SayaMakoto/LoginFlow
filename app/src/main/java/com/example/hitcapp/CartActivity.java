package com.example.hitcapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class CartActivity extends AppCompatActivity {

    private ListView lvCart;
    private TextView txtTotalPrice;
    private ImageButton btnBackCart;
    private Button btnCheckout;
    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initViews();
        setupCart();
        
        btnBackCart.setOnClickListener(v -> finish());
        
        btnCheckout.setOnClickListener(v -> {
            if (CartManager.getCartItems().isEmpty()) {
                Toast.makeText(this, "Giỏ hàng của bạn đang trống!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                CartManager.getCartItems().clear();
                updateCart();
            }
        });
    }

    private void initViews() {
        lvCart = findViewById(R.id.lvCart);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        btnBackCart = findViewById(R.id.btnBackCart);
        btnCheckout = findViewById(R.id.btnCheckout);
    }

    private void setupCart() {
        adapter = new CartAdapter(this, R.layout.item_cart, CartManager.getCartItems());
        lvCart.setAdapter(adapter);
        updateCart();
    }

    public void updateCart() {
        adapter.notifyDataSetChanged();
        double total = CartManager.getTotalPrice();
        DecimalFormat formatter = new DecimalFormat("#,###");
        txtTotalPrice.setText(formatter.format(total) + "đ");
    }
}
