package com.codecrafters.babyshop.dao;

import com.codecrafters.babyshop.db.DBConnection;
import com.codecrafters.babyshop.model.Employee;
import com.codecrafters.babyshop.model.Txn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public List<Employee> findAll() throws SQLException {
        List<Employee> list = new ArrayList<>();
        try (Connection c = DBConnection.get();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM employee ORDER BY name")) {
            while (rs.next()) {
                list.add(new Employee(rs.getInt("id"), rs.getString("name"), rs.getString("role"),
                        rs.getDouble("salary"), rs.getBoolean("paid")));
            }
        }
        return list;
    }

    public Employee insert(String name, String role, double salary) throws SQLException {
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO employee (name, role, salary, paid) VALUES (?,?,?,FALSE)",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, role);
            ps.setDouble(3, salary);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return new Employee(keys.getInt(1), name, role, salary, false);
            }
        }
    }

    /**
     * Marks the salary as paid AND records the expense in one database transaction,
     * so either both happen or neither does.
     */
    public Txn paySalary(Employee e) throws SQLException {
        Date today = new Date(System.currentTimeMillis());
        try (Connection c = DBConnection.get()) {
            c.setAutoCommit(false);
            try {
                try (PreparedStatement ps = c.prepareStatement("UPDATE employee SET paid=TRUE WHERE id=?")) {
                    ps.setInt(1, e.id);
                    ps.executeUpdate();
                }
                int txnId;
                String desc = "Salary - " + e.name;
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO transactions (date, description, type, amount) VALUES (?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setDate(1, today);
                    ps.setString(2, desc);
                    ps.setString(3, "Expense");
                    ps.setDouble(4, e.salary);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        keys.next();
                        txnId = keys.getInt(1);
                    }
                }
                c.commit();
                return new Txn(txnId, today.toString(), desc, "Expense", e.salary);
            } catch (SQLException ex) {
                c.rollback();
                throw ex;
            }
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM employee WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
