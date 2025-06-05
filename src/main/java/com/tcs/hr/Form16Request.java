package com.tcs.hr;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.cj.x.protobuf.MysqlxPrepare.Prepare;

@WebServlet(name = "form16request", urlPatterns = { "/form16request" })
public class Form16Request extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Form16Request() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		String empid=(String) session.getAttribute("empid");
		
		String action=request.getParameter("action");
		
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db","root","manager");
			
			if("form16status".equals(action))
			{
			PreparedStatement ps=con.prepareStatement("select * from form16request");
			ResultSet rs = ps.executeQuery();
			
			List<AttendanceRecord> requests = new ArrayList<>();
			while(rs.next())
			{
				AttendanceRecord requestRecord=new AttendanceRecord();
				requestRecord.setId(rs.getInt("id"));
				requestRecord.setUsername(rs.getString("username"));
				requestRecord.setForm16request(rs.getString("approval_status"));
			
				
				requests.add(requestRecord);
			}
				request.setAttribute("form16", requests);
				request.getRequestDispatcher("form16status.jsp").forward(request, response);
		} 
			else if("formstatus".equals(action))
			{
				PreparedStatement ps=con.prepareStatement("select * from form16request where empId=?");
				ps.setString(1, empid);
				
				ResultSet rs = ps.executeQuery();
				
				List<AttendanceRecord> records=new ArrayList<AttendanceRecord>();
				while (rs.next())
				{
					AttendanceRecord record=new AttendanceRecord();
					record.setEmpId(rs.getString("empId"));
					record.setUsername(rs.getString("username"));
					record.setFormStatus(rs.getString("approval_status"));
					
					records.add(record);
				}
				
				request.setAttribute("formstatus", records);
				request.getRequestDispatcher("formstatus.jsp").forward(request, response);
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		String empid =(String) session.getAttribute("empid");
		String username=(String) session.getAttribute("user");
		String action=request.getParameter("action");
		String act=request.getParameter("decision");
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance_db","root","manager");
			
			if("form16".equals(action))
			{
			PreparedStatement ps=con.prepareStatement("insert into form16request(empId, username, approval_status)values(?,?,?)");
			ps.setString(1, empid);
			ps.setString(2, username);
			ps.setString(3, "pending");
			
			ps.executeUpdate();
			ps.close();
			
			session.setAttribute("statusMessage", "Form 16 request sent to Accountant.");
            response.sendRedirect("dashboard.jsp");
			
			}
			else if("approved".equals(act)) 
			{
			    String id = request.getParameter("id");
			    PreparedStatement ps = con.prepareStatement("UPDATE form16request SET approval_status=? WHERE id=?");
			    ps.setString(1, "approved");
			    ps.setString(2, id);
			    ps.executeUpdate();
			    ps.close();
			    response.sendRedirect("form16request?action=form16status");
			}
			else if("rejected".equals(act))
			{
				String id = request.getParameter("id");
			    PreparedStatement ps = con.prepareStatement("UPDATE form16request SET approval_status=? WHERE id=?");
			    ps.setString(1, "rejected");
			    ps.setString(2, id);
			    ps.executeUpdate();
			    ps.close();
			    response.sendRedirect("form16request?action=form16status");
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
