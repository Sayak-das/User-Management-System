package com.admin.UserManagement.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.sql.*;
import com.admin.UserManagement.dao.*;
import com.admin.UserManagement.bean.*;
/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private UserDao userDAO;
  
	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(){
		userDAO=new UserDao();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action=request.getServletPath();
		try {
			switch(action) {
				case "/new":
					showNewForm(request, response);
				case "/insert":
					insertUser(request,response);
					break;
				case "/delete":
					deleteUser(request,response);
					break;
				case "/edit":
					showEditForm(request,response);
					break;
				case "/update":
					updateUser(request,response);
					break;
				default:
					listUser(request,response);
			}
		}
		catch(SQLException ex) {
			throw new ServletException(ex);
		}
		//response.getWriter().append("Served at: ").append(request.getContextPath());
 catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	private void listUser(HttpServletRequest request, HttpServletResponse response) 
			throws Exception{
		List<User> listUser=userDAO.selectAllUsers();
		request.setAttribute("listUser", listUser);
		RequestDispatcher dispatcher =request.getRequestDispatcher("user-list.jsp");
		dispatcher.forward(request, response);
		
	}
	private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws Exception{
		RequestDispatcher dispatcher =request.getRequestDispatcher("user-form.jsp");
		dispatcher.forward(request,response);
	}
	private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
			throws Exception{ 
		int id=Integer.parseInt(request.getParameter("id"));
		User existingUser=userDAO.selectUser(id);
		RequestDispatcher dispatcher=request.getRequestDispatcher("user-form.jsp");
		request.setAttribute("user", existingUser);
		dispatcher.forward(request, response);
	}
	private void insertUser(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String name=request.getParameter("name");
		String email=request.getParameter("email");
		String country=request.getParameter("country");
		User newUser=new User(name,email,country);
		userDAO.insertUser(newUser);
		response.sendRedirect("List");
	}
	private void updateUser(HttpServletRequest request, HttpServletResponse response) throws Exception{
		int id=Integer.parseInt(request.getParameter("id"));
		String name=request.getParameter("name");
		String email=request.getParameter("email");
		String country=request.getParameter("country");
		
		User book=new User(id,name,email,country);;
		userDAO.updateUser(book);
		response.sendRedirect("List");
	}
	private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws Exception{
		int id=Integer.parseInt(request.getParameter("id"));
		userDAO.deleteUser(id);
		response.sendRedirect("List");
	}
}
	


