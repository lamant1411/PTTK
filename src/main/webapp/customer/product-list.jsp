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
        .products-table-container {
            background: white;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            padding: 20px;
            margin-top: 20px;
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
            padding: 12px;
            text-align: left;
            font-weight: 600;
            font-size: 14px;
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
            padding: 15px 12px;
            font-size: 14px;
            vertical-align: middle;
        }
        
        .product-name-cell {
            max-width: 300px;
        }
        
        .product-name {
            font-weight: 600;
            color: #333;
            margin-bottom: 4px;
            font-size: 15px;
        }
        
        .product-id {
            font-size: 12px;
            color: #999;
        }
        
        .product-description {
            font-size: 13px;
            color: #666;
            line-height: 1.4;
            margin-top: 4px;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
        }
        
        .product-price {
            font-size: 18px;
            font-weight: bold;
            color: #4CAF50;
            white-space: nowrap;
        }
        
        .stock {
            display: inline-block;
            padding: 4px 10px;
            border-radius: 4px;
            font-size: 13px;
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
        }
        
        .btn-view {
            background: #2196F3;
            color: white;
            padding: 8px 16px;
            text-align: center;
            text-decoration: none;
            border-radius: 4px;
            font-weight: 500;
            font-size: 13px;
            transition: background 0.2s;
            white-space: nowrap;
        }
        
        .btn-view:hover {
            background: #1976D2;
        }
        
        .add-to-cart-form {
            margin: 0;
        }
        
        .btn-add-cart {
            background: #4CAF50;
            color: white;
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            font-weight: 500;
            font-size: 13px;
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
        <h1 class="page-title">Danh sách sản phẩm</h1>

        <!-- Thông báo -->
        <c:if test="${not empty param.message}">
            <div class="alert alert-success">${param.message}</div>
        </c:if>

        <!-- Thanh tìm kiếm -->
        <div class="search-container">
            <form action="${pageContext.request.contextPath}/product" method="get" class="search-form">
                <input type="hidden" name="action" value="search">
                <input type="text" name="keyword" placeholder="Tìm kiếm sản phẩm..." value="${keyword}">
                <button type="submit" class="btn-search">🔍 Tìm kiếm</button>
            </form>
        </div>

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
                                            <a href="${pageContext.request.contextPath}/customer/product-view.jsp?id=${product.id}" 
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
</body>
</html>
