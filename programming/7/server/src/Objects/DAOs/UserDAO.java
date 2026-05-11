package Objects.DAOs;

import Objects.Helpers.PasswordHasher;
import Objects.Managers.DBManager;
import Objects.UserData.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private final static Logger logger = LoggerFactory.getLogger(ProductDAO.class);

    public User register(User newUser) throws SQLException, IOException {
        PasswordHasher.PasswordData passwordData = PasswordHasher.hashPassword(newUser.getPassword());

        String sql = "INSERT INTO users(login,password,salt) values(?,?,?) RETURNING id,login,password;";

        Connection connection = DBManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, newUser.getLogin());
        statement.setString(2, passwordData.hash());
        statement.setString(3, passwordData.salt());

        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            User registered = new User(resultSet.getLong("id"),
                    resultSet.getString("login"));

            return registered;
        } else
            return null;
    }

    public static User getUserById(long id) throws SQLException, IOException {
        String sql = "SELECT id,login FROM users WHERE id=?;";

        Connection connection = DBManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setLong(1, id);

        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            User user = new User(resultSet.getLong("id"),
                    resultSet.getString("login"));


            return user;
        }
        throw new IllegalArgumentException("Product's author wasn't found");
    }

    public User login(User user) throws SQLException, IOException {
        String sql = "SELECT id,login,password,salt FROM users WHERE login=?;";

        Connection connection = DBManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, user.getLogin());

        ResultSet resultSet = statement.executeQuery();

        if (!resultSet.next())
            return null;

        Long id = resultSet.getLong("id");
        String login = resultSet.getString("login");

        String password_hash = resultSet.getString("password");
        String salt = resultSet.getString("salt");

        if (PasswordHasher.checkPassword(user.getPassword(), password_hash, salt))
            return new User(id, login);
        else
            return null;
    }
}
