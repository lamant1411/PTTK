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
    <title>Thêm sản phẩm - ShopMan</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/manager/static/css/manager.css">
    <style>
        body {
            margin: 0;
            padding: 0;
        }
        
        .container {
            padding: 10px;
        }
        
        .header {
            margin-bottom: 15px;
        }
        
        .form-container {
            background: white;
            border-radius: 5px;
            padding: 20px;
            max-width: 900px;
            margin: 0 auto;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        }
        
        .form-header {
            border-bottom: 2px solid #4CAF50;
            padding-bottom: 10px;
            margin-bottom: 20px;
        }
        
        .form-header h2 {
            color: #333;
            margin: 0;
            font-size: 20px;
        }
        
        .form-header p {
            color: #666;
            margin: 5px 0 0 0;
            font-size: 13px;
        }
        
        .form-group {
            margin-bottom: 15px;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 5px;
            color: #333;
            font-weight: 600;
            font-size: 13px;
        }
        
        .required {
            color: #e74c3c;
            margin-left: 2px;
        }
        
        .form-group input[type="text"],
        .form-group input[type="number"],
        .form-group select,
        .form-group textarea {
            width: 100%;
            padding: 8px 10px;
            border: 1px solid #ddd;
            border-radius: 3px;
            font-size: 13px;
        }
        
        .form-group input:focus,
        .form-group select:focus,
        .form-group textarea:focus {
            outline: none;
            border-color: #4CAF50;
        }
        
        .form-group input[readonly] {
            background: #f5f5f5;
            color: #666;
            cursor: not-allowed;
        }
        
        .form-group textarea {
            resize: vertical;
            min-height: 60px;
        }
        
        .form-group small {
            display: block;
            margin-top: 3px;
            color: #999;
            font-size: 11px;
        }
        
        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr 1fr;
            gap: 15px;
        }
        
        .auto-id-badge {
            display: inline-block;
            background: #e8f5e9;
            color: #2e7d32;
            padding: 2px 8px;
            border-radius: 10px;
            font-size: 11px;
            margin-left: 8px;
            font-weight: 500;
        }
        
        .form-actions {
            margin-top: 20px;
            padding-top: 15px;
            border-top: 1px solid #eee;
            display: flex;
            gap: 10px;
            justify-content: flex-end;
        }
        
        .btn-primary {
            background: #4CAF50;
            color: white;
            padding: 10px 25px;
            border: none;
            border-radius: 3px;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
        }
        
        .btn-primary:hover {
            background: #45a049;
        }
        
        .btn-secondary {
            background: #fff;
            color: #666;
            padding: 10px 25px;
            border: 1px solid #ddd;
            border-radius: 3px;
            text-decoration: none;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
        }
        
        .btn-secondary:hover {
            border-color: #999;
            color: #333;
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

        <!-- Thông báo lỗi -->
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <!-- Form thêm sản phẩm -->
        <div class="form-container">
            <div class="form-header">
                <h2>Thêm sản phẩm mới</h2>
                <p>Điền thông tin sản phẩm bên dưới</p>
            </div>
            
            <form action="${pageContext.request.contextPath}/product?action=add" method="post" class="product-form">
                <input type="hidden" name="action" value="add">

                <div class="form-group">
                    <label for="id">
                        Mã sản phẩm <span class="required">*</span>
                    </label>
                    <input type="text" id="id" name="id" 
                           value="${nextProductId}" 
                           readonly
                           required>
                </div>

                <div class="form-group">
                    <label for="name">Tên sản phẩm <span class="required">*</span></label>
                    <input type="text" id="name" name="name" required 
                           placeholder="Nhập tên sản phẩm"
                           maxlength="255">
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="price">Giá (₫) <span class="required">*</span></label>
                        <input type="number" id="price" name="price" required 
                               step="0.01" min="0" 
                               placeholder="0">
                    </div>

                    <div class="form-group">
                        <label for="quantity">Số lượng <span class="required">*</span></label>
                        <input type="number" id="quantity" name="quantity" required 
                               min="0" 
                               placeholder="0"
                               value="0">
                    </div>

                    <div class="form-group">
                        <label for="unit">Đơn vị <span class="required">*</span></label>
                        <select id="unit" name="unit" required>
                            <option value="">-- Chọn --</option>
                            <option value="Cái">Cái</option>
                            <option value="Hộp">Hộp</option>
                            <option value="Gói">Gói</option>
                            <option value="Bộ">Bộ</option>
                            <option value="Kg">Kg</option>
                            <option value="Lít">Lít</option>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label for="description">Mô tả</label>
                    <textarea id="description" name="description" rows="3" 
                              placeholder="Mô tả sản phẩm..."></textarea>
                </div>

                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/product?action=list" class="btn-secondary">Hủy</a>
                    <button type="submit" class="btn-primary">✓ Thêm sản phẩm</button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
