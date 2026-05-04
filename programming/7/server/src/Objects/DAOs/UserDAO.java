package Objects.DAOs;

import Objects.Collection.Coordinates;
import Objects.Collection.Location;
import Objects.Collection.Person;
import Objects.Collection.Product;
import Objects.Enums.UnitOfMeasure;
import Objects.Managers.DBManager;
import Objects.UserData.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;

public class UserDAO {
    private final static Logger logger = LoggerFactory.getLogger(ProductDAO.class);

    private final DBManager dbManager;

    public UserDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public User register(User newUser) throws SQLException, IOException {
        String sql = "INSERT INTO users values(?,?);";

        Connection connection = dbManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, newUser.getLogin());
        statement.setString(2, newUser.getPassword());

        statement.executeUpdate();
        return newUser;
    }

    public void insertProduct(Product product) throws SQLException, IOException {
        StringBuilder sql = new StringBuilder("INSERT INTO products (")
                .append(DBManager.buildInsertColumns())
                .append(") values (");

        sql.append("?,".repeat(Math.max(0, Product.getCountOfEditableFields(true))));
        sql.deleteCharAt(sql.length() - 1);
        sql.append(");");

        Connection connection = dbManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql.toString());

        fillStatementWithProduct(statement, product, false);

        statement.executeUpdate();
    }

    public void insertProductWithId(long id, Product product) throws SQLException, IOException {
        StringBuilder sql = new StringBuilder("INSERT INTO products (")
                .append(DBManager.buildInsertColumnsWithId())
                .append(") values (");

        sql.append("?,".repeat(Math.max(0, Product.getCountOfEditableFields(true))))
                .append("?")
                .append(");");

        Connection connection = dbManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql.toString());

        statement.setLong(1, id);
        fillStatementWithProduct(statement, product, false);

        statement.executeUpdate();
    }

    public void updateProduct(long id, Product product) throws SQLException, IOException {
        StringBuilder sql = new StringBuilder("UPDATE products SET ")
                .append(DBManager.buildUpdateColumns());

        sql.append("=?")
                .append(" WHERE id = ?;");

        Connection connection = dbManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql.toString());

        product.setId(id);

        fillStatementWithProduct(statement, product, true);

        statement.executeUpdate();
    }

    public void deleteProductById(long id) throws SQLException, IOException {
        String sql = "DELETE FROM products WHERE id=?;";


        Connection connection = dbManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setLong(1, id);

        statement.executeUpdate();
    }

    public void deleteAllProducts() throws SQLException, IOException {
        String sql = "DELETE FROM products;";


        Connection connection = dbManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.executeUpdate();
    }

    private void fillStatementWithProduct(
            PreparedStatement statement,
            Product product,
            boolean fillId
    ) throws SQLException {
        int index = 1;

        statement.setString(index++, product.getName());

        Coordinates coordinates = product.getCoordinates();
        statement.setInt(index++, coordinates.getX());
        statement.setDouble(index++, coordinates.getY());

        statement.setDate(index++, new Date(product.getCreationDate().getTime()));

        if (product.getPrice() == null) {
            statement.setNull(index++, Types.DOUBLE);
        } else {
            statement.setDouble(index++, product.getPrice());
        }

        statement.setInt(index++, product.getManufactureCost());

        UnitOfMeasure unit = product.getUnitOfMeasure();

        if (unit == null) {
            statement.setNull(index++, Types.VARCHAR);
        } else {
            statement.setString(index++, product.getUnitOfMeasure().name());
        }

        Person owner = product.getOwner();
        if (owner == null) {
            statement.setNull(index++, Types.VARCHAR);
            statement.setNull(index++, Types.REAL);
            statement.setNull(index++, Types.VARCHAR);
            statement.setNull(index++, Types.VARCHAR);
            statement.setNull(index++, Types.VARCHAR);
            statement.setNull(index++, Types.DOUBLE);
            statement.setNull(index++, Types.INTEGER);
            statement.setNull(index++, Types.DOUBLE);
            statement.setNull(index++, Types.VARCHAR);
        } else {
            statement.setString(index++, owner.getName());
            statement.setFloat(index++, owner.getHeight());

            if (owner.getEyeColor() == null) {
                statement.setNull(index++, Types.VARCHAR);
            } else {
                statement.setString(index++, owner.getEyeColor().name());
            }

            statement.setString(index++, owner.getHairColor().name());
            statement.setString(index++, owner.getNationality().name());

            Location location = owner.getLocation();

            if (location == null) {
                statement.setNull(index++, Types.DOUBLE);
                statement.setNull(index++, Types.INTEGER);
                statement.setNull(index++, Types.DOUBLE);
                statement.setNull(index++, Types.VARCHAR);
            } else {
                statement.setDouble(index++, location.getX());
                statement.setInt(index++, location.getY());
                statement.setDouble(index++, location.getZ());
                statement.setString(index++, location.getName());
            }
        }

        if (fillId)
            statement.setLong(index, product.getId());
    }
}
