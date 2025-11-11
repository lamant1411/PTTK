package com.shopman.servlet;
import com.shopman.dao.CartDAO;
import com.shopman.dao.ProductDAO;
import com.shopman.model.Cart;
import com.shopman.model.CartDetail;
import com.shopman.model.Product;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
            default:
                viewCart(request, response);
                break;
        }
    }
    private void redirectWithMessage(HttpServletRequest request, HttpServletResponse response, String message) 
            throws IOException {
        try {
            String encodedMessage = URLEncoder.encode(message, "UTF-8");
            String referer = request.getHeader("Referer");
            if (referer != null && !referer.isEmpty()) {
                String separator = referer.contains("?") ? "&" : "?";
                response.sendRedirect(referer + separator + "message=" + encodedMessage);
            } else {
                response.sendRedirect(request.getContextPath() + "/product?action=list&message=" + encodedMessage);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/product?action=list");
        }
    }
    private void redirectWithError(HttpServletRequest request, HttpServletResponse response, String error) 
            throws IOException {
        try {
            String encodedError = URLEncoder.encode(error, "UTF-8");
            String referer = request.getHeader("Referer");
            if (referer != null && !referer.isEmpty()) {
                String separator = referer.contains("?") ? "&" : "?";
                response.sendRedirect(referer + separator + "error=" + encodedError);
            } else {
                response.sendRedirect(request.getContextPath() + "/product?action=list&error=" + encodedError);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/product?action=list");
        }
    }
    private void viewCart(HttpServletRequest request, HttpServletResponse response)
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
                String cartId = cartDAO.getNextCartId();
                boolean created = cartDAO.createCart(cartId, customerId);
                if (created) {
                    cart = cartDAO.getCartById(cartId);
                }
            }
            if (cart == null) {
                request.setAttribute("error", "Không thể tải giỏ hàng. Vui lòng thử lại");
                request.getRequestDispatcher("/customer/CartView.jsp").forward(request, response);
                return;
            }
            List<CartDetail> cartDetails = cartDAO.getCartDetails(cart.getId());
            float totalAmount = cartDAO.calculateCartTotal(cart.getId());
            request.setAttribute("cart", cart);
            request.setAttribute("cartDetails", cartDetails);
            request.setAttribute("totalAmount", totalAmount);
            request.setAttribute("itemCount", cartDetails.size());
            request.getRequestDispatcher("/customer/CartView.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải giỏ hàng: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
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
            int quantity = Integer.parseInt(quantityStr);
            Product product = productDAO.getProductById(productId);
            if (product.getQuantity() < quantity) {
                redirectWithError(request, response, "Không đủ hàng trong kho");
                return;
            }
            Cart cart = cartDAO.getCartByCustomerId(customerId);
            if (cart == null) {
                String cartId = cartDAO.getNextCartId();
                cartDAO.createCart(cartId, customerId);
                cart = cartDAO.getCartById(cartId);
            }
            CartDetail existingDetail = cartDAO.getCartDetailByProduct(cart.getId(), productId);
            if (existingDetail != null) {
                int newQuantity = existingDetail.getQuantity() + quantity;
                if (product.getQuantity() < newQuantity) {
                    redirectWithError(request, response, "Không đủ hàng trong kho");
                    return;
                }
                cartDAO.updateCartDetailQuantity(existingDetail.getId(), newQuantity);
            } else {
                String cartDetailId = cartDAO.getNextCartDetailId();
                cartDAO.addToCart(cartDetailId, cart.getId(), productId, quantity, product.getPrice());
            }
            redirectWithMessage(request, response, "Thêm vào giỏ hàng thành công");
        } catch (NumberFormatException e) {
            redirectWithError(request, response, "Định dạng số lượng không hợp lệ");
        } catch (Exception e) {
            e.printStackTrace();
            redirectWithError(request, response, "Lỗi khi thêm vào giỏ hàng");
        }
    }
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
                response.sendRedirect(request.getContextPath() + "/cart?action=view&error=Tham số không hợp lệ");
                return;
            }
            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                cartDAO.removeFromCart(cartDetailId);
                response.sendRedirect(request.getContextPath() + "/cart?action=view&message=Đã xóa sản phẩm khỏi giỏ hàng");
                return;
            }
            CartDetail cartDetail = cartDAO.getCartDetailById(cartDetailId);
            if (cartDetail == null) {
                response.sendRedirect(request.getContextPath() + "/cart?action=view&error=Không tìm thấy sản phẩm");
                return;
            }
            Product product = cartDetail.getProduct();
            if (product.getQuantity() < quantity) {
                response.sendRedirect(request.getContextPath() + "/cart?action=view&error=Không đủ hàng trong kho");
                return;
            }
            boolean success = cartDAO.updateCartDetailQuantity(cartDetailId, quantity);
            if (success) {
                response.sendRedirect(request.getContextPath() + "/cart?action=view&message=Cập nhật giỏ hàng thành công");
            } else {
                response.sendRedirect(request.getContextPath() + "/cart?action=view&error=Cập nhật giỏ hàng thất bại");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/cart?action=view&error=Định dạng số lượng không hợp lệ");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/cart?action=view&error=" + e.getMessage());
        }
    }
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
                response.sendRedirect(request.getContextPath() + "/cart?action=view&error=Tham số không hợp lệ");
                return;
            }
            boolean success = cartDAO.removeFromCart(cartDetailId);
            if (success) {
                response.sendRedirect(request.getContextPath() + "/cart?action=view&message=Đã xóa sản phẩm khỏi giỏ hàng");
            } else {
                response.sendRedirect(request.getContextPath() + "/cart?action=view&error=Xóa sản phẩm thất bại");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/cart?action=view&error=" + e.getMessage());
        }
    }
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
                response.sendRedirect(request.getContextPath() + "/cart?action=view&message=Đã xóa toàn bộ giỏ hàng");
            } else {
                response.sendRedirect(request.getContextPath() + "/cart?action=view&error=Xóa giỏ hàng thất bại");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/cart?action=view&error=" + e.getMessage());
        }
    }
}