import java.sql.*;
import java.util.*;

public class Database {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/students?useSSL=false";
    private static final String DB_USERNAME = "username";
    private static final String DB_PASSWORD = "Welkom01";

    private static Connection connection;

    public static List<Map<String, Object>> executeQuery(String query) {

        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> row;

        try {
            Connection conn = getConnection();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            ResultSetMetaData metaData = resultSet.getMetaData();
            Integer columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                row = new HashMap<>();

                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), resultSet.getObject(i));
                }

                result.add(row);
            }

            resultSet.close();

        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            closeConnection();
        }

        return result;
    }

    public static int executeUpdate(String query) {
        int result = 0;
        try {
            Connection conn = getConnection();
            Statement statement = conn.createStatement();
            result = statement.executeUpdate(query);

        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            closeConnection();
        }

        return result;
    }

    public static int executePrepared(String query, Object... arguments) {
        int result = 0;
        
        try {
            Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(query);

            for (int i = 0; i < arguments.length; i++) {
                statement.setObject(i + 1, arguments[i]);
            }

            result = statement.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            closeConnection();
        }

        return result;
    }

    private static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        }

        connection.setAutoCommit(true);

        return connection;
    }

    private static void closeConnection() {
        if (connection == null) {
            return;
        }

        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
}
