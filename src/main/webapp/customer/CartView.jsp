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
    <title>Giỏ hàng - ShopMan</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/customer/static/css/customer.css">
    <style>
        .cart-table-wrapper {
            max-height: 400px;
            overflow-y: auto;
            overflow-x: auto;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            margin-bottom: 20px;
        }
        
        .cart-table {
            width: 100%;
            border-collapse: separate;
            border-spacing: 0;
        }
        
        .cart-table thead {
            position: sticky;
            top: 0;
            background: #f8f9fa;
            z-index: 10;
        }
        
        .cart-table thead th {
            border-bottom: 2px solid #dee2e6;
            padding: 12px;
            font-weight: 600;
        }
        
        .select-checkbox {
            width: 18px;
            height: 18px;
            cursor: pointer;
        }
        
        .select-all-container {
            padding: 12px 0;
            border-bottom: 2px solid #dee2e6;
            margin-bottom: 10px;
            background: white;
            position: sticky;
            top: 0;
            z-index: 11;
        }
        
        .select-all-label {
            display: flex;
            align-items: center;
            gap: 8px;
            font-weight: 600;
            cursor: pointer;
            user-select: none;
        }
        
        .selected-count {
            color: #667eea;
            font-size: 14px;
            margin-left: 10px;
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
                <a href="${pageContext.request.contextPath}/cart?action=view" class="cart-link active">
                    Giỏ hàng
                </a>
                <a href="${pageContext.request.contextPath}/order?action=myOrders">Đơn hàng</a>
                <span class="user-name">👤 ${sessionScope.username}</span>
                <a href="${pageContext.request.contextPath}/auth?action=logout" class="btn-logout">Đăng xuất</a>
            </div>
        </div>
    </nav>

    <div class="container">
        
        <h1 class="page-title">Giỏ hàng của bạn</h1>
        <!-- Nút quay lại -->
        <div class="back-navigation">
            <a href="${pageContext.request.contextPath}/product?action=list" class="btn-back">← Quay lại</a>
        </div>

        <div class="cart-container">
            <!-- Checkbox chọn tất cả -->
            <div class="select-all-container">
                <label class="select-all-label">
                    <input type="checkbox" id="selectAll" class="select-checkbox" checked>
                    <span>Chọn tất cả</span>
                    <span class="selected-count" id="selectedCount">(${itemCount} sản phẩm)</span>
                </label>
            </div>
            
            <table class="cart-table">
                <thead>
                    <tr>
                        <th style="width: 40px;">Chọn</th>
                        <th>Sản phẩm</th>
                        <th>Giá</th>
                        <th>Số lượng</th>
                        <th>Thành tiền</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${cartDetails}">
                        <tr>
                            <td style="text-align: center;">
                                <input type="checkbox" 
                                        class="select-checkbox item-checkbox" 
                                        data-id="${item.id}"
                                        data-price="${item.product.price}"
                                        data-quantity="${item.quantity}"
                                        checked>
                            </td>
                            
                            <td class="product-info">
                                <div class="product-name">
                                    <strong>${item.product.name}</strong>
                                </div>
                                <div class="product-id">ID: ${item.product.id}</div>
                            </td>
                            
                            <td class="price">
                                <fmt:formatNumber value="${item.product.price}" pattern="#,###"/>₫
                            </td>
                            
                            <td class="quantity-cell">
                                <form action="${pageContext.request.contextPath}/cart" method="post" class="quantity-form">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="cartDetailId" value="${item.id}">
                                    
                                    <div class="quantity-controls-inline">
                                        <input type="number" name="quantity" value="${item.quantity}" 
                                                min="0" max="${item.product.quantity}" class="qty-input-small">
                                        <button type="submit" class="btn-update">Cập nhật</button>
                                    </div>
                                </form>
                                <small class="stock-info">Tối đa: ${item.product.quantity}</small>
                            </td>
                            
                            <td class="subtotal">
                                <fmt:formatNumber value="${item.product.price * item.quantity}" pattern="#,###"/>₫
                            </td>
                            
                            <td class="actions-cell">
                                <a href="${pageContext.request.contextPath}/cart?action=remove&cartDetailId=${item.id}" 
                                    class="btn-remove"
                                    onclick="return confirm('Xóa sản phẩm này khỏi giỏ hàng?')">
                                    🗑️ Xóa
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <!-- Tổng kết giỏ hàng -->
            <div class="cart-summary">
                <div class="summary-row">
                    <span class="summary-label">Sản phẩm đã chọn:</span>
                    <span class="summary-value" id="selectedItemCount">${itemCount}</span>
                </div>
                <div class="summary-row total-row">
                    <span class="summary-label">Tổng tiền:</span>
                    <span class="summary-value total-amount" id="totalAmount">
                        <fmt:formatNumber value="${totalAmount}" pattern="#,###"/>₫
                    </span>
                </div>
            </div>

            <!-- Các hành động -->
            <div class="cart-actions">
            
                
                <button type="button" class="btn-checkout" id="checkoutBtn">
                    ✓ Đặt hàng
                </button>
            </div>
        </div>
    </div>
    
    <script>
        // Hàm định dạng số tiền
        function formatCurrency(amount) {
            return amount.toLocaleString('vi-VN');
        }
        
        // Hàm tính tổng tiền của sản phẩm đã chọn
        function updateTotal() {
            const checkboxes = document.querySelectorAll('.item-checkbox:checked');
            let total = 0;
            let count = 0;
            
            checkboxes.forEach(checkbox => {
                const price = parseFloat(checkbox.dataset.price);
                const quantity = parseInt(checkbox.dataset.quantity);
                total += price * quantity;
                count++;
            });
            
            // Cập nhật hiển thị
            document.getElementById('totalAmount').textContent = formatCurrency(total) + '₫';
            document.getElementById('selectedItemCount').textContent = count;
            document.getElementById('selectedCount').textContent = '(' + count + ' sản phẩm)';
        }
        
        // Xử lý checkbox "Chọn tất cả"
        document.getElementById('selectAll').addEventListener('change', function() {
            const checkboxes = document.querySelectorAll('.item-checkbox');
            checkboxes.forEach(checkbox => {
                checkbox.checked = this.checked;
            });
            updateTotal();
        });
        
        // Xử lý checkbox từng sản phẩm
        document.querySelectorAll('.item-checkbox').forEach(checkbox => {
            checkbox.addEventListener('change', function() {
                // Kiểm tra nếu tất cả checkbox đã được chọn
                const allCheckboxes = document.querySelectorAll('.item-checkbox');
                const checkedCheckboxes = document.querySelectorAll('.item-checkbox:checked');
                document.getElementById('selectAll').checked = allCheckboxes.length === checkedCheckboxes.length;
                
                updateTotal();
            });
        });
        
        // Xử lý nút "Đặt hàng"
        document.getElementById('checkoutBtn').addEventListener('click', function() {
            const checkboxes = document.querySelectorAll('.item-checkbox:checked');
            
            if (checkboxes.length === 0) {
                alert('Vui lòng chọn ít nhất một sản phẩm để đặt hàng!');
                return;
            }
            
            if (!confirm('Đặt hàng ' + checkboxes.length + ' sản phẩm đã chọn?')) {
                return;
            }
            
            // Tạo form để đặt hàng
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = '${pageContext.request.contextPath}/order';
            
            const actionInput = document.createElement('input');
            actionInput.type = 'hidden';
            actionInput.name = 'action';
            actionInput.value = 'create';
            form.appendChild(actionInput);
            
            checkboxes.forEach(checkbox => {
                const input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'selectedItems';
                input.value = checkbox.dataset.id;
                form.appendChild(input);
            });
            
            document.body.appendChild(form);
            form.submit();
        });
    </script>
</body>
</html>
