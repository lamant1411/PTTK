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
    <title>Quản lý sản phẩm - ShopMan</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/manager/static/css/manager.css">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: Arial, sans-serif;
            background: #f5f5f5;
            padding: 20px;
        }
        
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 5px;
        }
        
        .actions-bar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            gap: 10px;
        }
        
        .btn-primary {
            background: #28a745;
            color: white;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 3px;
            font-weight: bold;
        }
        
        .search-form {
            display: flex;
            gap: 5px;
        }
        
        .search-form input {
            padding: 8px 12px;
            border: 1px solid #ddd;
            border-radius: 3px;
            width: 250px;
        }
        
        .btn-search {
            background: #007bff;
            color: white;
            padding: 8px 20px;
            border: none;
            border-radius: 3px;
            cursor: pointer;
        }
        
        .table-container {
            overflow-x: auto;
            margin-bottom: 20px;
        }
        
        .product-table {
            width: 100%;
            border-collapse: collapse;
            border: 1px solid #ddd;
        }
        
        .product-table thead {
            background: #f8f9fa;
        }
        
        .product-table th {
            padding: 12px;
            text-align: left;
            border: 1px solid #ddd;
            font-weight: bold;
            color: #333;
        }
        
        .product-table td {
            padding: 10px 12px;
            border: 1px solid #ddd;
        }
        
        .product-table tbody tr:hover {
            background: #f5f5f5;
        }
        
        .actions {
            display: flex;
            gap: 5px;
        }
        
        .btn-edit {
            background: #28a745;
            color: white;
            padding: 5px 12px;
            text-decoration: none;
            border-radius: 3px;
            font-size: 13px;
        }
        
        .btn-delete {
            background: #dc3545;
            color: white;
            padding: 5px 12px;
            text-decoration: none;
            border-radius: 3px;
            font-size: 13px;
        }
        
        .no-data {
            text-align: center;
            padding: 30px;
            color: #999;
        }
        
        .summary {
            margin-top: 15px;
            padding-top: 15px;
            border-top: 1px solid #ddd;
            color: #666;
        }
        
        .alert {
            padding: 12px;
            margin-bottom: 15px;
            border-radius: 3px;
        }
        
        .alert-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        
        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- Tiêu đề -->
        <header class="header">
            <h1>📦 Quản lý sản phẩm</h1>
            <div class="user-info">
                <a href="${pageContext.request.contextPath}/product?action=list" class="btn-back">← Quay lại danh sách</a>
                <a href="${pageContext.request.contextPath}/auth?action=logout" class="btn-logout">Đăng xuất</a>
            </div>
        </header>

        <!-- Thông báo thành công/lỗi -->
        <c:if test="${not empty param.message}">
            <div class="alert alert-success">${param.message}</div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-error">${param.error}</div>
        </c:if>

        <!-- Thanh công cụ -->
        <div class="actions-bar">
            <a href="${pageContext.request.contextPath}/product?action=add" class="btn-primary">+ Thêm sản phẩm mới</a>
            
            <!-- Form tìm kiếm -->
            <form action="${pageContext.request.contextPath}/product" method="get" class="search-form">
                <input type="hidden" name="action" value="search">
                <input type="text" name="keyword" placeholder="Tìm kiếm sản phẩm..." value="${keyword}">
                <button type="submit" class="btn-search">Tìm kiếm</button>
            </form>
        </div>

        <!-- Bảng sản phẩm -->
        <div class="table-container">
            <table class="product-table">
                <thead>
                    <tr>
                        <th>Mã SP</th>
                        <th>Tên sản phẩm</th>
                        <th>Mô tả</th>
                        <th>Giá (₫)</th>
                        <th>Số lượng</th>
                        <th>Đơn vị</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty products}">
                            <c:forEach var="product" items="${products}">
                                <tr>
                                    <td>${product.id}</td>
                                    <td>${product.name}</td>
                                    <td>${product.des}</td>
                                    <td>${product.price}</td>
                                    <td>${product.quantity}</td>
                                    <td>${product.unit}</td>
                                    <td class="actions">
                                        <a href="${pageContext.request.contextPath}/product?action=edit&id=${product.id}" 
                                           class="btn-edit">Sửa</a>
                                        <a href="${pageContext.request.contextPath}/product?action=delete&id=${product.id}" 
                                           class="btn-delete" 
                                           onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này?')">Xóa</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="7" class="no-data">Không tìm thấy sản phẩm</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>

        <!-- Phân trang -->
        <c:if test="${not empty totalPages and totalPages > 1}">
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/product?action=list&page=${currentPage - 1}" class="page-link">← Trang trước</a>
                </c:if>
                
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="${pageContext.request.contextPath}/product?action=list&page=${i}" 
                       class="page-link ${currentPage == i ? 'active' : ''}">${i}</a>
                </c:forEach>
                
                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/product?action=list&page=${currentPage + 1}" class="page-link">Trang sau →</a>
                </c:if>
            </div>
        </c:if>
    </div>
</body>
</html>
