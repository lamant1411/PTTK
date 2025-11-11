package com.shopman.servlet;

import com.shopman.dao.ProductDAO;
import com.shopman.model.Product;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for handling Product operations
 * Supports: list, view, add, edit, delete, search
 */
@WebServlet(name = "ProductServlet", urlPatterns = {"/product"})
public class ProductServlet extends HttpServlet {
    
    private ProductDAO productDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
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
                listProducts(request, response);
                break;
            case "view":
                viewProduct(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteProduct(request, response);
                break;
            case "search":
                searchProducts(request, response);
                break;
            default:
                listProducts(request, response);
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
            case "add":
                addProduct(request, response);
                break;
            case "edit":
                editProduct(request, response);
                break;
            case "updateQuantity":
                updateProductQuantity(request, response);
                break;
            default:
                listProducts(request, response);
                break;
        }
    }
    
    /**
     * List all products with pagination
     */
    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Get page parameter
            String pageParam = request.getParameter("page");
            int page = 1;
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
            }
            
            int recordsPerPage = 10;
            int offset = (page - 1) * recordsPerPage;
            
            // Get products with pagination
            List<Product> products = productDAO.getProductsWithPagination(offset, recordsPerPage);
            int totalProducts = productDAO.getTotalProducts();
            int totalPages = (int) Math.ceil((double) totalProducts / recordsPerPage);
            
            // Set attributes
            request.setAttribute("products", products);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalProducts", totalProducts);
            
            // Check user role and forward to appropriate page
            String role = (String) request.getSession().getAttribute("role");
            if ("customer".equals(role) || role == null) {
                // Customer or guest view
                request.getRequestDispatcher("/customer/product-list.jsp").forward(request, response);
            } else {
                // Manager/admin view
                request.getRequestDispatcher("/manager/product-list.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading products: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    /**
     * View single product details
     */
    private void viewProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String productId = request.getParameter("id");
        
        if (productId == null || productId.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/product?action=list");
            return;
        }
        
        try {
            Product product = productDAO.getProductById(productId);
            
            if (product != null) {
                request.setAttribute("product", product);
                
                // Check user role and forward to appropriate page
                String role = (String) request.getSession().getAttribute("role");
                if ("customer".equals(role) || role == null) {
                    // Customer or guest view
                    request.getRequestDispatcher("/customer/product-view.jsp").forward(request, response);
                } else {
                    // Manager/admin view
                    request.getRequestDispatcher("/manager/product-view.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("error", "Product not found");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading product: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    /**
     * Show add product form
     */
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Generate next product ID
        ProductDAO productDAO = new ProductDAO();
        String nextId = productDAO.getNextProductId();
        request.setAttribute("nextProductId", nextId);
        
        request.getRequestDispatcher("/manager/product-add.jsp").forward(request, response);
    }
    
    /**
     * Add new product
     */
    private void addProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String priceStr = request.getParameter("price");
            String quantityStr = request.getParameter("quantity");
            String description = request.getParameter("description");
            String unit = request.getParameter("unit");
            
            // Validation
            if (id == null || id.trim().isEmpty() ||
                name == null || name.trim().isEmpty() ||
                priceStr == null || priceStr.trim().isEmpty() ||
                quantityStr == null || quantityStr.trim().isEmpty() ||
                unit == null || unit.trim().isEmpty()) {
                
                request.setAttribute("error", "All required fields must be filled");
                ProductDAO productDAO = new ProductDAO();
                String nextId = productDAO.getNextProductId();
                request.setAttribute("nextProductId", nextId);
                request.getRequestDispatcher("/manager/product-add.jsp").forward(request, response);
                return;
            }
            
            float price = Float.parseFloat(priceStr);
            int quantity = Integer.parseInt(quantityStr);
            
            if (price < 0 || quantity < 0) {
                request.setAttribute("error", "Price and quantity must be positive numbers");
                ProductDAO productDAO = new ProductDAO();
                String nextId = productDAO.getNextProductId();
                request.setAttribute("nextProductId", nextId);
                request.getRequestDispatcher("/manager/product-add.jsp").forward(request, response);
                return;
            }
            
            Product product = new Product(id, name, price, quantity, description, unit);
            boolean success = productDAO.saveProduct(product);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/product?action=list&message=Product added successfully");
            } else {
                request.setAttribute("error", "Failed to add product");
                ProductDAO productDAO = new ProductDAO();
                String nextId = productDAO.getNextProductId();
                request.setAttribute("nextProductId", nextId);
                request.getRequestDispatcher("/manager/product-add.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid number format");
            request.getRequestDispatcher("/manager/product-add.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error adding product: " + e.getMessage());
            request.getRequestDispatcher("/manager/product-add.jsp").forward(request, response);
        }
    }
    
    /**
     * Show edit product form
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String productId = request.getParameter("id");
        
        if (productId == null || productId.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/product?action=list");
            return;
        }
        
        try {
            Product product = productDAO.getProductById(productId);
            
            if (product != null) {
                request.setAttribute("product", product);
                request.getRequestDispatcher("/manager/product-edit.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Product not found");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading product: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    /**
     * Edit existing product
     */
    private void editProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String priceStr = request.getParameter("price");
            String quantityStr = request.getParameter("quantity");
            String description = request.getParameter("description");
            
            // Validation
            if (id == null || id.trim().isEmpty() ||
                name == null || name.trim().isEmpty() ||
                priceStr == null || priceStr.trim().isEmpty() ||
                quantityStr == null || quantityStr.trim().isEmpty()) {
                
                request.setAttribute("error", "All required fields must be filled");
                Product product = productDAO.getProductById(id);
                request.setAttribute("product", product);
                request.getRequestDispatcher("/manager/product-edit.jsp").forward(request, response);
                return;
            }
            
            float price = Float.parseFloat(priceStr);
            int quantity = Integer.parseInt(quantityStr);
            
            if (price < 0 || quantity < 0) {
                request.setAttribute("error", "Price and quantity must be positive numbers");
                Product product = productDAO.getProductById(id);
                request.setAttribute("product", product);
                request.getRequestDispatcher("/manager/product-edit.jsp").forward(request, response);
                return;
            }
            
            Product product = new Product(id, name, price, quantity, description);
            boolean success = productDAO.updateProduct(product);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/product?action=list&message=Product updated successfully");
            } else {
                request.setAttribute("error", "Failed to update product");
                request.setAttribute("product", product);
                request.getRequestDispatcher("/manager/product-edit.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid number format");
            request.getRequestDispatcher("/manager/product-edit.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error updating product: " + e.getMessage());
            request.getRequestDispatcher("/manager/product-edit.jsp").forward(request, response);
        }
    }
    
    /**
     * Delete product
     */
    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String productId = request.getParameter("id");
        
        if (productId == null || productId.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/product?action=list");
            return;
        }
        
        try {
            boolean success = productDAO.deleteProduct(productId);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/product?action=list&message=Product deleted successfully");
            } else {
                response.sendRedirect(request.getContextPath() + "/product?action=list&error=Failed to delete product");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/product?action=list&error=" + e.getMessage());
        }
    }
    
    /**
     * Search products by name
     */
    private void searchProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String keyword = request.getParameter("keyword");
        
        if (keyword == null || keyword.trim().isEmpty()) {
            listProducts(request, response);
            return;
        }
        
        try {
            List<Product> products = productDAO.searchProductsByName(keyword);
            
            request.setAttribute("products", products);
            request.setAttribute("keyword", keyword);
            request.setAttribute("searchResults", true);
            
            request.getRequestDispatcher("/manager/product-list.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error searching products: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    /**
     * Update product quantity (AJAX endpoint)
     */
    private void updateProductQuantity(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            String productId = request.getParameter("productId");
            String quantityStr = request.getParameter("quantity");
            
            if (productId == null || quantityStr == null) {
                response.getWriter().write("{\"success\": false, \"message\": \"Missing parameters\"}");
                return;
            }
            
            int quantity = Integer.parseInt(quantityStr);
            boolean success = productDAO.updateProductQuantity(productId, quantity);
            
            if (success) {
                response.getWriter().write("{\"success\": true, \"message\": \"Quantity updated\"}");
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Failed to update quantity\"}");
            }
            
        } catch (NumberFormatException e) {
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid quantity format\"}");
        } catch (Exception e) {
            response.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}
