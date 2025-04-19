package com.admin;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.sql.*;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/api/admin/vehicles")
public class AdminVehicleServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        JSONArray vehiclesArray = new JSONArray();

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:orcl1", "system", "ganesh9158");

            String sql = "SELECT VEHICLE_ID, DRIVER_NAME FROM vehicles WHERE IS_AVAILABLE = 'Y'";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("vehicle_id", rs.getInt("VEHICLE_ID"));
                obj.put("driver_name", rs.getString("DRIVER_NAME"));
                vehiclesArray.put(obj);
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.getWriter().print(vehiclesArray.toString());
    }
}
