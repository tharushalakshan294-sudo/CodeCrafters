package com.codecrafters.babyshop.model;
public abstract class Person {
 private final String name, phone;
 protected Person(String name,String phone){if(name==null||name.isBlank())throw new IllegalArgumentException("Name is required.");this.name=name.trim();this.phone=phone==null?"":phone.trim();}
 public String getName(){return name;} public String getPhone(){return phone;} public abstract String getRole();
}
