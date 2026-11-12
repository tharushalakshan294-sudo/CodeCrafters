package com.codecrafters.babyshop.dao;

import com.codecrafters.babyshop.db.DBConnection;
import com.codecrafters.babyshop.model.Txn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public List<Txn> findAll() throws SQLException {
        List<Txn> list = new ArrayList<>();
        try (Connection c = DBConnection.get();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM transactions ORDER BY date DESC, id DESC")) {
            while (rs.next()) {
                list.add(new Txn(rs.getInt("id"), rs.getDate("date").toString(),
                        rs.getString("description"), rs.getString("type"), rs.getDouble("amount")));
            }
        }
        return list;
    }

    /** Inserts the transaction and returns it with its generated id. */
    public Txn insert(String desc, String type, double amount) throws SQLException {
        Date today = new Date(System.currentTimeMillis());
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO transactions (date, description, type, amount) VALUES (?,?,?,?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, today);
            ps.setString(2, desc);
            ps.setString(3, type);
            ps.setDouble(4, amount);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return new Txn(keys.getInt(1), today.toString(), desc, type, amount);
            }
        }
    }
}
