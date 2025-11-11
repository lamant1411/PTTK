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

/**
 * Servlet for handling user authentication
 * Supports: login, logout, register
 */
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
    
    /**
     * Show login page
     */
    private void showLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    /**
     * Show register page
     */
    private void showRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
    
    /**
     * Process login
     */
    private void login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String redirect = request.getParameter("redirect");
        
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            
            request.setAttribute("error", "Username and password are required");
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
                
                // Set specific role-based IDs
                if ("customer".equals(member.getRole())) {
                    session.setAttribute("customerId", member.getId());
                } else if ("seller".equals(member.getRole())) {
                    session.setAttribute("sellerId", member.getId());
                } else if ("shipper".equals(member.getRole())) {
                    session.setAttribute("shipperId", member.getId());
                } else if ("manager".equals(member.getRole())) {
                    session.setAttribute("managerId", member.getId());
                }
                
                // Redirect based on role or redirect parameter
                if (redirect != null && !redirect.isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/" + redirect);
                } else {
                    // Redirect theo vai trò
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
                request.setAttribute("error", "Invalid username or password");
                request.setAttribute("username", username);
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Login error: " + e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
    
    /**
     * Process logout
     */
    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            session.invalidate();
        }
        
        response.sendRedirect(request.getContextPath() + "/login.jsp?message=Logged out successfully");
    }
    
    /**
     * Process registration
     */
    private void register(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");
            
            // Validation
            if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                confirmPassword == null || confirmPassword.trim().isEmpty()) {
                
                request.setAttribute("error", "All required fields must be filled");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                request.setAttribute("error", "Passwords do not match");
                request.setAttribute("username", username);
                request.setAttribute("address", address);
                request.setAttribute("phone", phone);
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }
            
            // Check if username exists
            if (memberDAO.isUsernameExist(username)) {
                request.setAttribute("error", "Username already exists");
                request.setAttribute("address", address);
                request.setAttribute("phone", phone);
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }
            
            // Create new customer with auto-increment member ID (00001, 00002, ...)
            // All users are members, differentiated by role
            String customerId = memberDAO.getNextMemberId();
            com.shopman.model.Customer customer = new com.shopman.model.Customer(
                customerId, null, username, password, address, phone, "customer", null
            );
            
            boolean success = memberDAO.insertCustomer(customer);
            
            if (success) {
                // Auto login after registration
                HttpSession session = request.getSession();
                session.setAttribute("userId", customerId);
                session.setAttribute("username", username);
                session.setAttribute("role", "customer");
                session.setAttribute("customerId", customerId);
                
                response.sendRedirect(request.getContextPath() + "/product?action=list&message=Registration successful");
            } else {
                request.setAttribute("error", "Failed to register. Please try again.");
                request.setAttribute("username", username);
                request.setAttribute("address", address);
                request.setAttribute("phone", phone);
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Registration error: " + e.getMessage());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}
