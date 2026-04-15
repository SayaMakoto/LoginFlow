package com.example.hitcapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class GameAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Game> gameList;

    public GameAdapter(Context context, int layout, List<Game> gameList) {
        this.context = context;
        this.layout = layout;
        this.gameList = gameList;
    }

    @Override
    public int getCount() {
        return gameList.size();
    }

    @Override
    public Object getItem(int position) {
        return gameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, null);

        TextView txtName = convertView.findViewById(R.id.txtName);
        ImageView imgGame = convertView.findViewById(R.id.imgGame);

        Game game = gameList.get(position);
        txtName.setText(game.getName());
        imgGame.setImageResource(game.getImageResource());

        return convertView;
    }
}