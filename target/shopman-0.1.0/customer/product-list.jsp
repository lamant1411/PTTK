<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Products - ShopMan</title>
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
                    🛒 Cart <span class="cart-badge" id="cartCount">0</span>
                </a>
                <a href="${pageContext.request.contextPath}/order?action=myOrders">My Orders</a>
                <span class="user-name">👤 ${sessionScope.username}</span>
                <a href="${pageContext.request.contextPath}/auth?action=logout" class="btn-logout">Logout</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <h1 class="page-title">Our Products</h1>

        <!-- Success Message -->
        <c:if test="${not empty param.message}">
            <div class="alert alert-success">${param.message}</div>
        </c:if>

        <!-- Search Bar -->
        <div class="search-container">
            <form action="${pageContext.request.contextPath}/product" method="get" class="search-form">
                <input type="hidden" name="action" value="search">
                <input type="text" name="keyword" placeholder="Search products..." value="${keyword}">
                <button type="submit" class="btn-search">🔍 Search</button>
            </form>
        </div>

        <!-- Products Grid -->
        <div class="products-grid">
            <c:choose>
                <c:when test="${not empty products}">
                    <c:forEach var="product" items="${products}">
                        <div class="product-card">
                            <div class="product-image">📦</div>
                            
                            <div class="product-info">
                                <h3 class="product-name">${product.name}</h3>
                                <p class="product-price">$${product.price}</p>
                                
                                <div class="product-stock">
                                    <c:choose>
                                        <c:when test="${product.quantity > 10}">
                                            <span class="stock in-stock">✓ In Stock (${product.quantity})</span>
                                        </c:when>
                                        <c:when test="${product.quantity > 0}">
                                            <span class="stock low-stock">⚠ Low Stock (${product.quantity})</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="stock out-stock">✗ Out of Stock</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                
                                <p class="product-description">${product.des}</p>
                            </div>
                            
                            <div class="product-actions">
                                <a href="${pageContext.request.contextPath}/customer/product-view.jsp?id=${product.id}" 
                                   class="btn-view">View Details</a>
                                
                                <c:if test="${product.quantity > 0}">
                                    <form action="${pageContext.request.contextPath}/cart" method="post" class="add-to-cart-form">
                                        <input type="hidden" name="action" value="add">
                                        <input type="hidden" name="productId" value="${product.id}">
                                        <input type="hidden" name="quantity" value="1">
                                        <button type="submit" class="btn-add-cart">+ Add to Cart</button>
                                    </form>
                                </c:if>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="no-products">
                        <p>No products found</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Pagination -->
        <c:if test="${not empty totalPages and totalPages > 1}">
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/product?action=list&page=${currentPage - 1}">← Previous</a>
                </c:if>
                
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="${pageContext.request.contextPath}/product?action=list&page=${i}" 
                       class="${currentPage == i ? 'active' : ''}">${i}</a>
                </c:forEach>
                
                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/product?action=list&page=${currentPage + 1}">Next →</a>
                </c:if>
            </div>
        </c:if>
    </div>

    <!-- Footer -->
    <footer class="footer">
        <p>&copy; 2025 ShopMan. All rights reserved.</p>
    </footer>
</body>
</html>
