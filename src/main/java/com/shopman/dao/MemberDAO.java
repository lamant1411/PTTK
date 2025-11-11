package com.shopman.dao;

import com.shopman.model.Member;
import com.shopman.model.Customer;
import com.shopman.model.Seller;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO extends DAO {
    
    /**
     * Get all members
     * @return List of all members
     */
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            String sql = "SELECT * FROM tblMember";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Member member = new Member(
                    rs.getString("id"),
                    null, // name not in tblMember
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("addr"),
                    rs.getString("phone"),
                    rs.getString("role")
                );
                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, ps, rs);
        }
        
        return members;
    }
    
    /**
     * Get member by ID
     * @param id Member ID
     * @return Member object or null if not found
     */
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
    
    /**
     * Get member by username
     * @param username Username
     * @return Member object or null if not found
     */
    public Member getMemberByUsername(String username) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Member member = null;
        
        try {
            conn = getConnection();
            String sql = "SELECT * FROM tblMember WHERE username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
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
    
    /**
     * Login authentication
     * @param username Username
     * @param password Password
     * @return Member object if credentials are valid, null otherwise
     */
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
    
    /**
     * Insert new member
     * @param member Member to insert
     * @return true if successful, false otherwise
     */
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
    
    /**
     * Update member
     * @param member Member to update
     * @return true if successful, false otherwise
     */
    public boolean updateMember(Member member) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = getConnection();
            String sql = "UPDATE tblMember SET username = ?, password = ?, addr = ?, phone = ?, role = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, member.getUsername());
            ps.setString(2, member.getPassword());
            ps.setString(3, member.getAdd());
            ps.setString(4, member.getPhone());
            ps.setString(5, member.getRole());
            ps.setString(6, member.getId());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn, ps);
        }
    }
    
    /**
     * Delete member
     * @param id Member ID
     * @return true if successful, false otherwise
     */
    public boolean deleteMember(String id) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = getConnection();
            String sql = "DELETE FROM tblMember WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn, ps);
        }
    }
    
    /**
     * Get members by role
     * @param role Member role
     * @return List of members with specified role
     */
    public List<Member> getMembersByRole(String role) {
        List<Member> members = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            String sql = "SELECT * FROM tblMember WHERE role = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, role);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Member member = new Member(
                    rs.getString("id"),
                    null,
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("addr"),
                    rs.getString("phone"),
                    rs.getString("role")
                );
                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, ps, rs);
        }
        
        return members;
    }
    
    /**
     * Check if username exists
     * @param username Username to check
     * @return true if exists, false otherwise
     */
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
    
    /**
     * Get next member ID (auto-increment: 00001, 00002, ...)
     * All users are members, differentiated by role
     * @return Next member ID
     */
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
                    // Try to parse as pure number (new format: "00005")
                    number = Integer.parseInt(lastId);
                } catch (NumberFormatException e) {
                    // Old format with letter prefix (e.g., "C2030", "S0001")
                    // Extract numeric part only
                    String numericPart = lastId.replaceAll("[^0-9]", "");
                    if (!numericPart.isEmpty()) {
                        number = Integer.parseInt(numericPart);
                    }
                }
                
                // Increment and format with leading zeros (5 digits)
                return String.format("%05d", number + 1);
            } else {
                // No members yet, start from 00001
                return "00001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // If error, generate random ID
            return String.format("%05d", (int)(Math.random() * 100000));
        } finally {
            closeConnection(conn, ps, rs);
        }
    }
    
    /**
     * Insert customer (includes tblMember and tblCustomer)
     * @param customer Customer to insert
     * @return true if successful, false otherwise
     */
    public boolean insertCustomer(Customer customer) {
        Connection conn = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            // Insert into tblMember
            String sql1 = "INSERT INTO tblMember (id, username, password, addr, phone, role) VALUES (?, ?, ?, ?, ?, ?)";
            ps1 = conn.prepareStatement(sql1);
            ps1.setString(1, customer.getId());
            ps1.setString(2, customer.getUsername());
            ps1.setString(3, customer.getPassword());
            ps1.setString(4, customer.getAdd());
            ps1.setString(5, customer.getPhone());
            ps1.setString(6, "customer");
            ps1.executeUpdate();
            
            // Insert into tblCustomer
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
    
    /**
     * Insert seller (includes tblMember and tblSeller)
     * @param seller Seller to insert
     * @return true if successful, false otherwise
     */
    public boolean insertSeller(Seller seller) {
        Connection conn = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            // Insert into tblMember
            String sql1 = "INSERT INTO tblMember (id, username, password, addr, phone, role) VALUES (?, ?, ?, ?, ?, ?)";
            ps1 = conn.prepareStatement(sql1);
            ps1.setString(1, seller.getId());
            ps1.setString(2, seller.getUsername());
            ps1.setString(3, seller.getPassword());
            ps1.setString(4, seller.getAdd());
            ps1.setString(5, seller.getPhone());
            ps1.setString(6, "seller");
            ps1.executeUpdate();
            
            // Insert into tblSeller
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
    
    /**
     * Insert manager (includes tblMember and tblManager)
     * @param manager Manager to insert
     * @return true if successful, false otherwise
     */
    public boolean insertManager(String id, String username, String password, String addr, String phone) {
        Connection conn = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            // Insert into tblMember
            String sql1 = "INSERT INTO tblMember (id, username, password, addr, phone, role) VALUES (?, ?, ?, ?, ?, ?)";
            ps1 = conn.prepareStatement(sql1);
            ps1.setString(1, id);
            ps1.setString(2, username);
            ps1.setString(3, password);
            ps1.setString(4, addr);
            ps1.setString(5, phone);
            ps1.setString(6, "manager");
            ps1.executeUpdate();
            
            // Insert into tblManager
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
    
    /**
     * Change password
     * @param id Member ID
     * @param newPassword New password
     * @return true if successful, false otherwise
     */
    public boolean changePassword(String id, String newPassword) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = getConnection();
            String sql = "UPDATE tblMember SET password = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, newPassword);
            ps.setString(2, id);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn, ps);
        }
    }
}
