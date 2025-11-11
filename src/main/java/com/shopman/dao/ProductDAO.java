package com.shopman.dao;

import com.shopman.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO extends DAO {
    
    /**
     * Get all products from database
     * @return List of all products
     */
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
    
    /**
     * Get product by ID
     * @param id Product ID
     * @return Product object or null if not found
     */
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
    
    /**
     * Search products by name
     * @param keyword Search keyword
     * @return List of matching products
     */
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
    
    /**
     * Insert new product
     * @param product Product to insert
     * @return true if successful, false otherwise
     */
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
    
    /**
     * Update existing product
     * @param product Product to update
     * @return true if successful, false otherwise
     */
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
    
    /**
     * Update product quantity
     * @param productId Product ID
     * @param quantity New quantity
     * @return true if successful, false otherwise
     */
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
    
    /**
     * Delete product by ID
     * @param id Product ID
     * @return true if successful, false otherwise
     */
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
    
    /**
     * Get total number of products
     * @return Total count
     */
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
    
    /**
     * Get products with pagination
     * @param offset Starting position
     * @param limit Number of records to fetch
     * @return List of products
     */
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
    
    /**
     * Generate next product ID automatically
     * @return Next product ID in format P00001, P00002, etc.
     */
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
                // Extract number from ID (handle both "P00001" and "00001" format)
                int number = 0;
                if (lastId.matches("^[A-Z].*")) {
                    // Has letter prefix (e.g., "P00001")
                    number = Integer.parseInt(lastId.substring(1));
                } else {
                    // Pure number (e.g., "00001")
                    number = Integer.parseInt(lastId);
                }
                // Increment and format
                return String.format("P%05d", number + 1);
            } else {
                // No products yet, start from P00001
                return "P00001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Return default if error
            return "P00001";
        } finally {
            closeConnection(conn, ps, rs);
        }
    }
}
