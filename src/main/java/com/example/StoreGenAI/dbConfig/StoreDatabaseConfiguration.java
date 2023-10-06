package com.example.StoreGenAI.dbConfig;

import com.example.StoreGenAI.model.Product;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class StoreDatabaseConfiguration {

    public StoreDatabaseConfiguration() {
        createDatabase();
    }

    private String DATA_SOURCE_URL = "jdbc:mysql://127.0.0.1:3306/";
    private final String PRODUCTS_LIST_ST = "SELECT * FROM products;";
    private final String DB_CREATION_ST = "CREATE DATABASE IF NOT EXISTS my_store;";
    private final String PRODUCT_TABLE_CREATION_ST = "CREATE TABLE IF NOT EXISTS products (" +
            "ID INT AUTO_INCREMENT," +
            " name VARCHAR(255)," +
            " description TEXT," +
            " quantity INT," +
            " price DOUBLE," +
            " PRIMARY KEY (ID)" +
            ");";
    private final String PRODUCT_UPDATE_ST = "UPDATE products " +
            "SET description = ?," +
            " quantity = ?," +
            " price = ?" +
            " name = ? " +
            " WHERE ID = ?;";
    private final String SAVE_PRODUCT_ST = "INSERT INTO products (name, description, quantity, price)" +
            "VALUES (?, ?, ?, ?);";
    private final String GET_PRODUCT_BY_ID = "SELECT * FROM products WHERE ID = ?;";

    @Bean
    private DataSource dataSource() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(DATA_SOURCE_URL);
        dataSource.setUsername("lenamargariann");
        dataSource.setPassword("Dinozavr-123");
        return dataSource;
    }

    private void createDatabase() {
        try (Connection connection = dataSource().getConnection()) {
            PreparedStatement statement1 = connection.prepareStatement(DB_CREATION_ST);
            statement1.execute();
            DATA_SOURCE_URL += "my_store";
            createProductTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createProductTable() {
        try (Connection connection = dataSource().getConnection()) {
            PreparedStatement statement2 = connection.prepareStatement(PRODUCT_TABLE_CREATION_ST);
            statement2.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateExistingProduct(Product product) {
        try (Connection connection = dataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(PRODUCT_UPDATE_ST);
            statement.setString(1, product.getDescription());
            statement.setInt(2, product.getQuantity());
            statement.setDouble(3, product.getPrice());
            statement.setString(4, product.getName());
            statement.setLong(5, product.getId());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean saveProduct(Product product) {
        try (Connection connection = dataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SAVE_PRODUCT_ST);
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setInt(3, product.getQuantity());
            statement.setDouble(4, product.getPrice());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Product getProduct(String id) {
        try (Connection connection = dataSource().getConnection()) {
            PreparedStatement st = connection.prepareStatement(GET_PRODUCT_BY_ID);
            st.setString(1, id);
            ResultSet set = st.executeQuery();
            if (set.next()) {
                Product product = new Product();
                product.setId(Long.parseLong(set.getString("id")));
                product.setName(set.getString("name"));
                product.setDescription(set.getString("description"));
                product.setQuantity(Integer.parseInt(set.getString("quantity")));
                product.setPrice(Double.parseDouble(set.getString("price")));
                return product;
            } else return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = dataSource().getConnection()) {
            PreparedStatement st = connection.prepareStatement(PRODUCTS_LIST_ST);
            ResultSet set = st.executeQuery();
            while (set.next()) {
                Product product = new Product();
                product.setId(Long.parseLong(set.getString("id")));
                product.setName(set.getString("name"));
                product.setDescription(set.getString("description"));
                product.setQuantity(Integer.parseInt(set.getString("quantity")));
                product.setPrice(Double.parseDouble(set.getString("price")));
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }
}
