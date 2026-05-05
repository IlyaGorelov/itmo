package Objects.Managers;

import Objects.DAOs.UserDAO;
import Objects.UserData.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

/**
 * class that controls collection
 */
public class AuthManager {
    private final static Logger logger = LoggerFactory.getLogger(AuthManager.class);

    private final UserDAO userDAO;

    private final String urlToDb;

    public AuthManager(String envKeyToDbUrl, String envKeyToPropsPath) {
        this.urlToDb = System.getenv(envKeyToDbUrl);
        String pathToProps = System.getenv(envKeyToPropsPath);

        userDAO = new UserDAO(new DBManager(urlToDb, pathToProps));
    }

    /**
     * transform collection into ArrayList to save it in CSVManager
     */
    public User register(User newUser) throws SQLException, IOException {
        User registered = userDAO.register(newUser);
        return registered;
    }

    public User login(User user) {
        try {
            User logged = userDAO.login(user);
            return logged;
        } catch (SQLException | IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


}
