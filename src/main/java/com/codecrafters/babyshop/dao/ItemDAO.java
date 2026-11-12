package com.codecrafters.babyshop.dao;

import com.codecrafters.babyshop.db.DBConnection;
import com.codecrafters.babyshop.model.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    public List<Item> findAll() throws SQLException {
        List<Item> list = new ArrayList<>();
        try (Connection c = DBConnection.get();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM item ORDER BY code")) {
            while (rs.next()) {
                list.add(new Item(rs.getString("code"), rs.getString("name"), rs.getString("size"),
                        rs.getInt("qty"), rs.getDouble("price")));
            }
        }
        return list;
    }

    public void insert(Item i) throws SQLException {
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO item (code, name, size, qty, price) VALUES (?,?,?,?,?)")) {
            ps.setString(1, i.code);
            ps.setString(2, i.name);
            ps.setString(3, i.size);
            ps.setInt(4, i.qty);
            ps.setDouble(5, i.price);
            ps.executeUpdate();
        }
    }

    public void updateQty(String code, int qty) throws SQLException {
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement("UPDATE item SET qty=? WHERE code=?")) {
            ps.setInt(1, qty);
            ps.setString(2, code);
            ps.executeUpdate();
        }
    }

    public void delete(String code) throws SQLException {
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM item WHERE code=?")) {
            ps.setString(1, code);
            ps.executeUpdate();
        }
    }
}
