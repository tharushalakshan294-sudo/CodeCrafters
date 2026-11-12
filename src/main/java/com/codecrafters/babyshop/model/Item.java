package com.codecrafters.babyshop.model;

public class Item {
    public String code, name, size;
    public int qty;
    public double price;

    public Item(String code, String name, String size, int qty, double price) {
        this.code = code; this.name = name; this.size = size; this.qty = qty; this.price = price;
    }
}
