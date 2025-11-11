<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Product - ShopMan</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/manager/static/css/manager.css">
</head>
<body>
    <div class="container">
        <!-- Header -->
        <header class="header">
            <h1>Add New Product</h1>
            <div class="user-info">
                <a href="${pageContext.request.contextPath}/product?action=list" class="btn-back">← Back to Products</a>
                <a href="${pageContext.request.contextPath}/auth?action=logout" class="btn-logout">Logout</a>
            </div>
        </header>

        <!-- Error Message -->
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <!-- Add Product Form -->
        <div class="form-container">
            <form action="${pageContext.request.contextPath}/product?action=add" method="post" class="product-form">
                <input type="hidden" name="action" value="add">

                <div class="form-group">
                    <label for="id">Product ID <span class="required">*</span></label>
                    <input type="text" id="id" name="id" required 
                           placeholder="e.g., P001" 
                           pattern="[A-Z0-9]+" 
                           title="Use uppercase letters and numbers only">
                    <small>Unique identifier for the product</small>
                </div>

                <div class="form-group">
                    <label for="name">Product Name <span class="required">*</span></label>
                    <input type="text" id="name" name="name" required 
                           placeholder="e.g., Laptop Dell XPS 15"
                           maxlength="255">
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="price">Price <span class="required">*</span></label>
                        <input type="number" id="price" name="price" required 
                               step="0.01" min="0" 
                               placeholder="0.00">
                        <small>Price in USD</small>
                    </div>

                    <div class="form-group">
                        <label for="quantity">Quantity <span class="required">*</span></label>
                        <input type="number" id="quantity" name="quantity" required 
                               min="0" 
                               placeholder="0">
                        <small>Available stock</small>
                    </div>
                </div>

                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea id="description" name="description" rows="5" 
                              placeholder="Enter product description..."></textarea>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn-primary">Add Product</button>
                    <a href="${pageContext.request.contextPath}/product?action=list" class="btn-secondary">Cancel</a>
                </div>
            </form>
        </div>

        <!-- Help Section -->
        <div class="help-section">
            <h3>💡 Tips</h3>
            <ul>
                <li>Product ID must be unique and cannot be changed later</li>
                <li>Use descriptive names for better searchability</li>
                <li>Price should be in USD format (e.g., 999.99)</li>
                <li>Set initial quantity based on your stock</li>
            </ul>
        </div>
    </div>
</body>
</html>
