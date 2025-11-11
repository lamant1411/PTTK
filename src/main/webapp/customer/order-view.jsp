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
    <title>Chi tiết đơn hàng - ShopMan</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/customer/static/css/customer.css">
    <style>
        .container {
            max-width: 1000px;
            margin: 0 auto;
            padding: 15px;
        }
        
        .page-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
        
        .page-title {
            font-size: 20px;
            margin: 0;
        }
        
        .order-id-badge {
            background: #333;
            color: white;
            padding: 6px 12px;
            border-radius: 3px;
            font-size: 13px;
            font-weight: 500;
        }
        
        .order-content {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 15px;
        }
        
        .order-info-section, .order-items-section {
            background: white;
            border: 1px solid #ddd;
            border-radius: 5px;
            padding: 15px;
        }
        
        .order-items-section {
            grid-column: 1 / -1;
        }
        
        .section-title {
            font-size: 14px;
            font-weight: 600;
            color: #333;
            margin: 0 0 12px 0;
            padding-bottom: 8px;
            border-bottom: 2px solid #e0e0e0;
        }
        
        .info-grid {
            display: grid;
            gap: 10px;
        }
        
        .info-row {
            display: flex;
            justify-content: space-between;
            padding: 6px 0;
            border-bottom: 1px solid #f0f0f0;
        }
        
        .info-row:last-child {
            border-bottom: none;
        }
        
        .info-label {
            font-size: 13px;
            color: #666;
            font-weight: 500;
        }
        
        .info-value {
            font-size: 13px;
            color: #333;
            font-weight: 600;
            text-align: right;
        }
        
        .order-status {
            display: inline-block;
            padding: 3px 10px;
            border-radius: 3px;
            font-size: 12px;
            font-weight: 600;
        }
        
        .status-pending {
            background: #fff3cd;
            color: #856404;
        }
        
        .status-processing {
            background: #cfe2ff;
            color: #084298;
        }
        
        .status-completed {
            background: #d1e7dd;
            color: #0f5132;
        }
        
        .status-cancelled {
            background: #f8d7da;
            color: #842029;
        }
        
        .order-items-table {
            width: 100%;
            border-collapse: collapse;
            font-size: 13px;
        }
        
        .order-items-table th {
            background: #f5f5f5;
            color: #333;
            padding: 8px;
            text-align: left;
            font-weight: 600;
            border-bottom: 2px solid #ddd;
        }
        
        .order-items-table td {
            padding: 8px;
            border-bottom: 1px solid #e0e0e0;
        }
        
        .order-items-table tr:last-child td {
            border-bottom: none;
        }
        
        .product-name {
            font-weight: 500;
            color: #333;
            font-size: 13px;
        }
        
        .order-summary {
            background: #f9f9f9;
            padding: 12px;
            border-radius: 3px;
            margin-top: 10px;
        }
        
        .summary-row {
            display: flex;
            justify-content: space-between;
            padding: 6px 0;
            font-size: 13px;
        }
        
        .summary-row:last-child {
            border-top: 2px solid #ddd;
            font-size: 15px;
            font-weight: 600;
            color: green;
            padding-top: 8px;
            margin-top: 6px;
        }
        
        .btn-back {
            display: inline-block;
            padding: 8px 16px;
            background: #333;
            color: white;
            text-decoration: none;
            border-radius: 3px;
            margin-top: 15px;
            font-size: 13px;
        }
        
        .btn-back:hover {
            background: #555;
        }
        
        @media print {
            .navbar, .btn-back { display: none; }
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
                <a href="${pageContext.request.contextPath}/cart?action=view" class="cart-link">Giỏ hàng</a>
                <a href="${pageContext.request.contextPath}/order?action=myOrders" class="active">Đơn hàng</a>
                <span class="user-name">👤 ${sessionScope.username}</span>
                <a href="${pageContext.request.contextPath}/auth?action=logout" class="btn-logout">Đăng xuất</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="page-header">
            <h1 class="page-title">Chi tiết đơn hàng</h1>
            <span class="order-id-badge">#${order.id}</span>
        </div>

        <div class="order-content">
            <!-- Thông tin đơn hàng -->
            <div class="order-info-section">
                <h3 class="section-title">Thông tin đơn hàng</h3>
                <div class="info-grid">
                    <div class="info-row">
                        <span class="info-label">Ngày đặt:</span>
                        <span class="info-value"><fmt:formatDate value="${order.date}" pattern="dd/MM/yyyy HH:mm"/></span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Trạng thái:</span>
                        <span class="info-value">
                            <c:choose>
                                <c:when test="${order.status == 'Pending' || order.status == 'pending'}">
                                    <span class="order-status status-pending">⏳ Chờ xử lý</span>
                                </c:when>
                                <c:when test="${order.status == 'Processing' || order.status == 'processing'}">
                                    <span class="order-status status-processing">🔄 Đang xử lý</span>
                                </c:when>
                                <c:when test="${order.status == 'Completed' || order.status == 'completed'}">
                                    <span class="order-status status-completed">✅ Hoàn thành</span>
                                </c:when>
                                <c:when test="${order.status == 'Cancelled' || order.status == 'cancelled'}">
                                    <span class="order-status status-cancelled">❌ Đã hủy</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="order-status status-pending">${order.status}</span>
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Tổng tiền:</span>
                        <span class="info-value" style="color: green; font-size: 16px;">
                            <fmt:formatNumber value="${order.total}" pattern="#,###"/>₫
                        </span>
                    </div>
                </div>
            </div>

            <!-- Thông tin giao hàng -->
            <div class="order-info-section">
                <h3 class="section-title">Thông tin giao hàng</h3>
                <div class="info-grid">
                    <div class="info-row">
                        <span class="info-label">Người nhận:</span>
                        <span class="info-value">${order.customer.name}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">SĐT:</span>
                        <span class="info-value">${order.customer.phone}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Địa chỉ:</span>
                        <span class="info-value">${order.customer.add}</span>
                    </div>
                </div>
            </div>

            <!-- Danh sách sản phẩm -->
            <div class="order-items-section">
                <h3 class="section-title">Sản phẩm đã đặt</h3>
                
                <!-- Debug info -->
                <c:if test="${empty orderDetails}">
                    <p style="color: red; padding: 10px;">Không có sản phẩm trong đơn hàng hoặc orderDetails = null</p>
                </c:if>
                
                <table class="order-items-table">
                    <thead>
                        <tr>
                            <th>Sản phẩm</th>
                            <th style="text-align: right;">Đơn giá</th>
                            <th style="text-align: center;">SL</th>
                            <th style="text-align: right;">Thành tiền</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="calculatedTotal" value="0" />
                        <c:forEach var="item" items="${orderDetails}">
                            <tr>
                                <td>
                                    <div class="product-name">${item.product.name}</div>
                                </td>
                                <td style="text-align: right;"><fmt:formatNumber value="${item.price}" pattern="#,###"/>₫</td>
                                <td style="text-align: center;">${item.quantity}</td>
                                <td style="text-align: right;"><fmt:formatNumber value="${item.price * item.quantity}" pattern="#,###"/>₫</td>
                            </tr>
                            <c:set var="calculatedTotal" value="${calculatedTotal + (item.price * item.quantity)}" />
                        </c:forEach>
                        
                        <!-- Show message if no items -->
                        <c:if test="${empty orderDetails}">
                            <tr>
                                <td colspan="4" style="text-align: center; padding: 20px; color: #999;">
                                    Chưa có sản phẩm trong đơn hàng
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>

                <!-- Tổng kết -->
                <div class="order-summary">
                    <div class="summary-row">
                        <span>Tạm tính:</span>
                        <span><fmt:formatNumber value="${calculatedTotal}" pattern="#,###"/>₫</span>
                    </div>
                    <div class="summary-row">
                        <span>Phí ship:</span>
                        <span>Miễn phí</span>
                    </div>
                    <div class="summary-row">
                        <span>Tổng cộng:</span>
                        <span><fmt:formatNumber value="${order.total}" pattern="#,###"/>₫</span>
                    </div>
                </div>
            </div>
        </div>

        <a href="${pageContext.request.contextPath}/order?action=myOrders" class="btn-back">← Quay lại</a>
    </div>
</body>
</html>
