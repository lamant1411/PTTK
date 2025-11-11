package com.shopman.dao;
import com.shopman.model.Cart;
import com.shopman.model.CartDetail;
import com.shopman.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class CartDAO extends DAO {
    public Cart getCartByCustomerId(String customerId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Cart cart = null;
        try {
            conn = getConnection();
            String sql = "SELECT * FROM tblCart WHERE tblCustomersId = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, customerId);
            rs = ps.executeQuery();
            if (rs.next()) {
                cart = new Cart(
                    rs.getString("id"),
                    null, 
                    null  
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, ps, rs);
        }
        return cart;
    }
    public Cart getCartById(String cartId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Cart cart = null;
        try {
            conn = getConnection();
            String sql = "SELECT * FROM tblCart WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cartId);
            rs = ps.executeQuery();
            if (rs.next()) {
                cart = new Cart(
                    rs.getString("id"),
                    null,
                    null
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, ps, rs);
        }
        return cart;
    }
    public String getNextCartId() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "SELECT id FROM tblCart ORDER BY id DESC LIMIT 1";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                String lastId = rs.getString("id");
                try {
                    int number = Integer.parseInt(lastId);
                    return String.format("%05d", number + 1);
                } catch (NumberFormatException e) {
                    String numberPart = lastId.replaceAll("[^0-9]", "");
                    if (!numberPart.isEmpty()) {
                        int number = Integer.parseInt(numberPart);
                        return String.format("%05d", number + 1);
                    }
                }
            }
            return "00001";
        } catch (SQLException e) {
            e.printStackTrace();
            return String.format("%05d", (int)(Math.random() * 100000));
        } finally {
            closeConnection(conn, ps, rs);
        }
    }
    public boolean createCart(String cartId, String customerId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            String sql = "INSERT INTO tblCart (id, tblCustomersId) VALUES (?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cartId);
            ps.setString(2, customerId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn, ps);
        }
    }
    public boolean deleteCart(String cartId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            String sql = "DELETE FROM tblCart WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cartId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn, ps);
        }
    }
    public List<CartDetail> getCartDetails(String cartId) {
        List<CartDetail> cartDetails = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "SELECT cd.*, p.id as product_id, p.name, p.price as product_price, p.quantity as product_quantity, p.des " +
                        "FROM tblCartDetail cd " +
                        "JOIN tblProduct p ON cd.tblProductId = p.id " +
                        "WHERE cd.tblCartId = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cartId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product(
                    rs.getString("product_id"),
                    rs.getString("name"),
                    rs.getFloat("product_price"),
                    rs.getInt("product_quantity"),
                    rs.getString("des")
                );
                CartDetail cartDetail = new CartDetail(
                    rs.getString("id"),
                    rs.getInt("quantity"),
                    product
                );
                cartDetails.add(cartDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, ps, rs);
        }
        return cartDetails;
    }
    public boolean addToCart(String cartDetailId, String cartId, String productId, int quantity, float price) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            String sql = "INSERT INTO tblCartDetail (id, price, quantity, tblCartId, tblProductId) VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cartDetailId);
            ps.setFloat(2, price);
            ps.setInt(3, quantity);
            ps.setString(4, cartId);
            ps.setString(5, productId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn, ps);
        }
    }
    public boolean updateCartDetailQuantity(String cartDetailId, int quantity) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            String sql = "UPDATE tblCartDetail SET quantity = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, quantity);
            ps.setString(2, cartDetailId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn, ps);
        }
    }
    public boolean removeFromCart(String cartDetailId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            String sql = "DELETE FROM tblCartDetail WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cartDetailId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn, ps);
        }
    }
    public boolean clearCart(String cartId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            String sql = "DELETE FROM tblCartDetail WHERE tblCartId = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cartId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected >= 0; 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn, ps);
        }
    }
    public CartDetail getCartDetailById(String cartDetailId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        CartDetail cartDetail = null;
        try {
            conn = getConnection();
            String sql = "SELECT cd.*, p.id as product_id, p.name, p.price as product_price, p.quantity as product_quantity, p.des " +
                        "FROM tblCartDetail cd " +
                        "JOIN tblProduct p ON cd.tblProductId = p.id " +
                        "WHERE cd.id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cartDetailId);
            rs = ps.executeQuery();
            if (rs.next()) {
                Product product = new Product(
                    rs.getString("product_id"),
                    rs.getString("name"),
                    rs.getFloat("product_price"),
                    rs.getInt("product_quantity"),
                    rs.getString("des")
                );
                cartDetail = new CartDetail(
                    rs.getString("id"),
                    rs.getInt("quantity"),
                    product
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, ps, rs);
        }
        return cartDetail;
    }
    public float calculateCartTotal(String cartId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        float total = 0;
        try {
            conn = getConnection();
            String sql = "SELECT SUM(price * quantity) as total FROM tblCartDetail WHERE tblCartId = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cartId);
            rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getFloat("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, ps, rs);
        }
        return total;
    }
    public CartDetail getCartDetailByProduct(String cartId, String productId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        CartDetail cartDetail = null;
        try {
            conn = getConnection();
            String sql = "SELECT cd.*, p.id as product_id, p.name, p.price as product_price, p.quantity as product_quantity, p.des " +
                        "FROM tblCartDetail cd " +
                        "JOIN tblProduct p ON cd.tblProductId = p.id " +
                        "WHERE cd.tblCartId = ? AND cd.tblProductId = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cartId);
            ps.setString(2, productId);
            rs = ps.executeQuery();
            if (rs.next()) {
                Product product = new Product(
                    rs.getString("product_id"),
                    rs.getString("name"),
                    rs.getFloat("product_price"),
                    rs.getInt("product_quantity"),
                    rs.getString("des")
                );
                cartDetail = new CartDetail(
                    rs.getString("id"),
                    rs.getInt("quantity"),
                    product
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, ps, rs);
        }
        return cartDetail;
    }
    public String getNextCartDetailId() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "SELECT id FROM tblCartDetail ORDER BY id DESC LIMIT 1";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                String lastId = rs.getString("id");
                String numberPart = lastId.replaceAll("[^0-9]", "");
                if (!numberPart.isEmpty()) {
                    int number = Integer.parseInt(numberPart);
                    return String.format("CD%05d", number + 1);
                }
            }
            return "CD00001";
        } catch (SQLException e) {
            e.printStackTrace();
            return "CD00001";
        } finally {
            closeConnection(conn, ps, rs);
        }
    }
}