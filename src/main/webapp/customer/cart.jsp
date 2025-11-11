<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    // Check if user is logged in and has customer role
    String role = (String) session.getAttribute("role");
    if (role == null || !"customer".equals(role)) {
        response.sendRedirect(request.getContextPath() + "/auth?action=login");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Giỏ hàng - ShopMan</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/customer/static/css/customer.css">
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar">
        <div class="nav-container">
            <a href="${pageContext.request.contextPath}/product?action=list" class="logo">🛒 ShopMan</a>
            
            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/product?action=list">Sản phẩm</a>
                <a href="${pageContext.request.contextPath}/cart?action=view" class="cart-link active">
                    Giỏ hàng
                </a>
                <a href="${pageContext.request.contextPath}/order?action=myOrders">Đơn hàng</a>
                <span class="user-name">👤 ${sessionScope.username}</span>
                <a href="${pageContext.request.contextPath}/auth?action=logout" class="btn-logout">Đăng xuất</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <h1 class="page-title">Giỏ hàng của bạn</h1>

        <!-- Thông báo -->
        <c:if test="${not empty param.message}">
            <div class="alert alert-success">${param.message}</div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-error">${param.error}</div>
        </c:if>

        <c:choose>
            <c:when test="${not empty cartDetails and cartDetails.size() > 0}">
                <!-- Cart Table -->
                <div class="cart-container">
                    <table class="cart-table">
                        <thead>
                            <tr>
                                <th>Sản phẩm</th>
                                <th>Giá</th>
                                <th>Số lượng</th>
                                <th>Thành tiền</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${cartDetails}">
                                <tr>
                                    <td class="product-info">
                                        <div class="product-name">
                                            <strong>${item.product.name}</strong>
                                        </div>
                                        <div class="product-id">ID: ${item.product.id}</div>
                                    </td>
                                    
                                    <td class="price">
                                        <fmt:formatNumber value="${item.product.price}" pattern="#,###"/>₫
                                    </td>
                                    
                                    <td class="quantity-cell">
                                        <form action="${pageContext.request.contextPath}/cart" method="post" class="quantity-form">
                                            <input type="hidden" name="action" value="update">
                                            <input type="hidden" name="cartDetailId" value="${item.id}">
                                            
                                            <div class="quantity-controls-inline">
                                                <input type="number" name="quantity" value="${item.quantity}" 
                                                       min="0" max="${item.product.quantity}" class="qty-input-small">
                                                <button type="submit" class="btn-update">Cập nhật</button>
                                            </div>
                                        </form>
                                        <small class="stock-info">Tối đa: ${item.product.quantity}</small>
                                    </td>
                                    
                                    <td class="subtotal">
                                        <fmt:formatNumber value="${item.product.price * item.quantity}" pattern="#,###"/>₫
                                    </td>
                                    
                                    <td class="actions-cell">
                                        <a href="${pageContext.request.contextPath}/cart?action=remove&cartDetailId=${item.id}" 
                                           class="btn-remove"
                                           onclick="return confirm('Xóa sản phẩm này khỏi giỏ hàng?')">
                                            🗑️ Xóa
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <!-- Tổng kết giỏ hàng -->
                    <div class="cart-summary">
                        <div class="summary-row">
                            <span class="summary-label">Tổng sản phẩm:</span>
                            <span class="summary-value">${itemCount}</span>
                        </div>
                        <div class="summary-row total-row">
                            <span class="summary-label">Tổng tiền:</span>
                            <span class="summary-value total-amount">
                                <fmt:formatNumber value="${totalAmount}" pattern="#,###"/>₫
                            </span>
                        </div>
                    </div>

                    <!-- Các hành động -->
                    <div class="cart-actions">
                        <a href="${pageContext.request.contextPath}/product?action=list" class="btn-continue">
                            ← Tiếp tục mua sắm
                        </a>
                        
                        <a href="${pageContext.request.contextPath}/cart?action=clear" 
                           class="btn-clear"
                           onclick="return confirm('Xóa toàn bộ giỏ hàng?')">
                            Xóa giỏ hàng
                        </a>
                        
                        <form action="${pageContext.request.contextPath}/order" method="post" style="display: inline;">
                            <input type="hidden" name="action" value="create">
                            <button type="submit" class="btn-checkout" onclick="return confirm('Đặt hàng ngay?')">
                                ✓ Đặt hàng
                            </button>
                        </form>
                    </div>
                </div>
            </c:when>
            
            <c:otherwise>
                <!-- Giỏ hàng trống -->
                <div class="empty-cart">
                    <div class="empty-cart-icon">🛒</div>
                    <h2>Giỏ hàng của bạn đang trống</h2>
                    <p>Thêm sản phẩm vào giỏ hàng để xem tại đây</p>
                    <a href="${pageContext.request.contextPath}/product?action=list" class="btn-primary">
                        Bắt đầu mua sắm
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>
