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
    <title>Checkout - ShopMan</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/customer/static/css/customer.css">
    <style>
        .checkout-container {
            max-width: 1000px;
            margin: 30px auto;
            padding: 20px;
        }
        
        .checkout-grid {
            display: grid;
            grid-template-columns: 2fr 1fr;
            gap: 30px;
            margin-top: 20px;
        }
        
        .checkout-section {
            background: white;
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        
        .section-title {
            font-size: 20px;
            font-weight: 600;
            margin-bottom: 20px;
            color: #333;
            border-bottom: 2px solid #4CAF50;
            padding-bottom: 10px;
        }
        
        .order-item {
            display: flex;
            justify-content: space-between;
            padding: 15px 0;
            border-bottom: 1px solid #eee;
        }
        
        .order-item:last-child {
            border-bottom: none;
        }
        
        .item-details {
            flex: 1;
        }
        
        .item-name {
            font-weight: 600;
            color: #333;
            margin-bottom: 5px;
        }
        
        .item-quantity {
            color: #666;
            font-size: 14px;
        }
        
        .item-price {
            font-weight: 600;
            color: #4CAF50;
            white-space: nowrap;
            margin-left: 20px;
        }
        
        .order-summary {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 5px;
            margin-top: 20px;
        }
        
        .summary-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            font-size: 15px;
        }
        
        .summary-row.total {
            border-top: 2px solid #ddd;
            margin-top: 10px;
            padding-top: 15px;
            font-size: 18px;
            font-weight: 700;
            color: #4CAF50;
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #333;
        }
        
        .form-group input,
        .form-group textarea,
        .form-group select {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
        }
        
        .form-group textarea {
            resize: vertical;
            min-height: 80px;
        }
        
        .btn-place-order {
            width: 100%;
            background: #4CAF50;
            color: white;
            padding: 15px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            margin-top: 20px;
        }
        
        .btn-place-order:hover {
            background: #45a049;
        }
        
        .btn-back {
            display: inline-block;
            color: #666;
            text-decoration: none;
            margin-bottom: 20px;
        }
        
        .btn-back:hover {
            color: #333;
        }
        
        .required {
            color: red;
        }
        
        @media (max-width: 768px) {
            .checkout-grid {
                grid-template-columns: 1fr;
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
                <a href="${pageContext.request.contextPath}/product?action=list">Products</a>
                <a href="${pageContext.request.contextPath}/cart?action=view" class="cart-link">
                    🛒 Cart
                </a>
                <a href="${pageContext.request.contextPath}/order?action=myOrders">My Orders</a>
                <span class="user-name">👤 ${sessionScope.username}</span>
                <a href="${pageContext.request.contextPath}/auth?action=logout" class="btn-logout">Logout</a>
            </div>
        </div>
    </nav>

    <div class="checkout-container">
        <a href="${pageContext.request.contextPath}/cart?action=view" class="btn-back">← Back to Cart</a>
        
        <h1 class="page-title">Checkout</h1>

        <!-- Error Messages -->
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <div class="checkout-grid">
            <!-- Left: Shipping Information -->
            <div class="checkout-section">
                <h2 class="section-title">Shipping Information</h2>
                
                <form action="${pageContext.request.contextPath}/order?action=create" method="post" id="checkoutForm">
                    <div class="form-group">
                        <label for="fullName">Full Name <span class="required">*</span></label>
                        <input type="text" id="fullName" name="fullName" required 
                               value="${sessionScope.username}" placeholder="Enter your full name">
                    </div>
                    
                    <div class="form-group">
                        <label for="phone">Phone Number <span class="required">*</span></label>
                        <input type="tel" id="phone" name="phone" required 
                               placeholder="Enter your phone number" pattern="[0-9]{10,11}">
                    </div>
                    
                    <div class="form-group">
                        <label for="address">Shipping Address <span class="required">*</span></label>
                        <textarea id="address" name="address" required 
                                  placeholder="Enter your shipping address"></textarea>
                    </div>
                    
                    <div class="form-group">
                        <label for="notes">Order Notes (Optional)</label>
                        <textarea id="notes" name="notes" 
                                  placeholder="Any special instructions for your order"></textarea>
                    </div>
                    
                    <div class="form-group">
                        <label for="paymentMethod">Payment Method <span class="required">*</span></label>
                        <select id="paymentMethod" name="paymentMethod" required>
                            <option value="">-- Select Payment Method --</option>
                            <option value="COD">Cash on Delivery (COD)</option>
                            <option value="Bank">Bank Transfer</option>
                            <option value="Card">Credit/Debit Card</option>
                        </select>
                    </div>
                </form>
            </div>

            <!-- Right: Order Summary -->
            <div class="checkout-section">
                <h2 class="section-title">Order Summary</h2>
                
                <div class="order-items">
                    <c:forEach var="item" items="${cartDetails}">
                        <div class="order-item">
                            <div class="item-details">
                                <div class="item-name">${item.product.name}</div>
                                <div class="item-quantity">Quantity: ${item.quantity} × $<fmt:formatNumber value="${item.product.price}" pattern="#,##0.00"/></div>
                            </div>
                            <div class="item-price">
                                $<fmt:formatNumber value="${item.product.price * item.quantity}" pattern="#,##0.00"/>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                
                <div class="order-summary">
                    <div class="summary-row">
                        <span>Subtotal:</span>
                        <span>$<fmt:formatNumber value="${totalAmount}" pattern="#,##0.00"/></span>
                    </div>
                    <div class="summary-row">
                        <span>Shipping:</span>
                        <span>Free</span>
                    </div>
                    <div class="summary-row">
                        <span>Tax:</span>
                        <span>$0.00</span>
                    </div>
                    <div class="summary-row total">
                        <span>Total:</span>
                        <span>$<fmt:formatNumber value="${totalAmount}" pattern="#,##0.00"/></span>
                    </div>
                </div>
                
                <button type="submit" form="checkoutForm" class="btn-place-order">
                    🛍️ Place Order
                </button>
            </div>
        </div>
    </div>

    <script>
        // Form validation
        document.getElementById('checkoutForm').addEventListener('submit', function(e) {
            const phone = document.getElementById('phone').value;
            const phoneRegex = /^[0-9]{10,11}$/;
            
            if (!phoneRegex.test(phone)) {
                e.preventDefault();
                alert('Please enter a valid phone number (10-11 digits)');
                return false;
            }
            
            const paymentMethod = document.getElementById('paymentMethod').value;
            if (!paymentMethod) {
                e.preventDefault();
                alert('Please select a payment method');
                return false;
            }
            
            return confirm('Confirm place order?');
        });
    </script>
</body>
</html>
