package com.example.hitcapp;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CartAdapter extends BaseAdapter {
    private CartActivity context;
    private int layout;
    private List<CartItem> cartItemList;

    public CartAdapter(CartActivity context, int layout, List<CartItem> cartItemList) {
        this.context = context;
        this.layout = layout;
        this.cartItemList = cartItemList;
    }

    @Override
    public int getCount() {
        return cartItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return cartItemList.get(position);
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

        CartItem item = cartItemList.get(position);

        ImageView imgBook = convertView.findViewById(R.id.imgCartBook);
        TextView txtTitle = convertView.findViewById(R.id.txtCartTitle);
        TextView txtPrice = convertView.findViewById(R.id.txtCartPrice);
        EditText edtQuantity = convertView.findViewById(R.id.edtCartQuantity);
        Button btnMinus = convertView.findViewById(R.id.btnMinus);
        Button btnPlus = convertView.findViewById(R.id.btnPlus);
        ImageButton btnRemove = convertView.findViewById(R.id.btnRemove);

        imgBook.setImageResource(item.getBook().getImageResource());
        txtTitle.setText(item.getBook().getTitle());
        txtPrice.setText(item.getBook().getPrice());
        
        // Tránh vòng lặp vô tận khi setText kích hoạt TextWatcher
        edtQuantity.setTag(position); 
        edtQuantity.setText(String.valueOf(item.getQuantity()));

        btnPlus.setOnClickListener(v -> {
            CartManager.updateQuantity(item.getBook().getTitle(), item.getQuantity() + 1);
            context.updateCart();
        });

        btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                CartManager.updateQuantity(item.getBook().getTitle(), item.getQuantity() - 1);
                context.updateCart();
            }
        });

        btnRemove.setOnClickListener(v -> {
            CartManager.removeFromCart(item.getBook().getTitle());
            context.updateCart();
        });

        edtQuantity.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String input = edtQuantity.getText().toString();
                if (!input.isEmpty()) {
                    int newQty = Integer.parseInt(input);
                    CartManager.updateQuantity(item.getBook().getTitle(), newQty);
                    context.updateCart();
                } else {
                    edtQuantity.setText(String.valueOf(item.getQuantity()));
                }
            }
        });

        return convertView;
    }
}
