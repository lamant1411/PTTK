<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.shopman.dao.ProductDAO" %>
<%@ page import="com.shopman.model.Product" %>
<%
    String productId = request.getParameter("id");
    ProductDAO productDAO = new ProductDAO();
    Product product = productDAO.getProductById(productId);
    request.setAttribute("product", product);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${product.name} - ShopMan</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/customer/static/css/customer.css">
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar">
        <div class="nav-container">
            <a href="${pageContext.request.contextPath}/customer/home.jsp" class="logo">🛒 ShopMan</a>
            
            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/product?action=list">Products</a>
                <a href="${pageContext.request.contextPath}/cart?action=view" class="cart-link">
                    🛒 Cart <span class="cart-badge">0</span>
                </a>
                <a href="${pageContext.request.contextPath}/order?action=myOrders">My Orders</a>
                <span class="user-name">👤 ${sessionScope.username}</span>
                <a href="${pageContext.request.contextPath}/auth?action=logout" class="btn-logout">Logout</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <!-- Back Button -->
        <div class="back-navigation">
            <a href="${pageContext.request.contextPath}/product?action=list" class="btn-back">← Back to Products</a>
        </div>

        <c:if test="${not empty product}">
            <div class="product-detail">
                <!-- Product Image -->
                <div class="product-detail-image">
                    <div class="image-placeholder">📦</div>
                </div>

                <!-- Product Information -->
                <div class="product-detail-info">
                    <h1 class="product-title">${product.name}</h1>
                    
                    <div class="product-meta">
                        <span class="product-id">Product ID: ${product.id}</span>
                    </div>

                    <div class="product-price-section">
                        <span class="price-label">Price:</span>
                        <span class="price-value">$${product.price}</span>
                    </div>

                    <div class="product-stock-section">
                        <c:choose>
                            <c:when test="${product.quantity > 10}">
                                <span class="stock-status in-stock">✓ In Stock</span>
                                <span class="stock-quantity">${product.quantity} units available</span>
                            </c:when>
                            <c:when test="${product.quantity > 0}">
                                <span class="stock-status low-stock">⚠ Low Stock</span>
                                <span class="stock-quantity">Only ${product.quantity} left</span>
                            </c:when>
                            <c:otherwise>
                                <span class="stock-status out-stock">✗ Out of Stock</span>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="product-description-section">
                        <h3>Description</h3>
                        <p>${product.des != null ? product.des : 'No description available'}</p>
                    </div>

                    <!-- Add to Cart Form -->
                    <c:if test="${product.quantity > 0}">
                        <form action="${pageContext.request.contextPath}/cart" method="post" class="add-to-cart-section">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="productId" value="${product.id}">
                            
                            <div class="quantity-selector">
                                <label for="quantity">Quantity:</label>
                                <div class="quantity-controls">
                                    <button type="button" class="qty-btn" onclick="decreaseQty()">−</button>
                                    <input type="number" id="quantity" name="quantity" value="1" 
                                           min="1" max="${product.quantity}" class="qty-input">
                                    <button type="button" class="qty-btn" onclick="increaseQty()">+</button>
                                </div>
                            </div>

                            <button type="submit" class="btn-add-to-cart">🛒 Add to Cart</button>
                        </form>
                    </c:if>
                </div>
            </div>
        </c:if>

        <c:if test="${empty product}">
            <div class="error-message">
                <h2>Product not found</h2>
                <p>The product you're looking for doesn't exist.</p>
                <a href="${pageContext.request.contextPath}/product?action=list" class="btn-primary">Back to Products</a>
            </div>
        </c:if>
    </div>

    <!-- Footer -->
    <footer class="footer">
        <p>&copy; 2025 ShopMan. All rights reserved.</p>
    </footer>

    <script>
        function decreaseQty() {
            var input = document.getElementById('quantity');
            var value = parseInt(input.value);
            if (value > 1) {
                input.value = value - 1;
            }
        }

        function increaseQty() {
            var input = document.getElementById('quantity');
            var max = parseInt(input.getAttribute('max'));
            var value = parseInt(input.value);
            if (value < max) {
                input.value = value + 1;
            }
        }
    </script>
</body>
</html>
