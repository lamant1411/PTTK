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
    <title>Đơn hàng của tôi - ShopMan</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/customer/static/css/customer.css">
    <style>
        .orders-container {
            max-width: 1200px;
            margin: 30px auto;
            padding: 20px;
        }
        
        .page-title {
            font-size: 28px;
            color: #333;
            margin-bottom: 30px;
        }
        
        .order-card {
            background: white;
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.05);
        }
        
        .order-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding-bottom: 15px;
            border-bottom: 2px solid #f0f0f0;
            margin-bottom: 15px;
        }
        
        .order-id {
            font-size: 18px;
            font-weight: 600;
            color: #333;
        }
        
        .order-date {
            color: #666;
            font-size: 14px;
        }
        
        .order-status {
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 13px;
            font-weight: 600;
        }
        
        .order-status.pending {
            background: #fff3cd;
            color: #856404;
        }
        
        .order-status.processing {
            background: #cce5ff;
            color: #004085;
        }
        
        .order-status.shipping {
            background: #d1ecf1;
            color: #0c5460;
        }
        
        .order-status.completed {
            background: #d4edda;
            color: #155724;
        }
        
        .order-status.cancelled {
            background: #f8d7da;
            color: #721c24;
        }
        
        .order-body {
            padding: 15px 0;
        }
        
        .order-total {
            font-size: 20px;
            font-weight: 700;
            color: #4CAF50;
            margin: 10px 0;
        }
        
        .order-actions {
            display: flex;
            gap: 10px;
            margin-top: 15px;
            padding-top: 15px;
            border-top: 1px solid #f0f0f0;
        }
        
        .btn {
            padding: 8px 20px;
            border-radius: 5px;
            text-decoration: none;
            font-size: 14px;
            font-weight: 500;
            transition: all 0.3s;
        }
        
        .btn-view {
            background: #007bff;
            color: white;
        }
        
        .btn-view:hover {
            background: #0056b3;
        }
        
        .btn-cancel {
            background: #dc3545;
            color: white;
        }
        
        .btn-cancel:hover {
            background: #c82333;
        }
        
        .empty-orders {
            text-align: center;
            padding: 60px 20px;
            background: white;
            border-radius: 8px;
        }
        
        .empty-orders-icon {
            font-size: 80px;
            margin-bottom: 20px;
        }
        
        .empty-orders h2 {
            color: #666;
            margin-bottom: 10px;
        }
        
        .empty-orders p {
            color: #999;
            margin-bottom: 30px;
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
                <a href="${pageContext.request.contextPath}/order?action=myOrders" class="active">Đơn hàng</a>
                <span class="user-name">👤 ${sessionScope.username}</span>
                <a href="${pageContext.request.contextPath}/auth?action=logout" class="btn-logout">Đăng xuất</a>
            </div>
        </div>
    </nav>

    <div class="orders-container">
        <h1 class="page-title">Đơn hàng của tôi</h1>

        <!-- Thông báo -->
        <c:if test="${not empty param.message}">
            <div class="alert alert-success">${param.message}</div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-error">${param.error}</div>
        </c:if>

        <c:choose>
            <c:when test="${not empty orders and orders.size() > 0}">
                <c:forEach var="order" items="${orders}">
                    <div class="order-card">
                        <div class="order-header">
                            <div>
                                <div class="order-id">Đơn hàng #${order.id}</div>
                                <div class="order-date">
                                    <fmt:formatDate value="${order.date}" pattern="dd/MM/yyyy"/> 
                                    <fmt:formatDate value="${order.time}" pattern="HH:mm"/>
                                </div>
                            </div>
                            <div>
                                <span class="order-status ${order.status}">
                                    <c:choose>
                                        <c:when test="${order.status == 'pending'}">⏳ Chờ xử lý</c:when>
                                        <c:when test="${order.status == 'processing'}">⚙️ Đang xử lý</c:when>
                                        <c:when test="${order.status == 'shipping'}">🚚 Đang giao</c:when>
                                        <c:when test="${order.status == 'completed'}">✓ Hoàn thành</c:when>
                                        <c:when test="${order.status == 'cancelled'}">✗ Đã hủy</c:when>
                                        <c:otherwise>${order.status}</c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                        </div>
                        
                        <div class="order-body">
                            <div class="order-total">
                                Tổng tiền: <fmt:formatNumber value="${order.total}" pattern="#,###"/>₫
                            </div>
                        </div>
                        
                        <div class="order-actions">
                            <a href="${pageContext.request.contextPath}/order?action=view&id=${order.id}" 
                               class="btn btn-view">Xem chi tiết</a>
                            
                            <c:if test="${order.status == 'pending'}">
                                <a href="${pageContext.request.contextPath}/order?action=cancel&id=${order.id}" 
                                   class="btn btn-cancel"
                                   onclick="return confirm('Bạn có chắc muốn hủy đơn hàng này?')">
                                   Hủy đơn hàng
                                </a>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
                
                <div class="order-summary">
                    <p>Tổng số đơn hàng: <strong>${totalOrders}</strong></p>
                </div>
            </c:when>
            
            <c:otherwise>
                <div class="empty-orders">
                    <div class="empty-orders-icon">📦</div>
                    <h2>Chưa có đơn hàng nào</h2>
                    <p>Bạn chưa đặt hàng</p>
                    <a href="${pageContext.request.contextPath}/product?action=list" class="btn-primary">
                        Bắt đầu mua sắm
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>
