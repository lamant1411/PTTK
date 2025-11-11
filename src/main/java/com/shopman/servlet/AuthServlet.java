package com.shopman.servlet;
import com.shopman.dao.MemberDAO;
import com.shopman.model.Member;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@WebServlet(name = "AuthServlet", urlPatterns = {"/auth"})
public class AuthServlet extends HttpServlet {
    private MemberDAO memberDAO;
    @Override
    public void init() throws ServletException {
        super.init();
        memberDAO = new MemberDAO();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "login";
        }
        switch (action) {
            case "login":
                showLogin(request, response);
                break;
            case "logout":
                logout(request, response);
                break;
            case "register":
                showRegister(request, response);
                break;
            default:
                showLogin(request, response);
                break;
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "login";
        }
        switch (action) {
            case "login":
                login(request, response);
                break;
            case "register":
                register(request, response);
                break;
            default:
                showLogin(request, response);
                break;
        }
    }
    private void showLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    private void showRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
    private void login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String redirect = request.getParameter("redirect");
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập tên đăng nhập và mật khẩu");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        try {
            Member member = memberDAO.login(username, password);
            if (member != null) {
                HttpSession session = request.getSession();
                session.setAttribute("userId", member.getId());
                session.setAttribute("username", member.getUsername());
                session.setAttribute("role", member.getRole());
                if ("customer".equals(member.getRole())) {
                    session.setAttribute("customerId", member.getId());
                } else if ("seller".equals(member.getRole())) {
                    session.setAttribute("sellerId", member.getId());
                } else if ("shipper".equals(member.getRole())) {
                    session.setAttribute("shipperId", member.getId());
                } else if ("manager".equals(member.getRole())) {
                    session.setAttribute("managerId", member.getId());
                }
                if (redirect != null && !redirect.isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/" + redirect);
                } else {
                    switch (member.getRole()) {
                        case "customer":
                            response.sendRedirect(request.getContextPath() + "/product?action=list");
                            break;
                        case "manager":
                            response.sendRedirect(request.getContextPath() + "/manager/ManagerView.jsp");
                            break;
                        case "seller":
                            response.sendRedirect(request.getContextPath() + "/order?action=list");
                            break;
                        case "shipper":
                            response.sendRedirect(request.getContextPath() + "/order?action=list");
                            break;
                        default:
                            response.sendRedirect(request.getContextPath() + "/login.jsp");
                            break;
                    }
                }
            } else {
                request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
                request.setAttribute("username", username);
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi đăng nhập: " + e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/login.jsp?message=Đăng xuất thành công");
    }
    private void register(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");
            if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                confirmPassword == null || confirmPassword.trim().isEmpty()) {
                request.setAttribute("error", "Vui lòng điền đầy đủ các trường bắt buộc");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }
            if (!password.equals(confirmPassword)) {
                request.setAttribute("error", "Mật khẩu không khớp");
                request.setAttribute("username", username);
                request.setAttribute("address", address);
                request.setAttribute("phone", phone);
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }
            if (memberDAO.isUsernameExist(username)) {
                request.setAttribute("error", "Tên đăng nhập đã tồn tại");
                request.setAttribute("address", address);
                request.setAttribute("phone", phone);
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }
            String customerId = memberDAO.getNextMemberId();
            com.shopman.model.Customer customer = new com.shopman.model.Customer(
                customerId, null, username, password, address, phone, "customer", null
            );
            boolean success = memberDAO.insertCustomer(customer);
            if (success) {
                HttpSession session = request.getSession();
                session.setAttribute("userId", customerId);
                session.setAttribute("username", username);
                session.setAttribute("role", "customer");
                session.setAttribute("customerId", customerId);
                response.sendRedirect(request.getContextPath() + "/product?action=list&message=Đăng ký thành công");
            } else {
                request.setAttribute("error", "Đăng ký thất bại. Vui lòng thử lại");
                request.setAttribute("username", username);
                request.setAttribute("address", address);
                request.setAttribute("phone", phone);
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi đăng ký: " + e.getMessage());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}