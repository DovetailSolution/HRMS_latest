<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.tcs.hr.AttendanceRecord" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Form 16 Request Status</title>
<style>
    body {
        font-family: Arial, sans-serif;
    }
    table {
        width: 90%;
        margin: 30px auto;
        border-collapse: collapse;
        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    }
    th, td {
        border: 1px solid #ddd;
        padding: 10px 15px;
        text-align: center;
    }
    th {
        background-color: #f7f7f7;
    }
    .btn {
        padding: 6px 14px;
        margin: 2px;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        font-weight: bold;
    }
    .approve-btn {
        background-color: #4CAF50;
        color: white;
    }
    .reject-btn {
        background-color: #f44336;
        color: white;
    }
</style>
</head>
<body>

<h2 style="text-align:center;">Form 16 Request Status</h2>

<%
    List<AttendanceRecord> list = (List<AttendanceRecord>) request.getAttribute("form16");
    if (list != null && !list.isEmpty()) {
%>
    <table>
        <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        <%
            for (AttendanceRecord record : list) {
        %>
        <tr>
            <td><%= record.getId() %></td>
            <td><%= record.getUsername() %></td>
            <td><%= record.getForm16request() %></td>
            <td>
			    <form action="form16request" method="post" style="display:inline;">
			        <input type="hidden" name="id" value="<%= record.getId() %>">
			        <!-- <input type="hidden" name="action" value="approveForm16"> -->
			        <button type="submit" class="btn approve-btn" name="decision" value="approved">Approve</button>
			        <button type="submit" class="btn reject-btn" name="decision" value="rejected">Reject</button>
			    </form>
			</td>

        </tr>
        <%
            }
        %>
    </table>
<%
    } else {
%>
    <p style="text-align:center;">No Form 16 requests found.</p>
<%
    }
%>

</body>
</html>
