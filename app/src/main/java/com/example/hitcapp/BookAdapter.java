package com.example.hitcapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class BookAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Book> bookList;

    public BookAdapter(Context context, int layout, List<Book> bookList) {
        this.context = context;
        this.layout = layout;
        this.bookList = bookList;
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
        }

        TextView txtTitle = convertView.findViewById(R.id.txtTitle);
        TextView txtAuthor = convertView.findViewById(R.id.txtAuthor);
        TextView txtPrice = convertView.findViewById(R.id.txtPrice);
        ImageView imgBook = convertView.findViewById(R.id.imgBook);

        Book book = bookList.get(position);
        txtTitle.setText(book.getTitle());
        txtAuthor.setText(book.getAuthor());
        txtPrice.setText(book.getPrice());
        imgBook.setImageResource(book.getImageResource());

        return convertView;
    }
}
