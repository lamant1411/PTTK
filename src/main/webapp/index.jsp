<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ShopMan - Hệ thống quản lý</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            background: #f5f5f5;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .welcome-box {
            background: white;
            border: 1px solid #ddd;
            border-radius: 4px;
            padding: 40px;
            max-width: 500px;
            text-align: center;
        }

        .logo {
            font-size: 50px;
            margin-bottom: 20px;
        }

        h1 {
            color: #2c3e50;
            font-size: 32px;
            margin-bottom: 16px;
        }

        p {
            color: #666;
            font-size: 16px;
            margin-bottom: 30px;
            line-height: 1.5;
        }

        .buttons {
            display: flex;
            gap: 15px;
            justify-content: center;
        }

        .btn {
            padding: 12px 30px;
            border-radius: 4px;
            text-decoration: none;
            font-size: 16px;
            display: inline-block;
        }

        .btn-primary {
            background: #27ae60;
            color: white;
        }

        .btn-primary:hover {
            background: #229954;
        }

        .btn-secondary {
            background: white;
            color: #333;
            border: 1px solid #ddd;
        }

        .btn-secondary:hover {
            background: #f5f5f5;
        }
    </style>
</head>
<body>
    <div class="welcome-box">
        <div class="logo">🛒</div>
        <h1>ShopMan</h1>
        <p>Hệ thống quản lý bán hàng đơn giản<br>Giải pháp tối ưu cho việc mua bán và quản lý sản phẩm</p>

        <div class="buttons">
            <a href="${pageContext.request.contextPath}/auth?action=login" class="btn btn-primary">Đăng nhập</a>
            <a href="${pageContext.request.contextPath}/auth?action=register" class="btn btn-secondary">Đăng ký ngay</a>
        </div>
    </div>
</body>
</html>
