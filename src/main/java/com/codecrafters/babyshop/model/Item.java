package com.codecrafters.babyshop.model;
public class Item {
 private final String code,name,size; private int qty; private final double price;
 public Item(String code,String name,String size,int qty,double price){if(code==null||code.isBlank()||name==null||name.isBlank())throw new IllegalArgumentException("Item code and name are required.");if(qty<0||price<0)throw new IllegalArgumentException("Quantity and price cannot be negative.");this.code=code.trim();this.name=name.trim();this.size=size==null?"":size.trim();this.qty=qty;this.price=price;}
 public String getCode(){return code;} public String getName(){return name;} public String getSize(){return size;} public int getQty(){return qty;} public double getPrice(){return price;}
 public void setQty(int qty){if(qty<0)throw new IllegalArgumentException("Quantity cannot be negative.");this.qty=qty;}
}
