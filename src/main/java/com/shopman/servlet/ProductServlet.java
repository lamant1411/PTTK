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
    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String pageParam = request.getParameter("page");
            int page = 1;
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
            }
            int recordsPerPage = 5;
            int offset = (page - 1) * recordsPerPage;
            List<Product> products = productDAO.getProductsWithPagination(offset, recordsPerPage);
            int totalProducts = productDAO.getTotalProducts();
            int totalPages = (int) Math.ceil((double) totalProducts / recordsPerPage);
            request.setAttribute("products", products);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalProducts", totalProducts);
            String role = (String) request.getSession().getAttribute("role");
            if ("customer".equals(role) || role == null) {
                request.getRequestDispatcher("/customer/ProductListView.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/manager/ManageProductView.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải danh sách sản phẩm: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
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
                String role = (String) request.getSession().getAttribute("role");
                if ("customer".equals(role) || role == null) {
                    request.getRequestDispatcher("/customer/ProductView.jsp").forward(request, response);
                } else {
                    request.getRequestDispatcher("/manager/ProductView.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("error", "Không tìm thấy sản phẩm");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải sản phẩm: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ProductDAO productDAO = new ProductDAO();
        String nextId = productDAO.getNextProductId();
        request.setAttribute("nextProductId", nextId);
        request.getRequestDispatcher("/manager/AddProductView.jsp").forward(request, response);
    }
    private void addProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String priceStr = request.getParameter("price");
            String quantityStr = request.getParameter("quantity");
            String description = request.getParameter("description");
            String unit = request.getParameter("unit");
            if (id == null || id.trim().isEmpty() ||
                name == null || name.trim().isEmpty() ||
                priceStr == null || priceStr.trim().isEmpty() ||
                quantityStr == null || quantityStr.trim().isEmpty() ||
                unit == null || unit.trim().isEmpty()) {
                request.setAttribute("error", "Vui lòng điền đầy đủ các trường bắt buộc");
                ProductDAO productDAO = new ProductDAO();
                String nextId = productDAO.getNextProductId();
                request.setAttribute("nextProductId", nextId);
                request.getRequestDispatcher("/manager/AddProductView.jsp").forward(request, response);
                return;
            }
            float price = Float.parseFloat(priceStr);
            int quantity = Integer.parseInt(quantityStr);
            if (price < 0 || quantity < 0) {
                request.setAttribute("error", "Giá và số lượng phải là số dương");
                ProductDAO productDAO = new ProductDAO();
                String nextId = productDAO.getNextProductId();
                request.setAttribute("nextProductId", nextId);
                request.getRequestDispatcher("/manager/AddProductView.jsp").forward(request, response);
                return;
            }
            Product product = new Product(id, name, price, quantity, description, unit);
            boolean success = productDAO.saveProduct(product);
            if (success) {
                response.sendRedirect(request.getContextPath() + "/product?action=list&message=Thêm sản phẩm thành công");
            } else {
                request.setAttribute("error", "Thêm sản phẩm thất bại");
                ProductDAO productDAO = new ProductDAO();
                String nextId = productDAO.getNextProductId();
                request.setAttribute("nextProductId", nextId);
                request.getRequestDispatcher("/manager/AddProductView.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Định dạng số không hợp lệ");
            request.getRequestDispatcher("/manager/AddProductView.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi thêm sản phẩm: " + e.getMessage());
            request.getRequestDispatcher("/manager/AddProductView.jsp").forward(request, response);
        }
    }
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
                request.setAttribute("error", "Không tìm thấy sản phẩm");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải sản phẩm: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    private void editProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String priceStr = request.getParameter("price");
            String quantityStr = request.getParameter("quantity");
            String description = request.getParameter("description");
            if (id == null || id.trim().isEmpty() ||
                name == null || name.trim().isEmpty() ||
                priceStr == null || priceStr.trim().isEmpty() ||
                quantityStr == null || quantityStr.trim().isEmpty()) {
                request.setAttribute("error", "Vui lòng điền đầy đủ các trường bắt buộc");
                Product product = productDAO.getProductById(id);
                request.setAttribute("product", product);
                request.getRequestDispatcher("/manager/product-edit.jsp").forward(request, response);
                return;
            }
            float price = Float.parseFloat(priceStr);
            int quantity = Integer.parseInt(quantityStr);
            if (price < 0 || quantity < 0) {
                request.setAttribute("error", "Giá và số lượng phải là số dương");
                Product product = productDAO.getProductById(id);
                request.setAttribute("product", product);
                request.getRequestDispatcher("/manager/product-edit.jsp").forward(request, response);
                return;
            }
            Product product = new Product(id, name, price, quantity, description);
            boolean success = productDAO.updateProduct(product);
            if (success) {
                response.sendRedirect(request.getContextPath() + "/product?action=list&message=Cập nhật sản phẩm thành công");
            } else {
                request.setAttribute("error", "Cập nhật sản phẩm thất bại");
                request.setAttribute("product", product);
                request.getRequestDispatcher("/manager/product-edit.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Định dạng số không hợp lệ");
            request.getRequestDispatcher("/manager/product-edit.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi cập nhật sản phẩm: " + e.getMessage());
            request.getRequestDispatcher("/manager/product-edit.jsp").forward(request, response);
        }
    }
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
                response.sendRedirect(request.getContextPath() + "/product?action=list&message=Xóa sản phẩm thành công");
            } else {
                response.sendRedirect(request.getContextPath() + "/product?action=list&error=Xóa sản phẩm thất bại");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/product?action=list&error=" + e.getMessage());
        }
    }
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
            String role = (String) request.getSession().getAttribute("role");
            if ("customer".equals(role)) {
                request.getRequestDispatcher("/customer/ProductListView.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/manager/ManageProductView.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error searching products: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
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