<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.shopman.dao.ProductDAO" %>
<%@ page import="com.shopman.model.Product" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
    <style>
        .alert {
            padding: 12px 16px;
            margin-bottom: 20px;
            border-radius: 6px;
            font-size: 14px;
            animation: slideDown 0.3s ease-out;
        }
        
        .alert-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
            border-left: 4px solid #28a745;
        }
        
        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
            border-left: 4px solid #dc3545;
        }
        
        @keyframes slideDown {
            from {
                opacity: 0;
                transform: translateY(-10px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
        
        @keyframes slideUp {
            from {
                opacity: 1;
                transform: translateY(0);
            }
            to {
                opacity: 0;
                transform: translateY(-10px);
            }
        }
        
        .product-detail-container {
            max-width: 800px;
            margin: 20px auto;
            background: white;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            padding: 30px;
        }
        
        .product-title {
            font-size: 28px;
            font-weight: 600;
            color: #333;
            margin-bottom: 12px;
            line-height: 1.3;
        }
        
        .product-meta {
            display: flex;
            align-items: center;
            gap: 15px;
            margin-bottom: 20px;
            padding-bottom: 20px;
            border-bottom: 2px solid #f5f5f5;
        }
        
        .product-id {
            font-size: 14px;
            color: #999;
        }
        
        .product-stock .stock {
            display: inline-block;
            padding: 6px 12px;
            border-radius: 4px;
            font-size: 14px;
            font-weight: 500;
        }
        
        .product-stock .in-stock {
            background: #E8F5E9;
            color: #2E7D32;
        }
        
        .product-stock .out-stock {
            background: #FFEBEE;
            color: #C62828;
        }
        
        .product-price-section {
            margin: 20px 0;
        }
        
        .price-label {
            font-size: 14px;
            color: #666;
            margin-bottom: 5px;
            display: block;
        }
        
        .price-value {
            font-size: 32px;
            font-weight: bold;
            color: #4CAF50;
        }
        
        .product-description-section {
            margin: 25px 0;
        }
        
        .product-description-section h3 {
            font-size: 18px;
            font-weight: 600;
            color: #333;
            margin-bottom: 10px;
        }
        
        .product-description-section p {
            font-size: 15px;
            color: #666;
            line-height: 1.6;
        }
        
        .add-to-cart-section {
            margin-top: 30px;
            padding-top: 25px;
            border-top: 2px solid #f5f5f5;
        }
        
        .quantity-selector {
            margin-bottom: 20px;
        }
        
        .quantity-selector label {
            display: block;
            font-size: 14px;
            font-weight: 500;
            color: #333;
            margin-bottom: 8px;
        }
        
        .quantity-controls {
            display: inline-flex;
            align-items: center;
            gap: 0;
            border: 1px solid #ddd;
            border-radius: 4px;
            overflow: hidden;
        }
        
        .qty-btn {
            width: 40px;
            height: 40px;
            background: #f5f5f5;
            color: #333;
            border: none;
            cursor: pointer;
            font-size: 18px;
            font-weight: bold;
            transition: background 0.2s;
        }
        
        .qty-btn:hover {
            background: #e0e0e0;
        }
        
        .qty-input {
            width: 70px;
            height: 40px;
            text-align: center;
            border: none;
            border-left: 1px solid #ddd;
            border-right: 1px solid #ddd;
            font-size: 16px;
            font-weight: 500;
        }
        
        .btn-add-to-cart {
            width: 100%;
            padding: 14px;
            background: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: background 0.2s;
        }
        
        .btn-add-to-cart:hover {
            background: #388E3C;
        }
        
        .out-of-stock-message {
            padding: 15px;
            background: #FFEBEE;
            color: #C62828;
            border-radius: 4px;
            text-align: center;
            font-weight: 500;
        }
    </style>
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar">
        <div class="nav-container">
            <a href="${pageContext.request.contextPath}/product?action=list" class="logo">🛒 ShopMan</a>
            
            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/product?action=list">Sản phẩm</a>
                <a href="${pageContext.request.contextPath}/cart?action=view" class="cart-link">
                    Giỏ hàng
                </a>
                <a href="${pageContext.request.contextPath}/order?action=myOrders">Đơn hàng</a>
                <span class="user-name">👤 ${sessionScope.username}</span>
                <a href="${pageContext.request.contextPath}/auth?action=logout" class="btn-logout">Đăng xuất</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <!-- Nút quay lại -->
        <div class="back-navigation">
            <a href="${pageContext.request.contextPath}/product?action=list" class="btn-back">← Quay lại danh sách</a>
        </div>

        <!-- Thông báo thành công/lỗi -->
        <c:if test="${not empty param.message}">
            <div class="alert alert-success">${param.message}</div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-error">${param.error}</div>
        </c:if>

        <c:if test="${not empty product}">
            <div class="product-detail-container">
                <h1 class="product-title">${product.name}</h1>
                
                <div class="product-meta">
                    <span class="product-id">Mã sản phẩm: ${product.id}</span>
                    <div class="product-stock">
                        <c:choose>
                            <c:when test="${product.quantity > 0}">
                                <span class="stock in-stock">Còn hàng: ${product.quantity} ${product.unit != null ? product.unit : ''}</span>
                            </c:when>
                            <c:otherwise>
                                <span class="stock out-stock">Hết hàng</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="product-price-section">
                    <span class="price-label">Giá bán</span>
                    <span class="price-value"><fmt:formatNumber value="${product.price}" pattern="#,###"/>₫</span>
                </div>

                <div class="product-description-section">
                    <h3>Mô tả sản phẩm</h3>
                    <p>${product.des != null ? product.des : 'Chưa có mô tả cho sản phẩm này.'}</p>
                </div>

                <!-- Form thêm vào giỏ hàng -->
                <c:choose>
                    <c:when test="${product.quantity > 0}">
                        <form action="${pageContext.request.contextPath}/cart" method="post" class="add-to-cart-section">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="productId" value="${product.id}">
                            
                            <div class="quantity-selector">
                                <label for="quantity">Số lượng:</label>
                                <div class="quantity-controls">
                                    <button type="button" class="qty-btn" onclick="decreaseQty()">−</button>
                                    <input type="number" id="quantity" name="quantity" value="1" 
                                           min="1" max="${product.quantity}" class="qty-input">
                                    <button type="button" class="qty-btn" onclick="increaseQty()">+</button>
                                </div>
                            </div>

                            <button type="submit" class="btn-add-to-cart">🛒 Thêm vào giỏ hàng</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <div class="out-of-stock-message">
                            Sản phẩm hiện đã hết hàng
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>

        <c:if test="${empty product}">
            <div class="error-message">
                <h2>Không tìm thấy sản phẩm</h2>
                <p>Sản phẩm bạn đang tìm không tồn tại.</p>
                <a href="${pageContext.request.contextPath}/product?action=list" class="btn-primary">Quay lại danh sách sản phẩm</a>
            </div>
        </c:if>
    </div>

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

        // Tự động ẩn thông báo sau 3 giây
        window.addEventListener('DOMContentLoaded', function() {
            var alerts = document.querySelectorAll('.alert');
            alerts.forEach(function(alert) {
                setTimeout(function() {
                    alert.style.animation = 'slideUp 0.3s ease-in';
                    setTimeout(function() {
                        alert.style.display = 'none';
                    }, 300);
                }, 3000);
            });
        });
    </script>
</body>
</html>
