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
@NoArgsConstructor
public class SimpleJDBCRepository {

    private static final String CREATE_USER_SQL = "INSERT INTO myusers(firstname, lastname, age) VALUES(?, ?, ?)  ";
    private static final String UPDATE_USER_SQL = "UPDATE myusers SET firstname=?, lastname=?, age=? WHERE id=?";
    private static final String DELETE_USER = "DELETE FROM myusers WHERE id=?";
    private static final String FIND_USER_BY_ID_SQL = "SELECT * FROM myusers WHERE id=?";
    private static final String FIND_USER_BY_NAME_SQL = "SELECT * FROM myusers WHERE firstname=?";
    private static final String FIND_ALL_USER_SQL = "SELECT * FROM myusers";

    public static void main(String[] args) {
        User user = new User();
        user.setId(2L);
        user.setFirstName("Test");
        user.setLastName("Testov");
        user.setAge(23);

        SimpleJDBCRepository db = new SimpleJDBCRepository();
        System.out.println(db.createUser(user));
    }
    public Long createUser(User user) {
        Long id = null;
        try(
                Connection connection = CustomDataSource.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement(CREATE_USER_SQL, Statement.RETURN_GENERATED_KEYS)
        ){
//            ps.setObject(1, user.getId());
            ps.setObject(1, user.getFirstName());
            ps.setObject(2, user.getLastName());
            ps.setObject(3, user.getAge());
            ps.executeUpdate();

            ResultSet resultSet = ps.getGeneratedKeys();
            if (resultSet.next()){
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
                PreparedStatement ps = connection.prepareStatement(FIND_USER_BY_ID_SQL);
        ){
            ps.setObject(1, userId);

            ResultSet resultSet = ps.executeQuery();
            if (!resultSet.next()) {
                throw new RuntimeException();
            }
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
                PreparedStatement ps = connection.prepareStatement(FIND_USER_BY_NAME_SQL)
        ){
            ps.setObject(1, userName);
            ResultSet resultSet = ps.executeQuery();
            if (!resultSet.next()) {
                throw new SQLException("no user with this name");
            }

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
                Statement st = connection.createStatement()
        ) {
            ResultSet resultSet = st.executeQuery(FIND_ALL_USER_SQL);
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
                PreparedStatement ps = connection.prepareStatement(UPDATE_USER_SQL)
        ) {
            ps.setObject(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getAge());
            ps.setLong(4, user.getId());

            if (ps.executeUpdate() == 0) {
                throw new SQLException("no user");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void deleteUser(Long userId) {
        try (
                Connection connection = CustomDataSource.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement(DELETE_USER)
        ) {
            ps.setLong(1, userId);
            if (ps.executeUpdate() == 0) {
                throw new SQLException("no user with this id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

