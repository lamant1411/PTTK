<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - ShopMan</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: Arial, sans-serif; background: #f5f5f5; min-height: 100vh; display: flex; align-items: center; justify-content: center; padding: 20px; }
        .login-container { background: white; border: 1px solid #ddd; border-radius: 5px; padding: 40px; width: 100%; max-width: 400px; }
        .logo { text-align: center; font-size: 40px; margin-bottom: 10px; }
        h1 { text-align: center; color: #2c3e50; margin-bottom: 30px; font-size: 24px; }
        .form-group { margin-bottom: 20px; }
        label { display: block; margin-bottom: 8px; color: #333; font-weight: 500; }
        input[type="text"], input[type="password"] { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 4px; font-size: 14px; }
        input:focus { outline: none; border-color: #2c3e50; }
        .btn-login { width: 100%; padding: 12px; background: #27ae60; color: white; border: none; border-radius: 4px; font-size: 16px; cursor: pointer; margin-top: 10px; }
        .btn-login:hover { background: #229954; }
        .alert { padding: 12px; margin-bottom: 20px; border-radius: 4px; }
        .alert-error { background: #f8d7da; color: #721c24; border-left: 4px solid #dc3545; }
        .register-link { text-align: center; margin-top: 20px; color: #666; }
        .register-link a { color: #27ae60; text-decoration: none; }
        .register-link a:hover { text-decoration: underline; }
        .back-home { text-align: center; margin-top: 15px; }
        .back-home a { color: #666; text-decoration: none; font-size: 14px; }
        .back-home a:hover { color: #2c3e50; }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="logo"></div>
        <h1>Đăng nhập</h1>
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>
        <form action="${pageContext.request.contextPath}/auth" method="post">
            <input type="hidden" name="action" value="login">
            <div class="form-group">
                <label for="username">Tên đăng nhập</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="form-group">
                <label for="password">Mật khẩu</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit" class="btn-login">Đăng nhập</button>
        </form>
        <div class="register-link">
            Chưa có tài khoản? <a href="${pageContext.request.contextPath}/auth?action=register">Đăng ký ngay</a>
        </div>
        <div class="back-home">
            <a href="${pageContext.request.contextPath}/"> Quay lại trang chủ</a>
        </div>
    </div>
</body>
</html>