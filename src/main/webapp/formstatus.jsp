<%@page import="com.tcs.hr.AttendanceRecord"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Form 16 Status</title>
<!-- Google Font -->
<link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">
<style>
    body {
        font-family: 'Roboto', sans-serif;
        background: #f0f2f5;
        margin: 0;
        padding: 20px;
    }

    h2 {
        text-align: center;
        color: #333;
        margin-bottom: 30px;
    }

    table {
        width: 90%;
        margin: auto;
        border-collapse: collapse;
        background: #fff;
        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        border-radius: 8px;
        overflow: hidden;
    }

    th, td {
        padding: 12px 15px;
        text-align: center;
    }

    th {
        background-color: #4CAF50;
        color: white;
        font-size: 16px;
    }

    tr:nth-child(even) {
        background-color: #f2f2f2;
    }

    tr:hover {
        background-color: #e6f7ff;
    }

    a {
        text-decoration: none;
        color: #2196F3;
        font-weight: bold;
        transition: color 0.3s ease;
    }

    a:hover {
        color: #0b7dda;
    }

    .message {
        text-align: center;
        font-size: 18px;
        color: #555;
        margin-top: 40px;
    }
</style>
</head>
<body>
    <h2>Form 16 Request Status</h2>
	<%
		List<AttendanceRecord> list = (List<AttendanceRecord>) request.getAttribute("formstatus");
		if (list != null && !list.isEmpty()) {
	%>
	<table>
		<tr>
			<th>EmpId</th>
			<th>Username</th>
			<th>Status</th>
			<th>Action</th>
		</tr>
		<%
            for (AttendanceRecord record : list) {
        %>
			<tr>
				<td><%= record.getEmpId() %></td>
				<td><%= record.getUsername() %></td>
				<td><%= record.getFormStatus() %></td>
				<td>
					<%
						if ("Approved".equalsIgnoreCase(record.getFormStatus())) {
					%>
						<a href="DownloadForm16Servlet?empId=<%= record.getEmpId() %>">Download Form 16</a>
					<%
						} else {
					%>
						<span style="color: #FF5722; font-weight: bold;">Wait for accountant approval</span>
					<%
						}
					%>
				</td>
			</tr>
		<%
            }
        %>
	</table>
	<%
		} else {
	%>
		<p class="message">No Form 16 records available at the moment.</p>
	<%
		}
	%>
</body>
</html>
