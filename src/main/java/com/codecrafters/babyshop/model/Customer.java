package com.codecrafters.babyshop.model;
public class Customer extends Person {
 private final String customerType;
 public Customer(String name,String phone,String customerType){super(name,phone);this.customerType=customerType==null?"Retail":customerType;}
 public String getCustomerType(){return customerType;} @Override public String getRole(){return "Customer";}
}
