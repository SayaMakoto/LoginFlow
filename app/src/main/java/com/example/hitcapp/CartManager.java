package com.example.hitcapp;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static List<CartItem> cartItems = new ArrayList<>();

    public static void addToCart(Book book) {
        for (CartItem item : cartItems) {
            if (item.getBook().getTitle().equals(book.getTitle())) {
                item.addQuantity(1);
                return;
            }
        }
        cartItems.add(new CartItem(book, 1));
    }

    public static void removeFromCart(String title) {
        cartItems.removeIf(item -> item.getBook().getTitle().equals(title));
    }

    public static void updateQuantity(String title, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getBook().getTitle().equals(title)) {
                item.setQuantity(Math.max(1, quantity));
                return;
            }
        }
    }

    public static List<CartItem> getCartItems() {
        return cartItems;
    }

    public static double getTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            String priceStr = item.getBook().getPrice().replace("đ", "").replace(".", "").replace("Miễn phí", "0").trim();
            try {
                double price = Double.parseDouble(priceStr);
                total += price * item.getQuantity();
            } catch (NumberFormatException e) {
                // Ignore or handle error
            }
        }
        return total;
    }
}
