package com.shopman.dao;

import com.shopman.model.Order;
import com.shopman.model.OrderDetail;
import com.shopman.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO extends DAO {
    
    /**
     * Lấy tất cả đơn hàng
     * @return Danh sách tất cả đơn hàng
     */
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            String sql = "SELECT * FROM tblOrder";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Order order = new Order(
                    rs.getString("id"),
                    rs.getFloat("total"),
                    new java.sql.Time(rs.getTime("time").getTime()),
                    new java.sql.Date(rs.getDate("date").getTime()),
                    null, // seller - sẽ được tải riêng nếu cần
                    null, // customer - sẽ được tải riêng nếu cần
                    null  // shipper - sẽ được tải riêng nếu cần
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, ps, rs);
        }
        
        return orders;
    }
    
    /**
     * Lấy đơn hàng theo ID cùng thông tin khách hàng
     * @param orderId Mã đơn hàng
     * @return Đối tượng Order hoặc null nếu không tìm thấy
     */
    public Order getOrderById(String orderId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Order order = null;
        
        try {
            conn = getConnection();
            String sql = "SELECT o.*, c.id as customer_id, m.name, m.addr, m.phone " +
                        "FROM tblOrder o " +
                        "LEFT JOIN tblCustomer c ON o.tblCustomerId = c.id " +
                        "LEFT JOIN tblMember m ON c.id = m.id " +
                        "WHERE o.id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, orderId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                // Tạo đối tượng customer
                com.shopman.model.Customer customer = new com.shopman.model.Customer();
                customer.setId(rs.getString("customer_id"));
                customer.setName(rs.getString("name"));
                customer.setAdd(rs.getString("addr"));
                customer.setPhone(rs.getString("phone"));
                
                // Tạo đối tượng order
                order = new Order();
                order.setId(rs.getString("id"));
                order.setTotal(rs.getFloat("total"));
                order.setTime(new java.sql.Time(rs.getTime("time").getTime()));
                order.setDate(new java.sql.Date(rs.getDate("date").getTime()));
                order.setStatus(rs.getString("status"));
                order.setCustomer(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, ps, rs);
        }
        
        return order;
    }
    
    /**
     * Get orders by customer ID
     * @param customerId Customer ID
     * @return List of orders
     */
    public List<Order> getOrdersByCustomerId(String customerId) {
        List<Order> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            String sql = "SELECT * FROM tblOrder WHERE tblCustomerId = ? ORDER BY date DESC, time DESC";
            ps = conn.prepareStatement(sql);
            ps.setString(1, customerId);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getString("id"));
                order.setTotal(rs.getFloat("total"));
                order.setTime(new java.sql.Time(rs.getTime("time").getTime()));
                order.setDate(new java.sql.Date(rs.getDate("date").getTime()));
                order.setStatus(rs.getString("status"));
                
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, ps, rs);
        }
        
        return orders;
    }
    
    /**
     * Get orders by seller ID
     * @param sellerId Seller ID
     * @return List of orders
     */
    public List<Order> getOrdersBySellerId(String sellerId) {
        List<Order> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            String sql = "SELECT * FROM tblOrder WHERE tblSellerId = ? ORDER BY date DESC, time DESC";
            ps = conn.prepareStatement(sql);
            ps.setString(1, sellerId);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Order order = new Order(
                    rs.getString("id"),
                    rs.getFloat("total"),
                    new java.sql.Time(rs.getTime("time").getTime()),
                    new java.sql.Date(rs.getDate("date").getTime()),
                    null,
                    null,
                    null
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, ps, rs);
        }
        
        return orders;
    }
    
    /**
     * Get orders by shipper ID
     * @param shipperId Shipper ID
     * @return List of orders
     */
    public List<Order> getOrdersByShipperId(String shipperId) {
        List<Order> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            String sql = "SELECT * FROM tblOrder WHERE tblShipperId = ? ORDER BY date DESC, time DESC";
            ps = conn.prepareStatement(sql);
            ps.setString(1, shipperId);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Order order = new Order(
                    rs.getString("id"),
                    rs.getFloat("total"),
                    new java.sql.Time(rs.getTime("time").getTime()),
                    new java.sql.Date(rs.getDate("date").getTime()),
                    null,
                    null,
                    null
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, ps, rs);
        }
        
        return orders;
    }
    
    /**
     * Get orders by status
     * @param status Order status
     * @return List of orders
     */
    public List<Order> getOrdersByStatus(String status) {
        List<Order> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            String sql = "SELECT * FROM tblOrder WHERE status = ? ORDER BY date DESC, time DESC";
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Order order = new Order(
                    rs.getString("id"),
                    rs.getFloat("total"),
                    new java.sql.Time(rs.getTime("time").getTime()),
                    new java.sql.Date(rs.getDate("date").getTime()),
                    null,
                    null,
                    null
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, ps, rs);
        }
        
        return orders;
    }
    
    /**
     * Create new order
     * @param orderId Order ID
     * @param total Total amount
     * @param time Time
     * @param date Date
     * @param status Status
     * @param customerId Customer ID
     * @param sellerId Seller ID
     * @param shipperId Shipper ID
     * @return true if successful, false otherwise
     */
    public boolean createOrder(Order order) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = getConnection();
            String sql = "INSERT INTO tblOrder (id, total, time, date, status, tblCustomerId, tblSellerId, tblShipperId) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, order.getId());
            ps.setFloat(2, order.getTotal());
            ps.setTime(3, order.getTime());
            ps.setDate(4, order.getDate());
            ps.setString(5, order.getStatus());
            ps.setString(6, order.getCustomer().getId());
            ps.setString(7, order.getSeller() != null ? order.getSeller().getId() : null);
            ps.setString(8, order.getShipper() != null ? order.getShipper().getId() : null);

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
     * Update order status
     * @param orderId Order ID
     * @param status New status
     * @return true if successful, false otherwise
     */
    public boolean updateOrderStatus(String orderId, String status) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = getConnection();
            String sql = "UPDATE tblOrder SET status = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, orderId);
            
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
     * Update order
     * @param orderId Order ID
     * @param total Total amount
     * @param status Status
     * @return true if successful, false otherwise
     */
    public boolean updateOrder(String orderId, float total, String status) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = getConnection();
            String sql = "UPDATE tblOrder SET total = ?, status = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setFloat(1, total);
            ps.setString(2, status);
            ps.setString(3, orderId);
            
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
     * Delete order
     * @param orderId Order ID
     * @return true if successful, false otherwise
     */
    public boolean deleteOrder(String orderId) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = getConnection();
            String sql = "DELETE FROM tblOrder WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, orderId);
            
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
     * Get order details by order ID
     * @param orderId Order ID
     * @return List of OrderDetail objects
     */
    public List<OrderDetail> getOrderDetails(String orderId) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            String sql = "SELECT od.*, p.id as product_id, p.name, p.price as product_price, p.quantity as product_quantity, p.des, p.unit " +
                        "FROM tblOrderDetail od " +
                        "JOIN tblProduct p ON od.tblProductId = p.id " +
                        "WHERE od.tblOrderId = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, orderId);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Product product = new Product(
                    rs.getString("product_id"),
                    rs.getString("name"),
                    rs.getFloat("product_price"),
                    rs.getInt("product_quantity"),
                    rs.getString("des"),
                    rs.getString("unit")
                );
                
                OrderDetail orderDetail = new OrderDetail(
                    rs.getString("id"),
                    rs.getFloat("price"),
                    rs.getInt("quantity"),
                    null, // order reference
                    product
                );
                orderDetails.add(orderDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, ps, rs);
        }
        
        return orderDetails;
    }
    
    /**
     * Add order detail
     * @param orderDetailId Order detail ID
     * @param orderId Order ID
     * @param productId Product ID
     * @param quantity Quantity
     * @param price Price
     * @return true if successful, false otherwise
     */
    public boolean addOrderDetail(String orderDetailId, String orderId, String productId, int quantity, float price) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = getConnection();
            String sql = "INSERT INTO tblOrderDetail (id, price, quantity, tblOrderId, tblProductId) VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, orderDetailId);
            ps.setFloat(2, price);
            ps.setInt(3, quantity);
            ps.setString(4, orderId);
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
    
    /**
     * Update order detail
     * @param orderDetailId Order detail ID
     * @param quantity New quantity
     * @param price New price
     * @return true if successful, false otherwise
     */
    public boolean updateOrderDetail(String orderDetailId, int quantity, float price) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = getConnection();
            String sql = "UPDATE tblOrderDetail SET quantity = ?, price = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, quantity);
            ps.setFloat(2, price);
            ps.setString(3, orderDetailId);
            
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
     * Delete order detail
     * @param orderDetailId Order detail ID
     * @return true if successful, false otherwise
     */
    public boolean deleteOrderDetail(String orderDetailId) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = getConnection();
            String sql = "DELETE FROM tblOrderDetail WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, orderDetailId);
            
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
     * Calculate total order amount from order details
     * @param orderId Order ID
     * @return Total amount
     */
    public float calculateOrderTotal(String orderId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        float total = 0;
        
        try {
            conn = getConnection();
            String sql = "SELECT SUM(price * quantity) as total FROM tblOrderDetail WHERE tblOrderId = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, orderId);
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
    
    /**
     * Get total number of orders
     * @return Total count
     */
    public int getTotalOrders() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        
        try {
            conn = getConnection();
            String sql = "SELECT COUNT(*) as total FROM tblOrder";
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
     * Get next order ID (auto-generated)
     * @return Next order ID in format O00001, O00002, etc.
     */
    public String getNextOrderId() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String nextId = "O00001";
        
        try {
            conn = getConnection();
            String sql = "SELECT id FROM tblOrder ORDER BY id DESC LIMIT 1";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                String lastId = rs.getString("id");
                // Extract number from ID (e.g., "O00001" -> 1 or "00001" -> 1)
                String numberPart = lastId.replaceAll("[^0-9]", "");
                int number = Integer.parseInt(numberPart);
                nextId = String.format("O%05d", number + 1);
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            // Return default if error
            nextId = "O00001";
        } finally {
            closeConnection(conn, ps, rs);
        }
        
        return nextId;
    }
    
    /**
     * Get next order detail ID (auto-generated)
     * @return Next order detail ID in format OD00001, OD00002, etc.
     */
    public String getNextOrderDetailId() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String nextId = "OD00001";
        
        try {
            conn = getConnection();
            String sql = "SELECT id FROM tblOrderDetail ORDER BY id DESC LIMIT 1";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                String lastId = rs.getString("id");
                // Extract number from ID (e.g., "OD00001" -> 1 or "00001" -> 1)
                String numberPart = lastId.replaceAll("[^0-9]", "");
                int number = Integer.parseInt(numberPart);
                nextId = String.format("OD%05d", number + 1);
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            // Return default if error
            nextId = "OD00001";
        } finally {
            closeConnection(conn, ps, rs);
        }
        
        return nextId;
    }
}
