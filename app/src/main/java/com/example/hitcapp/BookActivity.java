package com.example.hitcapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {

    ListView lvAllBooks;
    ArrayList<Book> allBooks;
    ArrayList<Book> filteredBooks;
    ArrayList<Book> currentPageBooks;
    BookAdapter adapter;
    BottomNavigationView bottomNavigationView;

    ImageButton btnPrev, btnNext;
    TextView txtPageNumber;
    
    // Khai báo các TextView thể loại để xử lý focus
    TextView catAll, catKinhDi, catTrinhTham, catKinhTe, catVanHoc;
    ArrayList<TextView> categoryViews;

    int currentPage = 0;
    final int ITEMS_PER_PAGE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        initViews();
        setupData();
        
        // Mặc định focus vào "Tất cả" lúc đầu
        updateCategoryUI(catAll);
        filterByCategory("Tất cả");
        
        setupBottomNav();

        btnNext.setOnClickListener(v -> {
            if ((currentPage + 1) * ITEMS_PER_PAGE < filteredBooks.size()) {
                currentPage++;
                updatePage();
            }
        });

        btnPrev.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--;
                updatePage();
            }
        });
    }

    private void initViews() {
        lvAllBooks = findViewById(R.id.lvAllBooks);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        txtPageNumber = findViewById(R.id.txtPageNumber);
        
        // Ánh xạ các nút thể loại
        catAll = findViewById(R.id.cat_all);
        catKinhDi = findViewById(R.id.cat_kinhdi);
        catTrinhTham = findViewById(R.id.cat_trinhtham);
        catKinhTe = findViewById(R.id.cat_kinhte);
        catVanHoc = findViewById(R.id.cat_vanhoc);
        
        categoryViews = new ArrayList<>();
        categoryViews.add(catAll);
        categoryViews.add(catKinhDi);
        categoryViews.add(catTrinhTham);
        categoryViews.add(catKinhTe);
        categoryViews.add(catVanHoc);

        currentPageBooks = new ArrayList<>();
        filteredBooks = new ArrayList<>();
    }

    private void setupData() {
        allBooks = new ArrayList<>();
        allBooks.add(new Book("Đắc Nhân Tâm", "Dale Carnegie", "Miễn phí", R.drawable.book1, "Văn học"));
        allBooks.add(new Book("Nhà Giả Kim", "Paulo Coelho", "50.000đ", R.drawable.book2, "Văn học"));
        allBooks.add(new Book("Sự im lặng của bầy cừu", "Thomas Harris", "90.000đ", R.drawable.book3, "Trinh thám"));
        allBooks.add(new Book("Số Đỏ", "Vũ Trọng Phụng", "45.000đ", R.drawable.book4, "Văn học"));
        allBooks.add(new Book("IT - Gã hề ma quái", "Stephen King", "120.000đ", R.drawable.book5, "Kinh dị"));
        allBooks.add(new Book("Kinh tế học cơ bản", "Thomas Sowell", "200.000đ", R.drawable.book6, "Kinh tế"));
        allBooks.add(new Book("Án mạng trên chuyến tàu", "Agatha Christie", "75.000đ", R.drawable.book7, "Trinh thám"));
        allBooks.add(new Book("Lời nguyền", "R.L. Stine", "Miễn phí", R.drawable.book8, "Kinh dị"));
        
        adapter = new BookAdapter(this, R.layout.item_book, currentPageBooks);
        lvAllBooks.setAdapter(adapter);

        // Thêm sự kiện click vào item sách để xem chi tiết
        lvAllBooks.setOnItemClickListener((parent, view, position, id) -> {
            Book selectedBook = currentPageBooks.get(position);
            Intent intent = new Intent(BookActivity.this, BookDetailActivity.class);
            intent.putExtra("title", selectedBook.getTitle());
            intent.putExtra("author", selectedBook.getAuthor());
            intent.putExtra("price", selectedBook.getPrice());
            intent.putExtra("category", selectedBook.getCategory());
            intent.putExtra("image", selectedBook.getImageResource());
            startActivity(intent);
        });
    }

    private void filterByCategory(String category) {
        filteredBooks.clear();
        if (category.equals("Tất cả")) {
            filteredBooks.addAll(allBooks);
        } else {
            for (Book book : allBooks) {
                if (book.getCategory().equals(category)) {
                    filteredBooks.add(book);
                }
            }
        }
        currentPage = 0;
        updatePage();
    }

    private void updatePage() {
        int start = currentPage * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, filteredBooks.size());

        currentPageBooks.clear();
        if (!filteredBooks.isEmpty()) {
            for (int i = start; i < end; i++) {
                currentPageBooks.add(filteredBooks.get(i));
            }
        }
        adapter.notifyDataSetChanged();

        txtPageNumber.setText("Trang " + (currentPage + 1));
        
        btnPrev.setEnabled(currentPage > 0);
        btnNext.setEnabled((currentPage + 1) * ITEMS_PER_PAGE < filteredBooks.size());
        
        btnPrev.setAlpha(btnPrev.isEnabled() ? 1.0f : 0.3f);
        btnNext.setAlpha(btnNext.isEnabled() ? 1.0f : 0.3f);
    }

    // Hàm xử lý UI khi chọn thể loại (Focus)
    private void updateCategoryUI(TextView selectedView) {
        for (TextView tv : categoryViews) {
            if (tv == selectedView) {
                // Nút được chọn: Nền xanh, chữ trắng
                tv.setBackgroundResource(R.drawable.bg_category);
                tv.getBackground().setTint(Color.parseColor("#2196F3"));
                tv.setTextColor(Color.WHITE);
            } else {
                // Nút không chọn: Nền trắng, chữ xám
                tv.setBackgroundResource(R.drawable.bg_category);
                tv.getBackground().setTintList(null); // Reset tint
                tv.setTextColor(Color.parseColor("#616161"));
            }
        }
    }

    public void onCategoryClick(View view) {
        TextView tv = (TextView) view;
        updateCategoryUI(tv); // Cập nhật hiệu ứng focus
        filterByCategory(tv.getText().toString());
    }

    private void setupBottomNav() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_books);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (id == R.id.navigation_books) {
                return true;
            } else if (id == R.id.navigation_cart) {
                Intent intent = new Intent(BookActivity.this, CartActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.navigation_info) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
}
