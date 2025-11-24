
package com.frauddetector.servlets;

import com.frauddetector.dao.TransactionDAO;
import com.frauddetector.model.Transaction;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet(name = "TransactionServlet", urlPatterns = {"/api/transactions"})
public class TransactionServlet extends HttpServlet {

    private TransactionDAO transactionDAO = new TransactionDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp) throws IOException {
        // return recent pending transactions as JSON
        try {
            List<Transaction> list = transactionDAO.getRecentPendingTransactions(50);
            resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(list));
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write(gson.toJson(java.util.Map.of("status","ERROR","message",e.getMessage())));
        }
    }

    @Override
    protected void doPost(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp) throws IOException {
        // Accept JSON to create a transaction: { "userId":1, "amount":100.0 }
        BufferedReader reader = req.getReader();
        var map = gson.fromJson(reader, java.util.Map.class);
        double amount = ((Number)map.get("amount")).doubleValue();
        long userId = ((Number)map.get("userId")).longValue();
        Transaction t = new Transaction();
        t.setUserId(userId);
        t.setAmount(amount);
        t.setTimestamp(new Timestamp(System.currentTimeMillis()));
        t.setStatus("PENDING");
        try {
            transactionDAO.saveTransaction(t);
            resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(java.util.Map.of("status","OK","id",t.getId())));
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write(gson.toJson(java.util.Map.of("status","ERROR","message",e.getMessage())));
        }
    }
}
