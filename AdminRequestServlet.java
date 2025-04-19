package com.admin;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.*;
import java.sql.*;
import org.json.JSONArray;
import org.json.JSONObject;

@SuppressWarnings("serial")
@WebServlet("/api/admin/requests")
public class AdminRequestServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        JSONArray requestsArray = new JSONArray();

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:orcl1", "system", "ganesh9158");

            String sql = "SELECT REQUEST_ID, LOCATION, STATUS, LATITUDE, LONGITUDE FROM waste_requests";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("request_id", rs.getInt("REQUEST_ID"));
                obj.put("location", rs.getString("LOCATION"));
                obj.put("status", rs.getString("STATUS"));
                obj.put("lat", rs.getDouble("LATITUDE"));
                obj.put("lng", rs.getDouble("LONGITUDE"));
                requestsArray.put(obj);
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.getWriter().print(requestsArray.toString());
    }
}
