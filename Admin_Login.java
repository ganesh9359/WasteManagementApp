package com.waste.controller;

import java.io.*; 
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.sql.*;
import com.waste.util.DBConnection;
@SuppressWarnings("serial")
@WebServlet("/adminlogin")
public class Admin_Login extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("uname");
        String password = request.getParameter("pass");

        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM admin WHERE username=? AND password=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);
            PrintWriter pw=response.getWriter();
            response.setContentType("text/html");
            
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                HttpSession session = request.getSession();
                session.setAttribute("admin", rs.getString("username"));
                response.sendRedirect("adminDashboard.html");
            } else {
                response.sendRedirect("admin_login.html?error=invalid");
                 pw.print("<h4>Invaid User Name or Password...!<h4>");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
