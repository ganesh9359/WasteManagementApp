package com.waste.controller;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.sql.*;
import com.waste.util.DBConnection;

@SuppressWarnings("serial")
@WebServlet("/register")
public class User_register extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullName = request.getParameter("full_name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO users(FULL_NAME, email, password) VALUES (?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, fullName);
            pst.setString(2, email);
            pst.setString(3, password);

            int result = pst.executeUpdate();
            if (result > 0) {
                response.sendRedirect("user_login.html?register=success");
            } else {
                response.sendRedirect("User_register.html?register=fail");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

