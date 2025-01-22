package utils;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseClient {

    // Параметры SSH-соединения
    private static final String SSH_HOST = "10.208.19.142";  // Хост SSH-сервера
    private static final String SSH_USER = "ezarubina";    // Имя пользователя SSH
    private static final String SSH_KEY_PATH = "D:/SoftWareCats/PROJECTS/SINKER/keys/priv.pem";
    // Путь к приватному ключу
    private static final int SSH_PORT = 22;  // Порт SSH, обычно 22

    // Параметры подключения к базе данных
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/sync_db";
    private static final String DB_USER = "sync";
    private static final String DB_PASSWORD = "changeme";

    // Локальный порт, на который будет проброшено соединение
    private static final int LOCAL_PORT = 5432;

    private static Session session;
    private static Connection connection;

    /**
     * Method to establish SSH connection and database connection.
     *
     * @throws SQLException If any error occurs while connecting.
     */
    public static void establishConnection() throws SQLException {
        try {
            // Установка SSH-туннеля
            JSch jsch = new JSch();
            jsch.addIdentity(SSH_KEY_PATH); // Использование приватного ключа для аутентификации

            session = jsch.getSession(SSH_USER, SSH_HOST, SSH_PORT);
            session.setConfig("StrictHostKeyChecking", "no"); // Отключаем проверку хоста
            session.connect();

            // Проброс порта (локальный порт -> удаленный порт на сервере базы данных)
            session.setPortForwardingL(LOCAL_PORT, "localhost", 5432);

            // Подключение к базе данных через локальный порт
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:" + LOCAL_PORT + "/sync_db", DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            throw new SQLException("Error while establishing SSH tunnel", e);
        }
    }

    /**
     * Method to close the SSH and database connection.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        } catch (SQLException e) {
            System.err.println("Error while closing the connection: " + e.getMessage());
        }
    }

    /**
     * Execute SQL query method
     *
     * @param sqlQuery SQL query to be executed
     * @throws SQLException
     */
    public static void executeQuery(String sqlQuery) throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("Connection is not established.");
        }

        try (Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate(sqlQuery);
            System.out.println("SQL query execution successful. Rows inserted: " + rowsAffected);
        } catch (SQLException e) {
            System.err.println("SQL query execution error: " + e.getMessage());
            throw e;
        }
    }
}


