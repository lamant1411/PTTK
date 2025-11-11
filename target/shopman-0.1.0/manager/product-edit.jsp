<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Product - ShopMan</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/manager/static/css/manager.css">
</head>
<body>
    <div class="container">
        <!-- Header -->
        <header class="header">
            <h1>Edit Product</h1>
            <div class="user-info">
                <a href="${pageContext.request.contextPath}/product?action=list" class="btn-back">← Back to Products</a>
                <a href="${pageContext.request.contextPath}/auth?action=logout" class="btn-logout">Logout</a>
            </div>
        </header>

        <!-- Error Message -->
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <!-- Edit Product Form -->
        <div class="form-container">
            <form action="${pageContext.request.contextPath}/product?action=edit" method="post" class="product-form">
                <input type="hidden" name="action" value="edit">
                <input type="hidden" name="id" value="${product.id}">

                <div class="form-group">
                    <label for="id-display">Product ID</label>
                    <input type="text" id="id-display" value="${product.id}" disabled class="disabled-input">
                    <small>Product ID cannot be changed</small>
                </div>

                <div class="form-group">
                    <label for="name">Product Name <span class="required">*</span></label>
                    <input type="text" id="name" name="name" required 
                           value="${product.name}"
                           maxlength="255">
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="price">Price <span class="required">*</span></label>
                        <input type="number" id="price" name="price" required 
                               step="0.01" min="0" 
                               value="${product.price}">
                        <small>Price in USD</small>
                    </div>

                    <div class="form-group">
                        <label for="quantity">Quantity <span class="required">*</span></label>
                        <input type="number" id="quantity" name="quantity" required 
                               min="0" 
                               value="${product.quantity}">
                        <small>Available stock</small>
                    </div>
                </div>

                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea id="description" name="description" rows="5">${product.des}</textarea>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn-primary">Update Product</button>
                    <a href="${pageContext.request.contextPath}/product?action=list" class="btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
