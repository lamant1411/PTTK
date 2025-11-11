package com.shopman.servlet;

import com.shopman.dao.CartDAO;
import com.shopman.dao.ProductDAO;
import com.shopman.model.Cart;
import com.shopman.model.CartDetail;
import com.shopman.model.Product;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet for handling Cart operations
 * Supports: view cart, add to cart, update quantity, remove from cart, clear cart
 */
@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {
    
    private CartDAO cartDAO;
    private ProductDAO productDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        cartDAO = new CartDAO();
        productDAO = new ProductDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "view";
        }
        
        switch (action) {
            case "view":
                viewCart(request, response);
                break;
            case "remove":
                removeFromCart(request, response);
                break;
            case "clear":
                clearCart(request, response);
                break;
            default:
                viewCart(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "view";
        }
        
        switch (action) {
            case "add":
                addToCart(request, response);
                break;
            case "update":
                updateCartQuantity(request, response);
                break;
            case "addAjax":
                addToCartAjax(request, response);
                break;
            default:
                viewCart(request, response);
                break;
        }
    }
    
    /**
     * View shopping cart
     */
    private void viewCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String customerId = (String) session.getAttribute("customerId");
        
        if (customerId == null) {
            // Not logged in, redirect to login
            response.sendRedirect(request.getContextPath() + "/login.jsp?redirect=cart");
            return;
        }
        
        try {
            // Get or create cart for customer
            Cart cart = cartDAO.getCartByCustomerId(customerId);
            
            if (cart == null) {
                // Create new cart with auto-increment ID
                String cartId = cartDAO.getNextCartId();
                boolean created = cartDAO.createCart(cartId, customerId);
                
                if (created) {
                    cart = cartDAO.getCartById(cartId);
                }
            }
            
            // Check if cart was successfully created/retrieved
            if (cart == null) {
                request.setAttribute("error", "Unable to load cart. Please try again.");
                request.getRequestDispatcher("/customer/CartView.jsp").forward(request, response);
                return;
            }
            
            // Get cart details
            List<CartDetail> cartDetails = cartDAO.getCartDetails(cart.getId());
            float totalAmount = cartDAO.calculateCartTotal(cart.getId());
            
            // Set attributes
            request.setAttribute("cart", cart);
            request.setAttribute("cartDetails", cartDetails);
            request.setAttribute("totalAmount", totalAmount);
            request.setAttribute("itemCount", cartDetails.size());
            
            // Forward to JSP
            request.getRequestDispatcher("/customer/CartView.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading cart: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    /**
     * Add product to cart
     */
    private void addToCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String customerId = (String) session.getAttribute("customerId");
        
        if (customerId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?redirect=cart");
            return;
        }
        
        try {
            String productId = request.getParameter("productId");
            String quantityStr = request.getParameter("quantity");
            
            if (productId == null || quantityStr == null) {
                request.setAttribute("error", "Invalid parameters");
                viewCart(request, response);
                return;
            }
            
            int quantity = Integer.parseInt(quantityStr);
            
            if (quantity <= 0) {
                request.setAttribute("error", "Quantity must be greater than 0");
                viewCart(request, response);
                return;
            }
            
            // Get product
            Product product = productDAO.getProductById(productId);
            
            if (product == null) {
                request.setAttribute("error", "Product not found");
                viewCart(request, response);
                return;
            }
            
            // Check stock
            if (product.getQuantity() < quantity) {
                request.setAttribute("error", "Not enough stock available");
                viewCart(request, response);
                return;
            }
            
            // Get or create cart
            Cart cart = cartDAO.getCartByCustomerId(customerId);
            
            if (cart == null) {
                String cartId = "CART" + System.currentTimeMillis();
                cartDAO.createCart(cartId, customerId);
                cart = cartDAO.getCartById(cartId);
            }
            
            // Check if product already in cart
            CartDetail existingDetail = cartDAO.getCartDetailByProduct(cart.getId(), productId);
            
            if (existingDetail != null) {
                // Update quantity
                int newQuantity = existingDetail.getQuantity() + quantity;
                
                if (product.getQuantity() < newQuantity) {
                    request.setAttribute("error", "Not enough stock available");
                    viewCart(request, response);
                    return;
                }
                
                cartDAO.updateCartDetailQuantity(existingDetail.getId(), newQuantity);
            } else {
                // Add new item
                String cartDetailId = cartDAO.getNextCartDetailId();
                cartDAO.addToCart(cartDetailId, cart.getId(), productId, quantity, product.getPrice());
            }
            
            // Redirect back to the referring page with success message
            String referer = request.getHeader("Referer");
            if (referer != null && !referer.isEmpty()) {
                String separator = referer.contains("?") ? "&" : "?";
                response.sendRedirect(referer + separator + "message=Product added to cart successfully");
            } else {
                response.sendRedirect(request.getContextPath() + "/product?action=list&message=Product added to cart successfully");
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid quantity format");
            viewCart(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error adding to cart: " + e.getMessage());
            viewCart(request, response);
        }
    }
    
    /**
     * Add to cart via AJAX
     */
    private void addToCartAjax(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        String customerId = (String) session.getAttribute("customerId");
        
        if (customerId == null) {
            response.getWriter().write("{\"success\": false, \"message\": \"Please login first\"}");
            return;
        }
        
        try {
            String productId = request.getParameter("productId");
            String quantityStr = request.getParameter("quantity");
            
            if (productId == null || quantityStr == null) {
                response.getWriter().write("{\"success\": false, \"message\": \"Invalid parameters\"}");
                return;
            }
            
            int quantity = Integer.parseInt(quantityStr);
            
            if (quantity <= 0) {
                response.getWriter().write("{\"success\": false, \"message\": \"Quantity must be greater than 0\"}");
                return;
            }
            
            Product product = productDAO.getProductById(productId);
            
            if (product == null) {
                response.getWriter().write("{\"success\": false, \"message\": \"Product not found\"}");
                return;
            }
            
            if (product.getQuantity() < quantity) {
                response.getWriter().write("{\"success\": false, \"message\": \"Not enough stock\"}");
                return;
            }
            
            Cart cart = cartDAO.getCartByCustomerId(customerId);
            
            if (cart == null) {
                String cartId = "CART" + System.currentTimeMillis();
                cartDAO.createCart(cartId, customerId);
                cart = cartDAO.getCartById(cartId);
            }
            
            CartDetail existingDetail = cartDAO.getCartDetailByProduct(cart.getId(), productId);
            
            if (existingDetail != null) {
                int newQuantity = existingDetail.getQuantity() + quantity;
                
                if (product.getQuantity() < newQuantity) {
                    response.getWriter().write("{\"success\": false, \"message\": \"Not enough stock\"}");
                    return;
                }
                
                cartDAO.updateCartDetailQuantity(existingDetail.getId(), newQuantity);
            } else {
                String cartDetailId = cartDAO.getNextCartDetailId();
                cartDAO.addToCart(cartDetailId, cart.getId(), productId, quantity, product.getPrice());
            }
            
            // Get updated cart count
            List<CartDetail> cartDetails = cartDAO.getCartDetails(cart.getId());
            
            response.getWriter().write("{\"success\": true, \"message\": \"Added to cart\", \"cartCount\": " + cartDetails.size() + "}");
            
        } catch (Exception e) {
            response.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
    
    /**
     * Update cart item quantity
     */
    private void updateCartQuantity(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String customerId = (String) session.getAttribute("customerId");
        
        if (customerId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?redirect=cart");
            return;
        }
        
        try {
            String cartDetailId = request.getParameter("cartDetailId");
            String quantityStr = request.getParameter("quantity");
            
            if (cartDetailId == null || quantityStr == null) {
                response.sendRedirect(request.getContextPath() + "/cart?action=view&error=Invalid parameters");
                return;
            }
            
            int quantity = Integer.parseInt(quantityStr);
            
            if (quantity <= 0) {
                // Remove item if quantity is 0 or negative
                cartDAO.removeFromCart(cartDetailId);
                response.sendRedirect(request.getContextPath() + "/cart?action=view&message=Item removed from cart");
                return;
            }
            
            // Get cart detail to check product stock
            CartDetail cartDetail = cartDAO.getCartDetailById(cartDetailId);
            
            if (cartDetail == null) {
                response.sendRedirect(request.getContextPath() + "/cart?action=view&error=Item not found");
                return;
            }
            
            Product product = cartDetail.getProduct();
            
            if (product.getQuantity() < quantity) {
                response.sendRedirect(request.getContextPath() + "/cart?action=view&error=Not enough stock available");
                return;
            }
            
            boolean success = cartDAO.updateCartDetailQuantity(cartDetailId, quantity);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/cart?action=view&message=Cart updated");
            } else {
                response.sendRedirect(request.getContextPath() + "/cart?action=view&error=Failed to update cart");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/cart?action=view&error=Invalid quantity format");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/cart?action=view&error=" + e.getMessage());
        }
    }
    
    /**
     * Remove item from cart
     */
    private void removeFromCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String customerId = (String) session.getAttribute("customerId");
        
        if (customerId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?redirect=cart");
            return;
        }
        
        try {
            String cartDetailId = request.getParameter("cartDetailId");
            
            if (cartDetailId == null) {
                response.sendRedirect(request.getContextPath() + "/cart?action=view&error=Invalid parameters");
                return;
            }
            
            boolean success = cartDAO.removeFromCart(cartDetailId);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/cart?action=view&message=Item removed from cart");
            } else {
                response.sendRedirect(request.getContextPath() + "/cart?action=view&error=Failed to remove item");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/cart?action=view&error=" + e.getMessage());
        }
    }
    
    /**
     * Clear all items from cart
     */
    private void clearCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String customerId = (String) session.getAttribute("customerId");
        
        if (customerId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?redirect=cart");
            return;
        }
        
        try {
            Cart cart = cartDAO.getCartByCustomerId(customerId);
            
            if (cart == null) {
                response.sendRedirect(request.getContextPath() + "/cart?action=view");
                return;
            }
            
            boolean success = cartDAO.clearCart(cart.getId());
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/cart?action=view&message=Cart cleared");
            } else {
                response.sendRedirect(request.getContextPath() + "/cart?action=view&error=Failed to clear cart");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/cart?action=view&error=" + e.getMessage());
        }
    }
}
