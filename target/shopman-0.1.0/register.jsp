<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký - ShopMan</title>
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

        .register-container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            overflow: hidden;
            width: 100%;
            max-width: 900px;
            display: grid;
            grid-template-columns: 1fr 1fr;
        }

        .register-image {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 60px 40px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            color: white;
            text-align: center;
        }

        .register-image-icon {
            font-size: 100px;
            margin-bottom: 30px;
        }

        .register-image h1 {
            font-size: 36px;
            margin-bottom: 20px;
        }

        .register-image p {
            font-size: 16px;
            line-height: 1.6;
            opacity: 0.9;
        }

        .register-form-container {
            padding: 60px 40px;
            max-height: 90vh;
            overflow-y: auto;
        }

        .register-form-header {
            text-align: center;
            margin-bottom: 30px;
        }

        .register-form-header h2 {
            color: #333;
            font-size: 28px;
            margin-bottom: 10px;
        }

        .register-form-header p {
            color: #666;
            font-size: 14px;
        }

        .alert {
            padding: 12px 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 14px;
        }

        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #333;
            font-weight: 500;
            font-size: 14px;
        }

        .required {
            color: #f44336;
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

        .btn-register {
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

        .btn-register:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
        }

        .btn-register:active {
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

        .help-text {
            font-size: 12px;
            color: #999;
            margin-top: 5px;
        }

        .benefits {
            background: rgba(255,255,255,0.1);
            border: 1px solid rgba(255,255,255,0.2);
            padding: 20px;
            border-radius: 10px;
            margin-top: 30px;
        }

        .benefits h3 {
            color: white;
            font-size: 18px;
            margin-bottom: 15px;
        }

        .benefits ul {
            list-style: none;
            padding: 0;
        }

        .benefits li {
            color: rgba(255,255,255,0.9);
            margin-bottom: 10px;
            padding-left: 25px;
            position: relative;
            font-size: 14px;
        }

        .benefits li::before {
            content: '✓';
            position: absolute;
            left: 0;
            color: #4caf50;
            font-weight: bold;
            font-size: 18px;
        }

        @media (max-width: 768px) {
            .register-container {
                grid-template-columns: 1fr;
            }

            .register-image {
                display: none;
            }

            .register-form-container {
                padding: 40px 30px;
            }
        }

        .btn-register.loading {
            pointer-events: none;
            opacity: 0.7;
        }

        .btn-register.loading::after {
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
    <div class="register-container">
        <!-- Left side - Brand -->
        <div class="register-image">
            <div class="register-image-icon">🛒</div>
            <h1>ShopMan</h1>
            <p>Tham gia cộng đồng mua sắm trực tuyến của chúng tôi ngay hôm nay!</p>
            
            <div class="benefits">
                <h3>Quyền lợi thành viên:</h3>
                <ul>
                    <li>Mua sắm dễ dàng với giao diện thân thiện</li>
                    <li>Quản lý đơn hàng trực tuyến</li>
                    <li>Theo dõi lịch sử mua hàng</li>
                    <li>Nhận thông báo về đơn hàng</li>
                    <li>Hỗ trợ khách hàng 24/7</li>
                </ul>
            </div>
        </div>

        <!-- Right side - Register Form -->
        <div class="register-form-container">
            <div class="register-form-header">
                <h2>Đăng ký tài khoản</h2>
                <p>Tạo tài khoản mới để bắt đầu mua sắm</p>
            </div>

            <!-- Error Message -->
            <c:if test="${not empty error}">
                <div class="alert alert-error">
                    ${error}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/auth" method="post" id="registerForm">
                <input type="hidden" name="action" value="register">
                
                <div class="form-group">
                    <label for="username">Tên đăng nhập <span class="required">*</span></label>
                    <div class="input-icon" data-icon="👤">
                        <input type="text" 
                               class="form-control" 
                               id="username" 
                               name="username" 
                               placeholder="Chọn tên đăng nhập"
                               value="${username}"
                               minlength="4"
                               required>
                    </div>
                    <div class="help-text">Tối thiểu 4 ký tự</div>
                </div>

                <div class="form-group">
                    <label for="password">Mật khẩu <span class="required">*</span></label>
                    <div class="input-icon" data-icon="🔒">
                        <input type="password" 
                               class="form-control" 
                               id="password" 
                               name="password" 
                               placeholder="Nhập mật khẩu"
                               minlength="6"
                               required>
                    </div>
                    <div class="help-text">Tối thiểu 6 ký tự</div>
                </div>

                <div class="form-group">
                    <label for="confirmPassword">Xác nhận mật khẩu <span class="required">*</span></label>
                    <div class="input-icon" data-icon="🔒">
                        <input type="password" 
                               class="form-control" 
                               id="confirmPassword" 
                               name="confirmPassword" 
                               placeholder="Nhập lại mật khẩu"
                               required>
                    </div>
                </div>

                <div class="form-group">
                    <label for="address">Địa chỉ</label>
                    <div class="input-icon" data-icon="📍">
                        <input type="text" 
                               class="form-control" 
                               id="address" 
                               name="address" 
                               placeholder="Nhập địa chỉ giao hàng"
                               value="${address}">
                    </div>
                </div>

                <div class="form-group">
                    <label for="phone">Số điện thoại</label>
                    <div class="input-icon" data-icon="📱">
                        <input type="tel" 
                               class="form-control" 
                               id="phone" 
                               name="phone" 
                               placeholder="Nhập số điện thoại"
                               value="${phone}"
                               pattern="[0-9]{10,11}">
                    </div>
                    <div class="help-text">10-11 số</div>
                </div>

                <button type="submit" class="btn-register" id="btnRegister">
                    Đăng ký
                </button>

                <div class="divider">
                    <span>hoặc</span>
                </div>

                <div class="form-footer">
                    <p>Đã có tài khoản?</p>
                    <a href="${pageContext.request.contextPath}/auth?action=login">Đăng nhập ngay</a>
                </div>
            </form>
        </div>
    </div>

    <script>
        // Password confirmation validation
        var password = document.getElementById('password');
        var confirmPassword = document.getElementById('confirmPassword');

        function validatePassword() {
            if (password.value != confirmPassword.value) {
                confirmPassword.setCustomValidity("Mật khẩu không khớp!");
            } else {
                confirmPassword.setCustomValidity('');
            }
        }

        password.addEventListener('change', validatePassword);
        confirmPassword.addEventListener('keyup', validatePassword);

        // Form submit animation
        document.getElementById('registerForm').addEventListener('submit', function(e) {
            if (password.value !== confirmPassword.value) {
                e.preventDefault();
                alert('Mật khẩu xác nhận không khớp!');
                return;
            }
            
            var btn = document.getElementById('btnRegister');
            btn.classList.add('loading');
            btn.textContent = 'Đang đăng ký';
        });

        // Auto focus
        window.addEventListener('load', function() {
            document.getElementById('username').focus();
        });
    </script>
</body>
</html>
