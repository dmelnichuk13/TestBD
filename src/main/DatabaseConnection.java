package main;

import java.net.ConnectException;
import java.sql.*;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:employees.db";

    public static Connection connect(){
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(URL);
            System.out.println("Connection to SQLite has been established");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return connection;
    }

    public static void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS employees (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "first_name TEXT NOT NULL, " +
                "last_name TEXT NOT NULL, " +
                "middle_name TEXT, " +
                "position TEXT NOT NULL);";
        try (Connection connection = connect();
             Statement statement = connection.createStatement()){
            statement.executeUpdate(createTableSQL);
            System.out.println("Таблица сотрудников успешно создана.");
        } catch (SQLException e){
            System.out.println("Ошибка при создании таблицы: " + e.getMessage());
        }
    }

    public static void addEmployee(String firstName, String lastName, String middleName, String position) {
        String insertSQL = "INSERT INTO employees (first_name, last_name, Middle_name, position) VALUES (?, ?, ?, ?)";
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)){
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, middleName);
            preparedStatement.setString(4, position);
            preparedStatement.executeUpdate();
            System.out.println("Сотрудник добавлен");
        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении сотрудника " + e.getMessage());
        }
    }

    public static void deleteEmployee(int id) {
        String deleteSQL = "DELETE FROM employees WHERE id = ?";
        try (Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)){
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0){
                System.out.println("Сотрудник удален");
            } else {
                System.out.println("Сотрудник с таким id  не найден");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении сотрудника: " + e.getMessage());
        }
    }

    public static void getAllEmployees() {
        String selectSQL = "SELECT * FROM employees";
        try (Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)){
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String middleName = resultSet.getString("middle_name");
                String position = resultSet.getString("position");
                System.out.println("ID: " + id + ", First name: " + firstName +
                        ", Last name: " + lastName + ", Middle Name: " +
                        middleName + ", Position: " + position);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при получении списка сотрудников: " + e.getMessage());
        }
    }

    public static void clearTable() {
        String deleteSQL = "DELETE FROM employees";
        String resetAutoIncrementSQL = "DELETE FROM sqlite_sequence WHERE name='employees'";
        try (Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            PreparedStatement resetStatement = connection.prepareStatement(resetAutoIncrementSQL)) {
            preparedStatement.executeUpdate();
            System.out.println("Таблица очищена");
            resetStatement.executeUpdate();
            System.out.println("Автоинкремент сброшен");
        } catch (SQLException e) {
            System.out.println(" Ошибка при очистке таблицы: " + e.getMessage());
        }
    }
}
