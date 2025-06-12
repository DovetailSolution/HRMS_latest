<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Notice Board</title>
    <style>
        /* General Body Styling */
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            flex-direction: column;
            min-height: 100vh;
        }

        h1 {
            color: #333;
            margin-bottom: 20px;
        }

        /* Table Styling */
        table {
            border-collapse: collapse;
            width: 80%;
            margin: 20px auto;
            background-color: #fff;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            overflow: hidden;
        }

        th, td {
            text-align: center;
            padding: 12px;
        }

        th {
            background-color: #4CAF50;
            color: white;
            text-transform: uppercase;
            letter-spacing: 1px;
        }

        td {
            border-bottom: 1px solid #ddd;
            color: #555;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        tr:hover {
            background-color: #f1f1f1;
            cursor: pointer;
        }

        /* Footer Styling */
        footer {
            text-align: center;
            margin-top: 20px;
            font-size: 14px;
            color: #777;
        }
    </style>
</head>
<body>

    <h1>Notice Board</h1>

    <table>
        <tr>
            <th>Date</th>
            <th>Message</th>
        </tr>
        <%
            List<Map<String, Object>> noticesList = (List<Map<String, Object>>) request.getAttribute("noticesList");
            if (noticesList != null && !noticesList.isEmpty()) {
                for (Map<String, Object> notice : noticesList) {
        %>
        <tr>
            <td><%= notice.get("Date") %></td>
            <td><%= notice.get("Message") %></td>
        </tr>
        <%
                }
            } else {
        %>
        <tr>
            <td colspan="2">No notices available.</td>
        </tr>
        <%
            }
        %>
    </table>

    <footer>
        &copy; 2024 Notice Board System. All rights reserved.
    </footer>

</body>
</html>
