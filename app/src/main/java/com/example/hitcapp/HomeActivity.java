package com.example.hitcapp;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ListView lvGames;
    ArrayList<Game> arrayGame;
    GameAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        lvGames = findViewById(R.id.lvGames);
        arrayGame = new ArrayList<>();

        // Thêm dữ liệu mẫu (Sử dụng icon mặc định vì chưa có ảnh thật)
        arrayGame.add(new Game("Free Fire", R.drawable.ic_launcher_foreground));
        arrayGame.add(new Game("PUBG Mobile", R.drawable.ic_launcher_foreground));
        arrayGame.add(new Game("Call of Duty", R.drawable.ic_launcher_foreground));
        arrayGame.add(new Game("Liên Quân Mobile", R.drawable.ic_launcher_foreground));
        arrayGame.add(new Game("Genshin Impact", R.drawable.ic_launcher_foreground));
        arrayGame.add(new Game("Minecraft", R.drawable.ic_launcher_foreground));

        adapter = new GameAdapter(this, R.layout.item_game, arrayGame);
        lvGames.setAdapter(adapter);
    }
}