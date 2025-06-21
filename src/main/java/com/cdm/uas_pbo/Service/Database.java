package com.cdm.uas_pbo.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin";

    private static Connection connection = null; // Default Value = Gagal

    public static Connection getConnection() { // Connect DB
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                System.err.println("Database connection failed!");
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection() { // Disconnect DB
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
