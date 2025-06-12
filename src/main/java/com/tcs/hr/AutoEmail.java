package com.tcs.hr;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "autoEmail", urlPatterns = { "/autoEmail" })
public class AutoEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AutoEmail() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	 private static final String OPENAI_API_KEY = "AIzaSyDaT_jIca3sTYhLxPqNpZSrGMZN60fk9EU"; // Add your API key

	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
	        String emailContext = request.getParameter("emailContext");

	        String prompt = "Write a professional email based on the following:\n" + emailContext;

	        // Send prompt to OpenAI
	        String emailBody = OpenAIUtil.generateEmail(prompt);  // Call your utility

	        // Download as .txt file
	        response.setContentType("text/plain");
	        response.setHeader("Content-Disposition", "attachment;filename=generated_email.txt");

	        try (PrintWriter out = response.getWriter()) {
	            out.print(emailBody);
	        }
	    }

}
