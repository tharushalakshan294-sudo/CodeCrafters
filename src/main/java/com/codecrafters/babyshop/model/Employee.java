package com.codecrafters.babyshop.model;
public class Employee extends Person {
 private final int id; private final String role; private final double salary; private boolean paid;
 public Employee(int id,String name,String role,double salary,boolean paid){this(id,name,role,salary,paid,"");}
 public Employee(int id,String name,String role,double salary,boolean paid,String phone){super(name,phone);if(salary<0)throw new IllegalArgumentException("Salary cannot be negative.");this.id=id;this.role=role==null?"":role;this.salary=salary;this.paid=paid;}
 public int getId(){return id;} public String getEmployeeRole(){return role;} public double getSalary(){return salary;} public boolean isPaid(){return paid;} public void setPaid(boolean paid){this.paid=paid;}
 @Override public String getRole(){return "Employee";}
}
