package com.waste.controller;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.sql.*;
import com.waste.util.DBConnection;

@SuppressWarnings("serial")
@WebServlet("/userLogin")
public class User_login extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uname = request.getParameter("uname");
        String password = request.getParameter("psw");

        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, uname);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                HttpSession session = request.getSession();
                session.setAttribute("username", rs.getString("email"));
                response.sendRedirect("user_dashboard.html");
            } else {
                response.sendRedirect("login.html?error=invalid");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
