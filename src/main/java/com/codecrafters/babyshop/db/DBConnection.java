package com.codecrafters.babyshop.db;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBConnection {

    private static final Properties props = new Properties();

    static {
        try (InputStream in = DBConnection.class.getResourceAsStream("/db.properties")) {
            props.load(in);
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Cannot load db.properties: " + e);
        }
    }

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password"));
    }

    /** Creates the tables on first run. */
    public static void init() throws SQLException {
        try (Connection c = get(); Statement st = c.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS item ("
                    + "code VARCHAR(20) PRIMARY KEY, name VARCHAR(100) NOT NULL, size VARCHAR(20), "
                    + "qty INT NOT NULL DEFAULT 0, price DECIMAL(10,2) NOT NULL)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS transactions ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, date DATE NOT NULL, "
                    + "description VARCHAR(200) NOT NULL, type VARCHAR(10) NOT NULL, amount DECIMAL(10,2) NOT NULL)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS employee ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100) NOT NULL, role VARCHAR(50), "
                    + "salary DECIMAL(10,2) NOT NULL, paid BOOLEAN NOT NULL DEFAULT FALSE)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
                    + "user_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "username VARCHAR(50) NOT NULL UNIQUE, password VARCHAR(255) NOT NULL)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS user_details ("
                    + "detail_id INT AUTO_INCREMENT PRIMARY KEY, user_id INT NOT NULL, "
                    + "full_name VARCHAR(100), email VARCHAR(100), phone VARCHAR(15), address VARCHAR(255), "
                    + "CONSTRAINT fk_user_details FOREIGN KEY (user_id) REFERENCES users(user_id) "
                    + "ON DELETE CASCADE ON UPDATE CASCADE)");
        }
    }
}
