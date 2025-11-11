<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sản phẩm - ShopMan</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/customer/static/css/customer.css">
    <style>
        .page-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 20px;
            margin-bottom: 15px;
        }
        
        .page-title {
            margin: 0;
            flex-shrink: 0;
        }
        
        .search-container {
            flex: 1;
            max-width: 500px;
            margin: 0;
        }
        
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
        
        .products-table-container {
            background: white;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            padding: 15px;
            margin-top: 15px;
        }
        
        .products-table {
            width: 100%;
            border-collapse: collapse;
        }
        
        .products-table thead {
            background: #f8f9fa;
            border-bottom: 2px solid #dee2e6;
        }
        
        .products-table th {
            padding: 8px 10px;
            text-align: left;
            font-weight: 600;
            font-size: 13px;
            color: #333;
        }
        
        .products-table th.text-center {
            text-align: center;
        }
        
        .products-table th.text-right {
            text-align: right;
        }
        
        .products-table tbody tr {
            border-bottom: 1px solid #e9ecef;
            transition: background 0.2s;
        }
        
        .products-table tbody tr:hover {
            background: #f8f9fa;
        }
        
        .products-table td {
            padding: 10px 10px;
            font-size: 13px;
            vertical-align: middle;
        }
        
        .product-name-cell {
            max-width: 300px;
        }
        
        .product-name {
            font-weight: 600;
            color: #333;
            margin-bottom: 3px;
            font-size: 14px;
        }
        
        .product-id {
            font-size: 11px;
            color: #999;
        }
        
        .product-description {
            font-size: 12px;
            color: #666;
            line-height: 1.3;
            margin-top: 3px;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
        }
        
        .product-price {
            font-size: 16px;
            font-weight: bold;
            color: #4CAF50;
            white-space: nowrap;
        }
        
        .stock {
            display: inline-block;
            padding: 3px 8px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: 500;
            white-space: nowrap;
        }
        
        .in-stock {
            background: #E8F5E9;
            color: #2E7D32;
        }
        
        .out-stock {
            background: #FFEBEE;
            color: #C62828;
        }
        
        .action-buttons {
            display: flex;
            gap: 6px;
            justify-content: center;
            align-items: stretch;
        }
        
        .btn-view {
            background: #2196F3;
            color: white;
            padding: 6px 10px;
            text-align: center;
            text-decoration: none;
            border-radius: 4px;
            font-weight: 500;
            font-size: 12px;
            transition: background 0.2s;
            white-space: nowrap;
            display: inline-flex;
            align-items: center;
            justify-content: center;
        }
        
        .btn-view:hover {
            background: #1976D2;
        }
        
        .add-to-cart-form {
            flex: 1;
            margin: 0;
            display: flex;
        }
        
        .btn-add-cart { 
            background: #4CAF50;
            color: white;
            padding: 6px 10px;
            border: none;
            border-radius: 4px;
            font-weight: 500;
            font-size: 12px;
            cursor: pointer;
            transition: background 0.2s;
            white-space: nowrap;
        }
        
        .btn-add-cart:hover {
            background: #388E3C;
        }
        
        .btn-add-cart:disabled {
            background: #ccc;
            cursor: not-allowed;
        }
        
        @media (max-width: 768px) {
            .products-table-container {
                overflow-x: auto;
            }
            
            .products-table {
                min-width: 600px;
            }
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
        <!-- Header với tiêu đề và tìm kiếm -->
        <div class="page-header">
            <h1 class="page-title">Danh sách sản phẩm</h1>
            
            <!-- Thanh tìm kiếm -->
            <div class="search-container">
                <form action="${pageContext.request.contextPath}/product" method="get" class="search-form">
                    <input type="hidden" name="action" value="search">
                    <input type="text" name="keyword" placeholder="Tìm kiếm sản phẩm..." value="${keyword}">
                    <button type="submit" class="btn-search">🔍 Tìm kiếm</button>
                </form>
            </div>
        </div>

        <!-- Thông báo -->
        <c:if test="${not empty param.message}">
            <div class="alert alert-success">${param.message}</div>
        </c:if>

        <!-- Products Grid -->
        <div class="products-table-container">
            <c:choose>
                <c:when test="${not empty products}">
                    <table class="products-table">
                        <thead>
                            <tr>
                                <th>Sản phẩm</th>
                                <th class="text-right">Giá</th>
                                <th class="text-center">Tình trạng</th>
                                <th class="text-center">Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="product" items="${products}">
                                <tr>
                                    <td class="product-name-cell">
                                        <div class="product-name">${product.name}</div>
                                        <div class="product-id">Mã: ${product.id}</div>
                                        <div class="product-description">${product.des != null ? product.des : 'Chưa có mô tả'}</div>
                                    </td>
                                    
                                    <td style="text-align: right;">
                                        <span class="product-price"><fmt:formatNumber value="${product.price}" pattern="#,###"/>₫</span>
                                    </td>
                                    
                                    <td style="text-align: center;">
                                        <c:choose>
                                            <c:when test="${product.quantity > 0}">
                                                <span class="stock in-stock">Còn: ${product.quantity} ${product.unit != null ? product.unit : ''}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="stock out-stock">Hết hàng</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    
                                    <td>
                                        <div class="action-buttons">
                                            <a href="${pageContext.request.contextPath}/customer/ProductView.jsp?id=${product.id}" 
                                               class="btn-view">Chi tiết</a>
                                            
                                            <c:choose>
                                                <c:when test="${product.quantity > 0}">
                                                    <form action="${pageContext.request.contextPath}/cart" method="post" class="add-to-cart-form">
                                                        <input type="hidden" name="action" value="add">
                                                        <input type="hidden" name="productId" value="${product.id}">
                                                        <input type="hidden" name="quantity" value="1">
                                                        <button type="submit" class="btn-add-cart">+ Giỏ hàng</button>
                                                    </form>
                                                </c:when>
                                                <c:otherwise>
                                                    <button class="btn-add-cart" disabled>Hết hàng</button>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="no-products">
                        <p>Không tìm thấy sản phẩm</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Phân trang -->
        <c:if test="${not empty totalPages and totalPages > 1}">
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/product?action=list&page=${currentPage - 1}">← Trang trước</a>
                </c:if>
                
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="${pageContext.request.contextPath}/product?action=list&page=${i}" 
                       class="${currentPage == i ? 'active' : ''}">${i}</a>
                </c:forEach>
                
                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/product?action=list&page=${currentPage + 1}">Trang sau →</a>
                </c:if>
            </div>
        </c:if>
    </div>

    <script>
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
