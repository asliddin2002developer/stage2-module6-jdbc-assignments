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

    private static final String createUserSQL = "INSERT INTO myusers(firstname, lastname, age) VALUES(?, ?, ?)  ";
    private static final String updateUserSQL = "UPDATE myusers SET firstname=?, lastname=?, age=? WHERE id=?";
    private static final String deleteUser = "DELETE FROM myusers WHERE id=?";
    private static final String findUserByIdSQL = "SELECT * FROM myusers WHERE id=?";
    private static final String findUserByNameSQL = "SELECT * FROM myusers WHERE name=?";
    private static final String findAllUserSQL = "SELECT * FROM myusers";

    public Long createUser(User user) {
        Long id = null;
        try(
                Connection connection = CustomDataSource.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement(createUserSQL, Statement.RETURN_GENERATED_KEYS);
        ){
            ps.setObject(1, user.getFirstName());
            ps.setObject(2, user.getLastName());
            ps.setObject(3, user.getAge());
            ps.executeUpdate(createUserSQL);

            ResultSet resultSet = ps.getGeneratedKeys();
            if (resultSet != null){
                System.out.println(resultSet.getString(2));
                id = resultSet.getLong(1);
            }
        }
        catch (SQLException e){
            System.out.println(e);
        }
        return id;
    }

    public User findUserById(Long userId) {
        User user = new User();

        try (
                Connection connection = CustomDataSource.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement(findUserByIdSQL);
        ){
            ps.setObject(1, userId);

            ResultSet resultSet = ps.executeQuery();
            if (!resultSet.next()) throw new RuntimeException();
            user.setId(resultSet.getLong("id"));
            user.setFirstName(resultSet.getString("firstname"));
            user.setLastName(resultSet.getString("lastname"));
            user.setAge(resultSet.getInt("age"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public User findUserByName(String userName) {
        User user = new User();

        try(
                Connection connection = CustomDataSource.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement(findUserByNameSQL);
        ){
            ps.setObject(1, userName);
            ResultSet resultSet = ps.executeQuery();
            if (!resultSet.next()) throw new SQLException("no user with this name");

            user.setId(resultSet.getLong("id"));
            user.setFirstName(resultSet.getString("firstname"));
            user.setLastName(resultSet.getString("lastname"));
            user.setAge(resultSet.getInt("age"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public List<User> findAllUser() {
        List<User> users = new ArrayList<>();

        try(
                Connection connection = CustomDataSource.getInstance().getConnection();
                Statement st = connection.createStatement();
        ) {
            ResultSet resultSet = st.executeQuery(findAllUserSQL);
            while (resultSet.next()){
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge(resultSet.getInt("age"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public User updateUser(User user) {
        try(
                Connection connection = CustomDataSource.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement(updateUserSQL);
        ) {
            ps.setObject(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getAge());
            ps.setLong(4, user.getId());

            if (ps.executeUpdate() == 0) throw new SQLException("no user");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    private void deleteUser(Long userId) {
        try (
                Connection connection = CustomDataSource.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement(deleteUser);
        ) {
            ps.setLong(1, userId);
            if (ps.executeUpdate() == 0) throw new SQLException("no user with this id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

