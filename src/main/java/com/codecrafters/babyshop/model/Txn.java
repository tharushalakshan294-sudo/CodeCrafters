package com.codecrafters.babyshop.model;
public class Txn {
 private final int id; private final String date,desc,type; private final double amount;
 public Txn(int id,String date,String desc,String type,double amount){if(amount<0)throw new IllegalArgumentException("Amount cannot be negative.");this.id=id;this.date=date;this.desc=desc;this.type=type;this.amount=amount;}
 public int getId(){return id;} public String getDate(){return date;} public String getDesc(){return desc;} public String getType(){return type;} public double getAmount(){return amount;}
}
