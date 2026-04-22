package com.example.hitcapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BookDetailActivity extends AppCompatActivity {

    ImageView imgMainBook, imgDetailBook;
    TextView txtTitle, txtAuthor, txtPrice, txtCategory;
    ImageButton btnBack;
    Button btnAddToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        initViews();

        // Nhận dữ liệu từ Intent
        String title = getIntent().getStringExtra("title");
        String author = getIntent().getStringExtra("author");
        String price = getIntent().getStringExtra("price");
        String category = getIntent().getStringExtra("category");
        int image = getIntent().getIntExtra("image", R.drawable.book1);

        // Hiển thị dữ liệu
        txtTitle.setText(title);
        txtAuthor.setText("Tác giả: " + author);
        txtPrice.setText(price);
        txtCategory.setText(category);
        imgMainBook.setImageResource(image);
        imgDetailBook.setImageResource(image);

        btnBack.setOnClickListener(v -> finish());

        btnAddToCart.setOnClickListener(v -> {
            Book book = new Book(title, author, price, image, category);
            CartManager.addToCart(book);
            Toast.makeText(this, "Đã thêm " + title + " vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        });
    }

    private void initViews() {
        imgMainBook = findViewById(R.id.imgMainBook);
        imgDetailBook = findViewById(R.id.imgDetailBook);
        txtTitle = findViewById(R.id.txtDetailTitle);
        txtAuthor = findViewById(R.id.txtDetailAuthor);
        txtPrice = findViewById(R.id.txtDetailPrice);
        txtCategory = findViewById(R.id.txtDetailCategory);
        btnBack = findViewById(R.id.btnBack);
        btnAddToCart = findViewById(R.id.btnAddToCart);
    }
}
