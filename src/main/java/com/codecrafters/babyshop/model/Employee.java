package com.codecrafters.babyshop.model;

public class Employee {
    public int id;
    public String name, role;
    public double salary;
    public boolean paid;

    public Employee(int id, String name, String role, double salary, boolean paid) {
        this.id = id; this.name = name; this.role = role; this.salary = salary; this.paid = paid;
    }
}
