package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CustomConnector {
    public Connection getConnection(String url) {
        try {
            return DriverManager.getConnection(url);

        }
        catch(SQLException e){
            System.out.println(e);
        }
        return null;
    }

    public Connection getConnection(String url, String user, String password)  {
        try {
            return DriverManager.getConnection(url, user, password);
        }
        catch (SQLException e){
            System.out.println(e);
        }
        return null;
    }
}
