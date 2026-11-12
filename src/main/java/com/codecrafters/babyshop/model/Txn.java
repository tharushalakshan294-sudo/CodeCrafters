package com.codecrafters.babyshop.model;

public class Txn {
    public int id;
    public String date, desc, type;
    public double amount;

    public Txn(int id, String date, String desc, String type, double amount) {
        this.id = id; this.date = date; this.desc = desc; this.type = type; this.amount = amount;
    }
}
