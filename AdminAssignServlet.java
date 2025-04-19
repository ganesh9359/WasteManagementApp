package com.admin;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.sql.*;
import org.json.JSONObject;

@WebServlet("/api/admin/assign")
public class AdminAssignServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;

        while((line = reader.readLine()) != null){
            sb.append(line);
        }

        JSONObject input = new JSONObject(sb.toString());
        int requestId = input.getInt("request_id");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:orcl1", "system", "ganesh9158");

            // Step 1: Get an available vehicle
            String getVehicleSQL = "SELECT VEHICLE_ID FROM vehicles WHERE IS_AVAILABLE = 'Y' FETCH FIRST 1 ROWS ONLY";
            PreparedStatement ps1 = con.prepareStatement(getVehicleSQL);
            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {
                int vehicleId = rs.getInt("VEHICLE_ID");

                // Step 2: Mark vehicle as not available
                String updateVeh = "UPDATE vehicles SET IS_AVAILABLE = 'N' WHERE VEHICLE_ID = ?";
                PreparedStatement ps2 = con.prepareStatement(updateVeh);
                ps2.setInt(1, vehicleId);
                ps2.executeUpdate();

                // Step 3: Assign request to vehicle
                String assignSQL = "INSERT INTO assignments (ASSIGNMENT_ID, REQUEST_ID, VEHICLE_ID, STATUS) VALUES (ASSIGN_SEQ.NEXTVAL, ?, ?, 'Assigned')";
                PreparedStatement ps3 = con.prepareStatement(assignSQL);
                ps3.setInt(1, requestId);
                ps3.setInt(2, vehicleId);
                ps3.executeUpdate();

                response.getWriter().print("{\"status\":\"success\"}");
            } else {
                response.setStatus(400);
                response.getWriter().print("{\"status\":\"error\", \"message\":\"No available vehicles.\"}");
            }

            rs.close();
            ps1.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().print("{\"status\":\"error\", \"message\":\"Server Error.\"}");
        }
    }
}
