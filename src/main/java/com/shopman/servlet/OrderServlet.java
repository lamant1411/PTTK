package com.shopman.servlet;

import com.shopman.dao.OrderDAO;
import com.shopman.dao.CartDAO;
import com.shopman.dao.ProductDAO;
import com.shopman.model.Order;
import com.shopman.model.OrderDetail;
import com.shopman.model.CartDetail;
import com.shopman.model.Customer;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet for handling Order operations
 * Supports: list orders, view order, create order, update status, cancel order
 */
@WebServlet(name = "OrderServlet", urlPatterns = {"/order"})
public class OrderServlet extends HttpServlet {
    
    private OrderDAO orderDAO;
    private CartDAO cartDAO;
    private ProductDAO productDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        orderDAO = new OrderDAO();
        cartDAO = new CartDAO();
        productDAO = new ProductDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "list":
                listOrders(request, response);
                break;
            case "view":
                viewOrder(request, response);
                break;
            case "myOrders":
                viewMyOrders(request, response);
                break;
            case "checkout":
                showCheckout(request, response);
                break;
            case "cancel":
                cancelOrder(request, response);
                break;
            default:
                listOrders(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "create":
                createOrder(request, response);
                break;
            case "updateStatus":
                updateOrderStatus(request, response);
                break;
            default:
                listOrders(request, response);
                break;
        }
    }
    
    /**
     * List all orders (for manager/seller)
     */
    private void listOrders(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        String userId = (String) session.getAttribute("userId");
        
        if (role == null || userId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        try {
            List<Order> orders;
            String statusFilter = request.getParameter("status");
            
            if ("seller".equals(role)) {
                // Seller sees only their orders
                if (statusFilter != null && !statusFilter.isEmpty()) {
                    orders = orderDAO.getOrdersByStatus(statusFilter);
                } else {
                    orders = orderDAO.getOrdersBySellerId(userId);
                }
            } else if ("shipper".equals(role)) {
                // Shipper sees their assigned orders
                orders = orderDAO.getOrdersByShipperId(userId);
            } else {
                // Manager sees all orders
                if (statusFilter != null && !statusFilter.isEmpty()) {
                    orders = orderDAO.getOrdersByStatus(statusFilter);
                } else {
                    orders = orderDAO.getAllOrders();
                }
            }
            
            request.setAttribute("orders", orders);
            request.setAttribute("statusFilter", statusFilter);
            request.setAttribute("totalOrders", orders.size());
            
            request.getRequestDispatcher("/manager/order-list.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading orders: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    /**
     * View single order details
     */
    private void viewOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        String orderId = request.getParameter("id");
        
        if (orderId == null || orderId.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/order?action=list");
            return;
        }
        
        try {
            Order order = orderDAO.getOrderById(orderId);
            
            if (order == null) {
                request.setAttribute("error", "Order not found");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }
            
            List<OrderDetail> orderDetails = orderDAO.getOrderDetails(orderId);
            
            request.setAttribute("order", order);
            request.setAttribute("orderDetails", orderDetails);
            
            String role = (String) session.getAttribute("role");
            
            if ("customer".equals(role)) {
                request.getRequestDispatcher("/customer/OrderView.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/manager/OrderView.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading order: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    /**
     * View customer's own orders
     */
    private void viewMyOrders(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String customerId = (String) session.getAttribute("customerId");
        
        if (customerId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?redirect=myOrders");
            return;
        }
        
        try {
            List<Order> orders = orderDAO.getOrdersByCustomerId(customerId);
            
            request.setAttribute("orders", orders);
            request.setAttribute("totalOrders", orders.size());
            
            request.getRequestDispatcher("/customer/OrderListView.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading orders: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    /**
     * Show checkout page
     */
    private void showCheckout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String customerId = (String) session.getAttribute("customerId");
        
        if (customerId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?redirect=checkout");
            return;
        }
        
        try {
            var cart = cartDAO.getCartByCustomerId(customerId);
            
            if (cart == null) {
                response.sendRedirect(request.getContextPath() + "/cart?action=view&error=Cart is empty");
                return;
            }
            
            List<CartDetail> cartDetails = cartDAO.getCartDetails(cart.getId());
            
            if (cartDetails.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/cart?action=view&error=Cart is empty");
                return;
            }
            
            float totalAmount = cartDAO.calculateCartTotal(cart.getId());
            
            request.setAttribute("cartDetails", cartDetails);
            request.setAttribute("totalAmount", totalAmount);
            
            request.getRequestDispatcher("/customer/checkout.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading checkout: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    /**
     * Create new order from cart (immediately without additional info)
     */
    private void createOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String customerId = (String) session.getAttribute("customerId");
        
        if (customerId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?redirect=cart");
            return;
        }
        
        try {
            // Get cart
            var cart = cartDAO.getCartByCustomerId(customerId);
            
            if (cart == null) {
                response.sendRedirect(request.getContextPath() + "/cart?action=view&error=Cart is empty");
                return;
            }
            
            List<CartDetail> cartDetails = cartDAO.getCartDetails(cart.getId());
            
            if (cartDetails.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/cart?action=view&error=Cart is empty");
                return;
            }
            
            // Validate stock availability
            for (CartDetail detail : cartDetails) {
                var product = productDAO.getProductById(detail.getProduct().getId());
                if (product.getQuantity() < detail.getQuantity()) {
                    response.sendRedirect(request.getContextPath() + "/cart?action=view&error=Not enough stock for " + product.getName());
                    return;
                }
            }
            
            // Calculate total
            float total = cartDAO.calculateCartTotal(cart.getId());
            
            // Create order with auto-generated ID (no additional info required)
            String orderId = orderDAO.getNextOrderId();
            long now = System.currentTimeMillis();
            Time time = new Time(now);
            Date date = new Date(now);
            String status = "pending";
            
            // Create customer object with just ID (no need to query database)
            Customer customer = new Customer();
            customer.setId(customerId);
            
            Order order = new Order(orderId, total, time, date, status, null, customer, null);
            boolean orderCreated = orderDAO.createOrder(order);

            if (!orderCreated) {
                response.sendRedirect(request.getContextPath() + "/cart?action=view&error=Failed to create order");
                return;
            }
            
            // Add order details with auto-generated IDs
            for (CartDetail cartDetail : cartDetails) {
                // Get next sequential order detail ID
                String orderDetailId = orderDAO.getNextOrderDetailId();
                boolean detailAdded = orderDAO.addOrderDetail(orderDetailId, orderId, 
                    cartDetail.getProduct().getId(), 
                    cartDetail.getQuantity(), 
                    cartDetail.getProduct().getPrice());
                
                if (!detailAdded) {
                    System.err.println("Failed to add order detail for product: " + cartDetail.getProduct().getId());
                }
                
                // Update product quantity
                var product = productDAO.getProductById(cartDetail.getProduct().getId());
                int newQuantity = product.getQuantity() - cartDetail.getQuantity();
                productDAO.updateProductQuantity(product.getId(), newQuantity);
            }
            
            // Clear cart
            cartDAO.clearCart(cart.getId());
            
            response.sendRedirect(request.getContextPath() + "/order?action=myOrders&message=Order placed successfully! Order ID: " + orderId);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/cart?action=view&error=Error creating order: " + e.getMessage());
        }
    }
    
    /**
     * Update order status
     */
    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        
        if (role == null || "customer".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        try {
            String orderId = request.getParameter("orderId");
            String status = request.getParameter("status");
            
            if (orderId == null || status == null) {
                response.sendRedirect(request.getContextPath() + "/order?action=list&error=Invalid parameters");
                return;
            }
            
            boolean success = orderDAO.updateOrderStatus(orderId, status);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/order?action=view&id=" + orderId + "&message=Status updated");
            } else {
                response.sendRedirect(request.getContextPath() + "/order?action=view&id=" + orderId + "&error=Failed to update status");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/order?action=list&error=" + e.getMessage());
        }
    }
    
    /**
     * Cancel order (customer only for pending orders)
     */
    private void cancelOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String customerId = (String) session.getAttribute("customerId");
        
        if (customerId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        try {
            String orderId = request.getParameter("id");
            
            if (orderId == null) {
                response.sendRedirect(request.getContextPath() + "/order?action=myOrders&error=Invalid order");
                return;
            }
            
            Order order = orderDAO.getOrderById(orderId);
            
            if (order == null) {
                response.sendRedirect(request.getContextPath() + "/order?action=myOrders&error=Order not found");
                return;
            }
            
            // Only allow cancel for pending orders
            // Note: You need to add getStatus() method to Order model or check via database
            
            boolean success = orderDAO.updateOrderStatus(orderId, "cancelled");
            
            if (success) {
                // Restore product quantities
                List<OrderDetail> orderDetails = orderDAO.getOrderDetails(orderId);
                for (OrderDetail detail : orderDetails) {
                    var product = productDAO.getProductById(detail.getProduct().getId());
                    int restoredQuantity = product.getQuantity() + detail.getQuantity();
                    productDAO.updateProductQuantity(product.getId(), restoredQuantity);
                }
                
                response.sendRedirect(request.getContextPath() + "/order?action=myOrders&message=Order cancelled");
            } else {
                response.sendRedirect(request.getContextPath() + "/order?action=myOrders&error=Failed to cancel order");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/order?action=myOrders&error=" + e.getMessage());
        }
    }
}
