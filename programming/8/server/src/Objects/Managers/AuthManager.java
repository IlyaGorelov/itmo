package Objects.Managers;

import Commons.UserData.User;
import Objects.DAOs.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

/**
 * class that controls collection
 */
public class AuthManager {
    private final static Logger logger = LoggerFactory.getLogger(AuthManager.class);

    private final UserDAO userDAO = new UserDAO();

    public User register(User newUser) throws SQLException, IOException {
        return userDAO.register(newUser);
    }

    public User login(User user) {
        try {
            return userDAO.login(user);
        } catch (SQLException | IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


}
