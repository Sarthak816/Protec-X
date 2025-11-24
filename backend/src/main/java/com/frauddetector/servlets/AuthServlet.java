
package com.frauddetector.servlets;

import com.frauddetector.dao.UserDAO;
import com.frauddetector.model.User;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "AuthServlet", urlPatterns = {"/api/auth"})
public class AuthServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();
    private Gson gson = new Gson();

    @Override
    protected void doPost(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp) throws ServletException, IOException {
        // Expecting JSON: { "email":"", "password":"" }
        var reader = req.getReader();
        var data = gson.fromJson(reader, java.util.Map.class);
        String email = (String) data.get("email");
        String password = (String) data.get("password");

        try {
            User u = userDAO.findByEmail(email);
            if (u != null && u.getPassword().equals(password)) {
                // simple session handling
                HttpSession session = req.getSession(true);
                session.setAttribute("userId", u.getId());
                session.setAttribute("role", u.getRole());
                resp.setContentType("application/json");
                resp.getWriter().write(gson.toJson(java.util.Map.of("status","OK","role",u.getRole())));
            } else {
                resp.setStatus(401);
                resp.getWriter().write(gson.toJson(java.util.Map.of("status","FAILED","message","Invalid credentials")));
            }
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write(gson.toJson(java.util.Map.of("status","ERROR","message",e.getMessage())));
        }
    }
}
