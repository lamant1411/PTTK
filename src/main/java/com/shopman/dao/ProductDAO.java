package com.shopman.dao;
import com.shopman.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class ProductDAO extends DAO {
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "SELECT * FROM tblProduct";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getFloat("price"),
                    rs.getInt("quantity"),
                    rs.getString("des"),
                    rs.getString("unit")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, ps, rs);
        }
        return products;
    }
    public Product getProductById(String id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Product product = null;
        try {
            conn = getConnection();
            String sql = "SELECT * FROM tblProduct WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                product = new Product(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getFloat("price"),
                    rs.getInt("quantity"),
                    rs.getString("des"),
                    rs.getString("unit")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, ps, rs);
        }
        return product;
    }
    public List<Product> searchProductsByName(String keyword) {
        List<Product> products = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "SELECT * FROM tblProduct WHERE name LIKE ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getFloat("price"),
                    rs.getInt("quantity"),
                    rs.getString("des"),
                    rs.getString("unit")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, ps, rs);
        }
        return products;
    }
    public boolean saveProduct(Product product) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            String sql = "INSERT INTO tblProduct (id, name, price, quantity, des, unit) VALUES (?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, product.getId());
            ps.setString(2, product.getName());
            ps.setFloat(3, product.getPrice());
            ps.setInt(4, product.getQuantity());
            ps.setString(5, product.getDes());
            ps.setString(6, product.getUnit());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn, ps);
        }
    }
    public boolean updateProduct(Product product) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            String sql = "UPDATE tblProduct SET name = ?, price = ?, quantity = ?, des = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, product.getName());
            ps.setFloat(2, product.getPrice());
            ps.setInt(3, product.getQuantity());
            ps.setString(4, product.getDes());
            ps.setString(5, product.getId());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn, ps);
        }
    }
    public boolean updateProductQuantity(String productId, int quantity) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            String sql = "UPDATE tblProduct SET quantity = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, quantity);
            ps.setString(2, productId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn, ps);
        }
    }
    public boolean deleteProduct(String id) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            String sql = "DELETE FROM tblProduct WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn, ps);
        }
    }
    public int getTotalProducts() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        try {
            conn = getConnection();
            String sql = "SELECT COUNT(*) as total FROM tblProduct";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, ps, rs);
        }
        return count;
    }
    public List<Product> getProductsWithPagination(int offset, int limit) {
        List<Product> products = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "SELECT * FROM tblProduct LIMIT ? OFFSET ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getFloat("price"),
                    rs.getInt("quantity"),
                    rs.getString("des"),
                    rs.getString("unit")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, ps, rs);
        }
        return products;
    }
    public String getNextProductId() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "SELECT id FROM tblProduct ORDER BY id DESC LIMIT 1";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                String lastId = rs.getString("id");
                int number = 0;
                if (lastId.matches("^[A-Z].*")) {
                    number = Integer.parseInt(lastId.substring(1));
                } else {
                    number = Integer.parseInt(lastId);
                }
                return String.format("P%05d", number + 1);
            } else {
                return "P00001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "P00001";
        } finally {
            closeConnection(conn, ps, rs);
        }
    }
}