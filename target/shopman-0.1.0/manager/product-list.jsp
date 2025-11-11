<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Products - ShopMan</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/manager/static/css/manager.css">
</head>
<body>
    <div class="container">
        <!-- Header -->
        <header class="header">
            <h1>Manage Products</h1>
            <div class="user-info">
                <a href="${pageContext.request.contextPath}/manager/dashboard.jsp" class="btn-back">← Back to Dashboard</a>
                <a href="${pageContext.request.contextPath}/auth?action=logout" class="btn-logout">Logout</a>
            </div>
        </header>

        <!-- Success/Error Messages -->
        <c:if test="${not empty param.message}">
            <div class="alert alert-success">${param.message}</div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-error">${param.error}</div>
        </c:if>

        <!-- Actions Bar -->
        <div class="actions-bar">
            <a href="${pageContext.request.contextPath}/product?action=add" class="btn-primary">+ Add New Product</a>
            
            <!-- Search Form -->
            <form action="${pageContext.request.contextPath}/product" method="get" class="search-form">
                <input type="hidden" name="action" value="search">
                <input type="text" name="keyword" placeholder="Search products..." value="${keyword}">
                <button type="submit" class="btn-search">Search</button>
            </form>
        </div>

        <!-- Products Table -->
        <div class="table-container">
            <table class="product-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>Description</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty products}">
                            <c:forEach var="product" items="${products}">
                                <tr>
                                    <td>${product.id}</td>
                                    <td><strong>${product.name}</strong></td>
                                    <td class="price">$${product.price}</td>
                                    <td class="quantity ${product.quantity < 10 ? 'low-stock' : ''}">${product.quantity}</td>
                                    <td class="description">${product.des}</td>
                                    <td class="actions">
                                        <a href="${pageContext.request.contextPath}/product?action=view&id=${product.id}" 
                                           class="btn-view" title="View">👁️</a>
                                        <a href="${pageContext.request.contextPath}/product?action=edit&id=${product.id}" 
                                           class="btn-edit" title="Edit">✏️</a>
                                        <a href="${pageContext.request.contextPath}/product?action=delete&id=${product.id}" 
                                           class="btn-delete" 
                                           onclick="return confirm('Are you sure you want to delete this product?')" 
                                           title="Delete">🗑️</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="6" class="no-data">No products found</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>

        <!-- Pagination -->
        <c:if test="${not empty totalPages and totalPages > 1}">
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/product?action=list&page=${currentPage - 1}" class="page-link">← Previous</a>
                </c:if>
                
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="${pageContext.request.contextPath}/product?action=list&page=${i}" 
                       class="page-link ${currentPage == i ? 'active' : ''}">${i}</a>
                </c:forEach>
                
                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/product?action=list&page=${currentPage + 1}" class="page-link">Next →</a>
                </c:if>
            </div>
        </c:if>

        <!-- Summary -->
        <div class="summary">
            <p>Total Products: <strong>${totalProducts}</strong></p>
            <c:if test="${not empty currentPage}">
                <p>Page ${currentPage} of ${totalPages}</p>
            </c:if>
        </div>
    </div>
</body>
</html>
