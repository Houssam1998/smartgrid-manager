package org.smartgrid.smartgridmanager.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// 🔹 Ce servlet prend le contrôle de la racine "/"
@WebServlet("/")
public class LandingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Il transfère simplement vers la nouvelle page landing.jsp
        req.getRequestDispatcher("/landing.jsp").forward(req, resp);
    }
}