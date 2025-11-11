<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ShopMan - Hệ thống quản lý bán hàng</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .welcome-container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            padding: 60px;
            max-width: 800px;
            text-align: center;
        }

        .logo {
            font-size: 100px;
            margin-bottom: 30px;
        }

        h1 {
            color: #333;
            font-size: 48px;
            margin-bottom: 20px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        .tagline {
            color: #666;
            font-size: 18px;
            margin-bottom: 40px;
            line-height: 1.6;
        }

        .cta-buttons {
            display: flex;
            gap: 20px;
            justify-content: center;
            margin-bottom: 40px;
        }

        .btn {
            padding: 15px 40px;
            border-radius: 10px;
            text-decoration: none;
            font-size: 16px;
            font-weight: 600;
            transition: all 0.3s;
            display: inline-block;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-3px);
            box-shadow: 0 10px 30px rgba(102, 126, 234, 0.4);
        }

        .btn-secondary {
            background: white;
            color: #667eea;
            border: 2px solid #667eea;
        }

        .btn-secondary:hover {
            background: #667eea;
            color: white;
            transform: translateY(-3px);
            box-shadow: 0 10px 30px rgba(102, 126, 234, 0.4);
        }

        .features {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 30px;
            margin-top: 50px;
        }

        .feature {
            padding: 20px;
            background: #f5f5f5;
            border-radius: 10px;
            transition: all 0.3s;
        }

        .feature:hover {
            transform: translateY(-5px);
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .feature-icon {
            font-size: 40px;
            margin-bottom: 15px;
        }

        .feature h3 {
            font-size: 18px;
            margin-bottom: 10px;
        }

        .feature p {
            font-size: 14px;
            opacity: 0.8;
        }

        .feature:hover h3,
        .feature:hover p {
            color: white;
        }

        @media (max-width: 768px) {
            .welcome-container {
                padding: 40px 30px;
            }

            h1 {
                font-size: 36px;
            }

            .cta-buttons {
                flex-direction: column;
            }

            .features {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <div class="welcome-container">
        <div class="logo">🛒</div>
        <h1>ShopMan</h1>
        <p class="tagline">
            Hệ thống quản lý bán hàng toàn diện<br>
            Giải pháp tối ưu cho việc mua bán và quản lý sản phẩm
        </p>

        <div class="cta-buttons">
            <a href="${pageContext.request.contextPath}/auth?action=login" class="btn btn-primary">
                Đăng nhập
            </a>
            <a href="${pageContext.request.contextPath}/auth?action=register" class="btn btn-secondary">
                Đăng ký ngay
            </a>
        </div>

        <div class="features">
            <div class="feature">
                <div class="feature-icon">🛍️</div>
                <h3>Mua sắm Online</h3>
                <p>Dễ dàng tìm kiếm và đặt hàng sản phẩm yêu thích</p>
            </div>

            <div class="feature">
                <div class="feature-icon">📦</div>
                <h3>Quản lý Đơn hàng</h3>
                <p>Theo dõi trạng thái đơn hàng mọi lúc mọi nơi</p>
            </div>

            <div class="feature">
                <div class="feature-icon">📊</div>
                <h3>Dashboard Quản lý</h3>
                <p>Quản trị sản phẩm và doanh thu hiệu quả</p>
            </div>

            <div class="feature">
                <div class="feature-icon">🔒</div>
                <h3>Bảo mật Cao</h3>
                <p>Đảm bảo an toàn thông tin người dùng</p>
            </div>
        </div>
    </div>
</body>
</html>
