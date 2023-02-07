package jdbc;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository {

    private Connection connection = null;
    private PreparedStatement ps = null;
    private Statement st = null;

    private static final String createUserSQL = "INSERT INTO myusers VALUES(1, 'Asliddin', 'Eshonkulov', 21)";
    private static final String updateUserSQL = "";
    private static final String deleteUser = "";
    private static final String findUserByIdSQL = "";
    private static final String findUserByNameSQL = "";
    private static final String findAllUserSQL = "";


    public Long createUser() {
        connection = new CustomConnector().getConnection("jdbc:postgresql://localhost:5432/myfirstdb", "postgres", "postgres");
        try {
            st = connection.createStatement();
            st.executeUpdate(createUserSQL);
        }
        catch (SQLException e){
            System.out.println(e);
        }
        return 13L;
    }

    public User findUserById(Long userId) {
        connection = new CustomConnector().getConnection("jdbc:postgresql://localhost:5432/myfirstdb", "postgres", "postgres");
        User user = new User();
        try {
            ps = connection.prepareStatement(String.format("SELECT * FROM myusers WHERE id = %s", userId));
//            ps.setString(1, String.valueOf(userId));
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()){
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                int age = resultSet.getInt("age");

                user.setId(id);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setAge(age);
                System.out.println(user.getFirstName());
            }
        }
        catch (SQLException e){
            System.out.println(e);
        }
        return user;
    }

    public User findUserByName(String userName) {
        connection = new CustomConnector().getConnection("jdbc:postgresql://localhost:5432/myfirstdb", "postgres", "postgres");
        User user = new User();
        try {
            ps = connection.prepareStatement(String.format("SELECT * FROM myusers WHERE username = %s", userName));
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()){
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                int age = resultSet.getInt("age");

                user.setId(id);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setAge(age);
            }
        }
        catch (SQLException e){
            System.out.println(e);
        }
        return user;
    }

    public List<User> findAllUser() {
        connection = new CustomConnector().getConnection("jdbc:postgresql://localhost:5432/myfirstdb", "postgres", "postgres");
        List<User> ans = new ArrayList<>();
        try {
            ps = connection.prepareStatement("SELECT * FROM myusers");
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()){
                User user = new User();
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                int age = resultSet.getInt("age");

                user.setId(id);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setAge(age);

                ans.add(user);
            }
        }
        catch (SQLException e){
            System.out.println(e);
        }
        return ans;
    }

    public User updateUser() {
        connection = new CustomConnector().getConnection("jdbc:postgresql://localhost:5432/myfirstdb", "postgres", "postgres");
        User user = new User();
        try {
            ps = connection.prepareStatement("UPDATE myusers SET age = 20");
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()){
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                int age = resultSet.getInt("age");

                user.setId(id);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setAge(age);
            }
        }
        catch (SQLException e){
            System.out.println(e);
        }
        return user;
    }

    private void deleteUser(Long userId) {
        connection = new CustomConnector().getConnection("jdbc:postgresql://localhost:5432/myfirstdb", "postgres", "postgres");
        User user = new User();

        try {
            st = connection.createStatement();
            st.executeUpdate(String.format("DELETE FROM myusers WHERE id = %s", userId));
        }
        catch (SQLException e){
            System.out.println(e);
        }
    }
}
