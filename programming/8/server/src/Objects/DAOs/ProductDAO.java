package Objects.DAOs;

import Commons.Collection.Coordinates;
import Commons.Collection.Location;
import Commons.Collection.Person;
import Commons.Collection.Product;
import Objects.Enums.UnitOfMeasure;
import Objects.Managers.DBManager;
import Objects.Parsers.ProductParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;

public class ProductDAO {
    private final static Logger logger = LoggerFactory.getLogger(ProductDAO.class);

    public HashSet<Product> loadProducts() {
        HashSet<Product> result = new HashSet<>();

        String sql = "SELECT " + DBManager.buildSelectColumns() + " FROM products";

        try {
            Connection connection = DBManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            ProductParser productParser = new ProductParser();
            while (resultSet.next()) {
                Product product = productParser.parse(resultSet);
                if (product != null)
                    result.add(product);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return result;
    }

    public Product insertProduct(Product product) throws SQLException, IOException {
        StringBuilder sql = new StringBuilder("INSERT INTO products (")
                .append(DBManager.buildInsertColumns())
                .append(") values (");

        sql.append("?,".repeat(Math.max(0, Product.getCountOfEditableFields(true))))
                .deleteCharAt(sql.length() - 1)
                .append(") ")
                .append("RETURNING ")
                .append(DBManager.buildSelectColumns())
                .append(";");

        Connection connection = DBManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql.toString());

        fillStatementWithProduct(statement, product, false);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next())
            return new ProductParser().parse(resultSet);
        else
            throw new SQLException("Product wasn't added");
    }

    public void insertProductWithId(long id, Product product) throws SQLException, IOException {
        StringBuilder sql = new StringBuilder("INSERT INTO products (")
                .append(DBManager.buildInsertColumnsWithId())
                .append(") values (");

        sql.append("?,".repeat(Math.max(0, Product.getCountOfEditableFields(true))))
                .append("?")
                .append(");");

        Connection connection = DBManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql.toString());

        statement.setLong(1, id);
        fillStatementWithProduct(statement, product, true);

        statement.executeUpdate();
    }

    public void updateProduct(long id, Product product) throws SQLException, IOException {
        StringBuilder sql = new StringBuilder("UPDATE products SET ")
                .append(DBManager.buildUpdateColumns());

        sql.append("=?")
                .append(" WHERE id = ?;");

        Connection connection = DBManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql.toString());

        fillStatementWithProduct(statement, product, false);
        statement.setLong(18, id);

        statement.executeUpdate();
    }

    public void deleteProductById(long id) throws SQLException, IOException {
        String sql = "DELETE FROM products WHERE id=?;";


        Connection connection = DBManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setLong(1, id);

        statement.executeUpdate();
    }

    private void fillStatementWithProduct(
            PreparedStatement statement,
            Product product,
            boolean fillId
    ) throws SQLException {
        int index = fillId ? 2 : 1;

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
        statement.setLong(index++, product.getAuthor().getId());

    }
}
