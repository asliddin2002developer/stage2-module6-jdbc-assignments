package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CustomConnector {
    public Connection getConnection(String url) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);

        }
        catch(SQLException e){
            System.out.println(e);
        }
        return conn;
    }

    public Connection getConnection(String url, String user, String password)  {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
        }
        catch (SQLException e){
            System.out.println(e);
        }

        return conn;
    }
}
