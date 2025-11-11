<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manager Dashboard - ShopMan</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/manager/static/css/manager.css">
</head>
<body>
    <div class="container">
        <!-- Header -->
        <header class="header">
            <h1>ShopMan - Manager Dashboard</h1>
            <div class="user-info">
                <span>Welcome, ${sessionScope.username}</span>
                <a href="${pageContext.request.contextPath}/auth?action=logout" class="btn-logout">Logout</a>
            </div>
        </header>

        <!-- Navigation Cards -->
        <div class="dashboard-grid">
            <!-- Statistics Card -->
            <div class="dashboard-card">
                <div class="card-icon">📊</div>
                <h2>Statistics</h2>
                <p>View sales and inventory statistics</p>
                <a href="${pageContext.request.contextPath}/manager/statistics.jsp" class="btn-primary">View Statistics</a>
            </div>

            <!-- Manage Products Card -->
            <div class="dashboard-card">
                <div class="card-icon">📦</div>
                <h2>Manage Products</h2>
                <p>Add, edit, delete products</p>
                <a href="${pageContext.request.contextPath}/product?action=list" class="btn-primary">Manage Products</a>
            </div>

            <!-- Import Products Card -->
            <div class="dashboard-card">
                <div class="card-icon">📥</div>
                <h2>Import Products</h2>
                <p>Import products from suppliers</p>
                <a href="${pageContext.request.contextPath}/manager/import.jsp" class="btn-primary">Import</a>
            </div>

            <!-- Manage Suppliers Card -->
            <div class="dashboard-card">
                <div class="card-icon">🏢</div>
                <h2>Manage Suppliers</h2>
                <p>View and manage suppliers</p>
                <a href="${pageContext.request.contextPath}/manager/suppliers.jsp" class="btn-primary">Manage Suppliers</a>
            </div>
        </div>

        <!-- Quick Stats -->
        <div class="quick-stats">
            <h3>Quick Overview</h3>
            <div class="stats-row">
                <div class="stat-box">
                    <div class="stat-number">0</div>
                    <div class="stat-label">Total Products</div>
                </div>
                <div class="stat-box">
                    <div class="stat-number">0</div>
                    <div class="stat-label">Total Orders</div>
                </div>
                <div class="stat-box">
                    <div class="stat-number">0</div>
                    <div class="stat-label">Suppliers</div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
