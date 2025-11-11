<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    // Kiểm tra đăng nhập và quyền manager
    String role = (String) session.getAttribute("role");
    if (role == null || !"manager".equals(role)) {
        response.sendRedirect(request.getContextPath() + "/auth?action=login");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bảng điều khiển - ShopMan</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/manager/static/css/manager.css">
</head>
<body>
    <div class="container">
        <!-- Tiêu đề -->
        <header class="header">
            <h1>📦 Quản lý sản phẩm</h1>
            <div class="user-info">
                <a href="${pageContext.request.contextPath}/auth?action=logout" class="btn-logout">Đăng xuất</a>
            </div>
        </header>

        <!-- Các thẻ điều hướng -->
        <div class="dashboard-grid">
            <!-- Thẻ thống kê -->
            <div class="dashboard-card">
                <h2>Thống kê</h2>
                <a href="${pageContext.request.contextPath}/manager/statistics.jsp" class="btn-primary">Xem thống kê</a>
            </div>

            <!-- Thẻ quản lý sản phẩm -->
            <div class="dashboard-card">
                <h2>Quản lý sản phẩm</h2>
                <a href="${pageContext.request.contextPath}/product?action=list" class="btn-primary">Quản lý sản phẩm</a>
            </div>

            <!-- Thẻ nhập hàng -->
            <div class="dashboard-card">
                <h2>Nhập hàng</h2>
                <a href="${pageContext.request.contextPath}/manager/import.jsp" class="btn-primary">Nhập hàng</a>
            </div>

            <!-- Thẻ quản lý nhà cung cấp -->
            <div class="dashboard-card">
                <h2>Quản lý nhà cung cấp</h2>
                <a href="${pageContext.request.contextPath}/manager/suppliers.jsp" class="btn-primary">Quản lý nhà cung cấp</a>
            </div>
        </div>
    </div>
</body>
</html>
