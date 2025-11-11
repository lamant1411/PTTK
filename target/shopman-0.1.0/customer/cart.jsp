<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart - ShopMan</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/customer/static/css/customer.css">
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar">
        <div class="nav-container">
            <a href="${pageContext.request.contextPath}/customer/home.jsp" class="logo">🛒 ShopMan</a>
            
            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/product?action=list">Products</a>
                <a href="${pageContext.request.contextPath}/cart?action=view" class="cart-link active">
                    🛒 Cart <span class="cart-badge">${itemCount}</span>
                </a>
                <a href="${pageContext.request.contextPath}/order?action=myOrders">My Orders</a>
                <span class="user-name">👤 ${sessionScope.username}</span>
                <a href="${pageContext.request.contextPath}/auth?action=logout" class="btn-logout">Logout</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <h1 class="page-title">Shopping Cart</h1>

        <!-- Messages -->
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
                                <th>Product</th>
                                <th>Price</th>
                                <th>Quantity</th>
                                <th>Subtotal</th>
                                <th>Actions</th>
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
                                        $<fmt:formatNumber value="${item.product.price}" pattern="#,##0.00"/>
                                    </td>
                                    
                                    <td class="quantity-cell">
                                        <form action="${pageContext.request.contextPath}/cart" method="post" class="quantity-form">
                                            <input type="hidden" name="action" value="update">
                                            <input type="hidden" name="cartDetailId" value="${item.id}">
                                            
                                            <div class="quantity-controls-inline">
                                                <input type="number" name="quantity" value="${item.quantity}" 
                                                       min="0" max="${item.product.quantity}" class="qty-input-small">
                                                <button type="submit" class="btn-update">Update</button>
                                            </div>
                                        </form>
                                        <small class="stock-info">Max: ${item.product.quantity}</small>
                                    </td>
                                    
                                    <td class="subtotal">
                                        $<fmt:formatNumber value="${item.product.price * item.quantity}" pattern="#,##0.00"/>
                                    </td>
                                    
                                    <td class="actions-cell">
                                        <a href="${pageContext.request.contextPath}/cart?action=remove&cartDetailId=${item.id}" 
                                           class="btn-remove"
                                           onclick="return confirm('Remove this item from cart?')">
                                            🗑️ Remove
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <!-- Cart Summary -->
                    <div class="cart-summary">
                        <div class="summary-row">
                            <span class="summary-label">Total Items:</span>
                            <span class="summary-value">${itemCount}</span>
                        </div>
                        <div class="summary-row total-row">
                            <span class="summary-label">Total Amount:</span>
                            <span class="summary-value total-amount">
                                $<fmt:formatNumber value="${totalAmount}" pattern="#,##0.00"/>
                            </span>
                        </div>
                    </div>

                    <!-- Cart Actions -->
                    <div class="cart-actions">
                        <a href="${pageContext.request.contextPath}/product?action=list" class="btn-continue">
                            ← Continue Shopping
                        </a>
                        
                        <a href="${pageContext.request.contextPath}/cart?action=clear" 
                           class="btn-clear"
                           onclick="return confirm('Clear all items from cart?')">
                            Clear Cart
                        </a>
                        
                        <a href="${pageContext.request.contextPath}/order?action=checkout" class="btn-checkout">
                            Proceed to Checkout →
                        </a>
                    </div>
                </div>
            </c:when>
            
            <c:otherwise>
                <!-- Empty Cart -->
                <div class="empty-cart">
                    <div class="empty-cart-icon">🛒</div>
                    <h2>Your cart is empty</h2>
                    <p>Add some products to your cart to see them here</p>
                    <a href="${pageContext.request.contextPath}/product?action=list" class="btn-primary">
                        Start Shopping
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Footer -->
    <footer class="footer">
        <p>&copy; 2025 ShopMan. All rights reserved.</p>
    </footer>
</body>
</html>
