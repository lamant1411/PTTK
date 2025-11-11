<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - ShopMan</title>
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

        .login-container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            overflow: hidden;
            width: 100%;
            max-width: 900px;
            display: grid;
            grid-template-columns: 1fr 1fr;
        }

        .login-image {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 60px 40px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            color: white;
            text-align: center;
        }

        .login-image-icon {
            font-size: 100px;
            margin-bottom: 30px;
        }

        .login-image h1 {
            font-size: 36px;
            margin-bottom: 20px;
        }

        .login-image p {
            font-size: 16px;
            line-height: 1.6;
            opacity: 0.9;
        }

        .login-form-container {
            padding: 60px 40px;
        }

        .login-form-header {
            text-align: center;
            margin-bottom: 40px;
        }

        .login-form-header h2 {
            color: #333;
            font-size: 28px;
            margin-bottom: 10px;
        }

        .login-form-header p {
            color: #666;
            font-size: 14px;
        }

        .alert {
            padding: 12px 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 14px;
        }

        .alert-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .form-group {
            margin-bottom: 25px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #333;
            font-weight: 500;
            font-size: 14px;
        }

        .form-control {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 14px;
            transition: all 0.3s;
        }

        .form-control:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        .input-icon {
            position: relative;
        }

        .input-icon input {
            padding-left: 45px;
        }

        .input-icon::before {
            content: attr(data-icon);
            position: absolute;
            left: 15px;
            top: 50%;
            transform: translateY(-50%);
            font-size: 18px;
            color: #999;
        }

        .btn-login {
            width: 100%;
            padding: 14px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
            margin-top: 10px;
        }

        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
        }

        .btn-login:active {
            transform: translateY(0);
        }

        .form-footer {
            text-align: center;
            margin-top: 30px;
        }

        .form-footer p {
            color: #666;
            font-size: 14px;
            margin-bottom: 10px;
        }

        .form-footer a {
            color: #667eea;
            text-decoration: none;
            font-weight: 600;
        }

        .form-footer a:hover {
            text-decoration: underline;
        }

        .divider {
            text-align: center;
            margin: 25px 0;
            position: relative;
        }

        .divider::before {
            content: '';
            position: absolute;
            left: 0;
            top: 50%;
            width: 100%;
            height: 1px;
            background: #e0e0e0;
        }

        .divider span {
            background: white;
            padding: 0 15px;
            position: relative;
            color: #999;
            font-size: 13px;
        }

        .role-info {
            background: #f5f5f5;
            padding: 15px;
            border-radius: 8px;
            margin-top: 20px;
        }

        .role-info h4 {
            color: #333;
            font-size: 14px;
            margin-bottom: 10px;
        }

        .role-info ul {
            list-style: none;
            padding-left: 0;
        }

        .role-info li {
            color: #666;
            font-size: 13px;
            margin-bottom: 5px;
            padding-left: 20px;
            position: relative;
        }

        .role-info li::before {
            content: '✓';
            position: absolute;
            left: 0;
            color: #4caf50;
            font-weight: bold;
        }

        @media (max-width: 768px) {
            .login-container {
                grid-template-columns: 1fr;
            }

            .login-image {
                display: none;
            }

            .login-form-container {
                padding: 40px 30px;
            }
        }

        /* Loading animation */
        .btn-login.loading {
            pointer-events: none;
            opacity: 0.7;
        }

        .btn-login.loading::after {
            content: '';
            display: inline-block;
            width: 16px;
            height: 16px;
            border: 2px solid white;
            border-top-color: transparent;
            border-radius: 50%;
            animation: spin 0.6s linear infinite;
            margin-left: 10px;
            vertical-align: middle;
        }

        @keyframes spin {
            to { transform: rotate(360deg); }
        }
    </style>
</head>
<body>
    <div class="login-container">
        <!-- Left side - Brand -->
        <div class="login-image">
            <div class="login-image-icon">🛒</div>
            <h1>ShopMan</h1>
            <p>Hệ thống quản lý bán hàng toàn diện. Đăng nhập để trải nghiệm các tính năng dành cho khách hàng và quản lý.</p>
            
            <div class="role-info" style="margin-top: 40px; background: rgba(255,255,255,0.1); border: 1px solid rgba(255,255,255,0.2);">
                <h4 style="color: white;">Vai trò hệ thống:</h4>
                <ul>
                    <li style="color: rgba(255,255,255,0.9);">👤 Customer - Mua sắm và quản lý đơn hàng</li>
                    <li style="color: rgba(255,255,255,0.9);">👨‍💼 Manager - Quản lý sản phẩm và hệ thống</li>
                </ul>
            </div>
        </div>

        <!-- Right side - Login Form -->
        <div class="login-form-container">
            <div class="login-form-header">
                <h2>Đăng nhập</h2>
                <p>Chào mừng bạn quay trở lại!</p>
            </div>

            <!-- Success Message -->
            <c:if test="${not empty param.message}">
                <div class="alert alert-success">
                    ${param.message}
                </div>
            </c:if>

            <!-- Error Message -->
            <c:if test="${not empty error}">
                <div class="alert alert-error">
                    ${error}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/auth" method="post" id="loginForm">
                <input type="hidden" name="action" value="login">
                
                <div class="form-group">
                    <label for="username">Tên đăng nhập</label>
                    <div class="input-icon" data-icon="👤">
                        <input type="text" 
                               class="form-control" 
                               id="username" 
                               name="username" 
                               placeholder="Nhập tên đăng nhập"
                               value="${username}"
                               required>
                    </div>
                </div>

                <div class="form-group">
                    <label for="password">Mật khẩu</label>
                    <div class="input-icon" data-icon="🔒">
                        <input type="password" 
                               class="form-control" 
                               id="password" 
                               name="password" 
                               placeholder="Nhập mật khẩu"
                               required>
                    </div>
                </div>

                <button type="submit" class="btn-login" id="btnLogin">
                    Đăng nhập
                </button>

                <div class="divider">
                    <span>hoặc</span>
                </div>

                <div class="form-footer">
                    <p>Chưa có tài khoản?</p>
                    <a href="${pageContext.request.contextPath}/auth?action=register">Đăng ký ngay</a>
                </div>
            </form>

            <div class="role-info">
                <h4>📌 Hướng dẫn đăng nhập:</h4>
                <ul>
                    <li>Nhập tên đăng nhập và mật khẩu</li>
                    <li>Hệ thống tự động nhận diện vai trò</li>
                    <li>Bạn sẽ được chuyển đến trang tương ứng</li>
                </ul>
            </div>
        </div>
    </div>

    <script>
        // Form submit animation
        document.getElementById('loginForm').addEventListener('submit', function() {
            var btn = document.getElementById('btnLogin');
            btn.classList.add('loading');
            btn.textContent = 'Đang đăng nhập';
        });

        // Auto focus on username field
        window.addEventListener('load', function() {
            document.getElementById('username').focus();
        });

        // Enter key support
        document.getElementById('username').addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                document.getElementById('password').focus();
            }
        });
    </script>
</body>
</html>
