package com.waste.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@SuppressWarnings("serial")
@WebServlet("/api/user-dashboard")
public class UserDashboard extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        int totalRequests = 0;
        String latestStatus = "No Requests";

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:orcl1", "system", "ganesh9158"
            );

            PreparedStatement ps1 = conn.prepareStatement("SELECT COUNT(*) FROM waste_requests WHERE user_id = ?");
            ps1.setInt(1, 101); // demo user
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) totalRequests = rs1.getInt(1);
            rs1.close();
            ps1.close();

            PreparedStatement ps2 = conn.prepareStatement(
                "SELECT status FROM waste_requests WHERE user_id = ? ORDER BY request_date DESC FETCH FIRST 1 ROWS ONLY"
            );
            ps2.setInt(1, 101);
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) latestStatus = rs2.getString("status");
            rs2.close();
            ps2.close();

            conn.close();
        } catch (Exception e) {
            latestStatus = "Error";
        }

        String json = String.format("{\"totalRequests\": %d, \"latestStatus\": \"%s\"}", totalRequests, latestStatus);
        response.getWriter().write(json);
    }
}
