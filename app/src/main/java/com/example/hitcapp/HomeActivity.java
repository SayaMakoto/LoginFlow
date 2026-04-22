package com.example.hitcapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ListView lvBooks;
    ArrayList<Book> arrayBook;
    ArrayList<Book> filteredList;
    BookAdapter adapter;
    BottomNavigationView bottomNavigationView;
    EditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        lvBooks = findViewById(R.id.lvBooks);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        edtSearch = findViewById(R.id.edtSearch);

        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        arrayBook = new ArrayList<>();
        // Dữ liệu gốc - Đã cập nhật tham số Category (Văn học, Khoa học,...)
        arrayBook.add(new Book("Đắc Nhân Tâm", "Dale Carnegie", "Miễn phí", R.drawable.book1, "Văn học"));
        arrayBook.add(new Book("Nhà Giả Kim", "Paulo Coelho", "50.000đ", R.drawable.book2, "Văn học"));
        arrayBook.add(new Book("Tôi Thấy Hoa Vàng Trên Cỏ Xanh", "Nguyễn Nhật Ánh", "Miễn phí", R.drawable.book9, "Văn học"));
        arrayBook.add(new Book("Số Đỏ", "Vũ Trọng Phụng", "45.000đ", R.drawable.book4, "Văn học"));
        arrayBook.add(new Book("Mắt Biếc", "Nguyễn Nhật Ánh", "Miễn phí", R.drawable.book10, "Văn học"));
        arrayBook.add(new Book("Lược Sử Thời Gian", "Stephen Hawking", "120.000đ", R.drawable.book11, "Khoa học"));

        // filteredList sẽ chứa dữ liệu hiển thị trên ListView
        filteredList = new ArrayList<>(arrayBook);

        adapter = new BookAdapter(this, R.layout.item_book, filteredList);
        lvBooks.setAdapter(adapter);

        // Sự kiện click vào item sách
        lvBooks.setOnItemClickListener((parent, view, position, id) -> {
            Book selectedBook = filteredList.get(position);
            Intent intent = new Intent(HomeActivity.this, BookDetailActivity.class);
            intent.putExtra("title", selectedBook.getTitle());
            intent.putExtra("author", selectedBook.getAuthor());
            intent.putExtra("price", selectedBook.getPrice());
            intent.putExtra("category", selectedBook.getCategory());
            intent.putExtra("image", selectedBook.getImageResource());
            startActivity(intent);
        });

        // Lắng nghe sự kiện thay đổi text trong ô tìm kiếm
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                return true;
            } else if (id == R.id.navigation_books) {
                // CHUYỂN SANG TRANG SÁCH
                Intent intent = new Intent(HomeActivity.this, BookActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.navigation_cart) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.navigation_message) {
                return true;
            } else if (id == R.id.navigation_info) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    // Hàm lọc dữ liệu
    private void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            filteredList.addAll(arrayBook);
        } else {
            String filterPattern = text.toLowerCase().trim();
            for (Book item : arrayBook) {
                // Tìm theo tên sách hoặc tên tác giả
                if (item.getTitle().toLowerCase().contains(filterPattern) || 
                    item.getAuthor().toLowerCase().contains(filterPattern)) {
                    filteredList.add(item);
                }
            }
        }
        adapter.notifyDataSetChanged(); // Cập nhật lại ListView
    }
}
