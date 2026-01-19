package main;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        DatabaseConnection.createTable();
        DatabaseConnection.clearTable();
        DatabaseConnection.addEmployee("First", "Last", "Middle", "Position");
        DatabaseConnection.getAllEmployees();
        DatabaseConnection.addEmployee("First 1", " Last 1", "Middle 1", "Position 1");
        DatabaseConnection.getAllEmployees();
        DatabaseConnection.deleteEmployee(2);
        DatabaseConnection.getAllEmployees();
    }
}
