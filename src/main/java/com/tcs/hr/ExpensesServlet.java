package com.tcs.hr;

import java.io.File;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.json.JSONObject;


@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB before storing to disk
	    maxFileSize = 1024 * 1024 * 20,       // 20MB max file size
	    maxRequestSize = 1024 * 1024 * 25     // 25MB max request size
	)


@WebServlet(name = "expense", urlPatterns = { "/expense" })
public class ExpensesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ExpensesServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        boolean isApiRequest = "XMLHttpRequest".equalsIgnoreCase(request.getHeader("expense"));
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject jsonResponse = new JSONObject();

        try {
            // Retrieve form parameters
            String travelRoute = request.getParameter("travelRoute");
            String date = request.getParameter("date");
            String timeFrom = request.getParameter("timeFrom");
            String timeTo = request.getParameter("timeTo");
            String purpose = request.getParameter("purpose");
            String projectName = request.getParameter("projectName");
            String expensesIncurred = request.getParameter("expensesIncurred");
            String advanceTaken = request.getParameter("advanceTaken");
            String mode = request.getParameter("mode");
            String ticketNo = request.getParameter("ticketNo");
            String ticketDate = request.getParameter("ticketDate");

            // Retrieve session data
            HttpSession session = request.getSession();
            String username = (String) session.getAttribute("user");
            String empId = (String) session.getAttribute("empid");

            Part filePart = request.getPart("attachment");        //get attachment from jsp
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            
            // Save in WebApp Directory
            String webAppUploadDir = getServletContext().getRealPath("/") + "uploads";
            File webAppDir = new File(webAppUploadDir);
            
            if (!webAppDir.exists()) 
            {
                webAppDir.mkdirs();
            }
            
            String webAppUploadPath = webAppUploadDir + File.separator + fileName;
            filePart.write(webAppUploadPath);

            // Save in C:/uploads/
            String externalUploadDir = "C:/uploads";
            File externalDir = new File(externalUploadDir);
            
            if (!externalDir.exists())
            {
                externalDir.mkdirs();
            }
            
            String externalUploadPath = externalUploadDir + File.separator + fileName;
            filePart.write(externalUploadPath);

            // Store only relative path for web access
            String fileUrl = "uploads/" + fileName;

            // Database connection and insertion
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db", "root", "manager")) {
                String sql = "INSERT INTO expenses(empId, username, travel_route, date, time_from, time_to, "
                           + "purpose, project, expenses_incurred, advance_taken, mode, ticket_no, ticket_date, attachment_path) "
                           + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, empId);
                    ps.setString(2, username);
                    ps.setString(3, travelRoute);
                    ps.setString(4, date);
                    ps.setString(5, timeFrom);
                    ps.setString(6, timeTo);
                    ps.setString(7, purpose);
                    ps.setString(8, projectName);
                    ps.setString(9, expensesIncurred);
                    ps.setString(10, advanceTaken);
                    ps.setString(11, mode);
                    ps.setString(12, ticketNo);
                    ps.setString(13, ticketDate);
                    ps.setString(14, fileUrl);

                    int rowsInserted = ps.executeUpdate();

                    if (rowsInserted > 0) {
                        jsonResponse.put("status", "success");
                        jsonResponse.put("message", "Expense record inserted successfully!");
                        jsonResponse.put("empId", empId);
                        jsonResponse.put("username", username);
                        jsonResponse.put("date", date);
                    } else {
                        jsonResponse.put("status", "failure");
                        jsonResponse.put("message", "Failed to insert expense record.");
                    }
                }
            }
        } catch (Exception e) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", e.getMessage());
            e.printStackTrace();
        }

        // Return JSON response for API requests
        if (isApiRequest) {
            response.getWriter().write(jsonResponse.toString());
        } else {
            // Redirect for normal form submissions
            response.sendRedirect("expense.jsp");
        }
    }
}
