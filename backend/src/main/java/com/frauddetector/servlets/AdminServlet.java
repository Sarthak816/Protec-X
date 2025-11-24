
package com.frauddetector.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "AdminServlet", urlPatterns = {"/api/admin/ping"})
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.getWriter().write("{\"status\":\"OK\",\"message\":\"Admin API reachable\"}") ;
    }
}
