package org.example;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/adduser")
public class userHandler extends HttpServlet {

    private Connection conn;

    public void init(ServletConfig var1) throws ServletException{
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/java";
            conn = DriverManager.getConnection(url, "postgres", "123");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String group = req.getParameter("group");

        // add to DB

        try {
            PreparedStatement stm = conn.prepareStatement("INSERT INTO usuarios (name, password, group_num) VALUES (?, ?, ?);");
            stm.setString(1, username);
            stm.setString(2, password);req.getRequestDispatcher("usershow.jsp").forward(req,resp);
            stm.setString(3, group);
            int rows = stm.executeUpdate();

            if(rows > 0){
                req.setAttribute("username", username);
                req.setAttribute("group", group);
                req.getRequestDispatcher("usershow.jsp").forward(req,resp);
            }
            else{
                resp.getWriter().println("<h1>Something went wrong</h1>");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
