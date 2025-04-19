package com.waste.controller;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.*;
import java.sql.*;

@SuppressWarnings("serial")
@WebServlet("/api/request-pickup")
public class PickupRequestServlet extends HttpServlet {

    // This method handles the POST request for submitting the pickup request
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Retrieve the parameters from the request (location, latitude, and longitude)
        String location = request.getParameter("location");
        double lat = Double.parseDouble(request.getParameter("lat"));
        double lng = Double.parseDouble(request.getParameter("lng"));

        // Log the values (for debugging purposes)
        System.out.println("Location: " + location);
        System.out.println("Latitude: " + lat);
        System.out.println("Longitude: " + lng);

        // Insert data into Oracle DB (make sure to replace with your actual DB credentials)
        try {
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "your_username", "your_password");
            String query = "INSERT INTO waste_requests (location, lat, lng) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, location);
            ps.setDouble(2, lat);
            ps.setDouble(3, lng);
            ps.executeUpdate();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Send a response back to the frontend with a success message
        response.setContentType("text/plain");
        response.getWriter().write("Pickup request submitted successfully!");
    }
}
