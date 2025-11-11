package com.shopman.dao;
import com.shopman.model.Member;
import com.shopman.model.Customer;
import com.shopman.model.Seller;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class MemberDAO extends DAO {
    public Member getMemberById(String id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Member member = null;
        try {
            conn = getConnection();
            String sql = "SELECT * FROM tblMember WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                member = new Member(
                    rs.getString("id"),
                    null,
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("addr"),
                    rs.getString("phone"),
                    rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, ps, rs);
        }
        return member;
    }
    public Member login(String username, String password) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Member member = null;
        try {
            conn = getConnection();
            String sql = "SELECT * FROM tblMember WHERE username = ? AND password = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                member = new Member(
                    rs.getString("id"),
                    null,
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("addr"),
                    rs.getString("phone"),
                    rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, ps, rs);
        }
        return member;
    }
    public boolean insertMember(Member member) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            String sql = "INSERT INTO tblMember (id, username, password, addr, phone, role) VALUES (?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, member.getId());
            ps.setString(2, member.getUsername());
            ps.setString(3, member.getPassword());
            ps.setString(4, member.getAdd());
            ps.setString(5, member.getPhone());
            ps.setString(6, member.getRole());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn, ps);
        }
    }
    public boolean isUsernameExist(String username) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "SELECT COUNT(*) as count FROM tblMember WHERE username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, ps, rs);
        }
        return false;
    }
    public String getNextMemberId() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = "SELECT id FROM tblMember ORDER BY id DESC LIMIT 1";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                String lastId = rs.getString("id");
                int number = 0;
                try {
                    number = Integer.parseInt(lastId);
                } catch (NumberFormatException e) {
                    String numericPart = lastId.replaceAll("[^0-9]", "");
                    if (!numericPart.isEmpty()) {
                        number = Integer.parseInt(numericPart);
                    }
                }
                return String.format("%05d", number + 1);
            } else {
                return "00001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return String.format("%05d", (int)(Math.random() * 100000));
        } finally {
            closeConnection(conn, ps, rs);
        }
    }
    public boolean insertCustomer(Customer customer) {
        Connection conn = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false); 
            String sql1 = "INSERT INTO tblMember (id, username, password, addr, phone, role) VALUES (?, ?, ?, ?, ?, ?)";
            ps1 = conn.prepareStatement(sql1);
            ps1.setString(1, customer.getId());
            ps1.setString(2, customer.getUsername());
            ps1.setString(3, customer.getPassword());
            ps1.setString(4, customer.getAdd());
            ps1.setString(5, customer.getPhone());
            ps1.setString(6, "customer");
            ps1.executeUpdate();
            String sql2 = "INSERT INTO tblCustomer (id) VALUES (?)";
            ps2 = conn.prepareStatement(sql2);
            ps2.setString(1, customer.getId());
            ps2.executeUpdate();
            conn.commit(); 
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (ps1 != null) ps1.close();
                if (ps2 != null) ps2.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean insertSeller(Seller seller) {
        Connection conn = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            String sql1 = "INSERT INTO tblMember (id, username, password, addr, phone, role) VALUES (?, ?, ?, ?, ?, ?)";
            ps1 = conn.prepareStatement(sql1);
            ps1.setString(1, seller.getId());
            ps1.setString(2, seller.getUsername());
            ps1.setString(3, seller.getPassword());
            ps1.setString(4, seller.getAdd());
            ps1.setString(5, seller.getPhone());
            ps1.setString(6, "seller");
            ps1.executeUpdate();
            String sql2 = "INSERT INTO tblSeller (id) VALUES (?)";
            ps2 = conn.prepareStatement(sql2);
            ps2.setString(1, seller.getId());
            ps2.executeUpdate();
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (ps1 != null) ps1.close();
                if (ps2 != null) ps2.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean insertManager(String id, String username, String password, String addr, String phone) {
        Connection conn = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            String sql1 = "INSERT INTO tblMember (id, username, password, addr, phone, role) VALUES (?, ?, ?, ?, ?, ?)";
            ps1 = conn.prepareStatement(sql1);
            ps1.setString(1, id);
            ps1.setString(2, username);
            ps1.setString(3, password);
            ps1.setString(4, addr);
            ps1.setString(5, phone);
            ps1.setString(6, "manager");
            ps1.executeUpdate();
            String sql2 = "INSERT INTO tblManager (id) VALUES (?)";
            ps2 = conn.prepareStatement(sql2);
            ps2.setString(1, id);
            ps2.executeUpdate();
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (ps1 != null) ps1.close();
                if (ps2 != null) ps2.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}