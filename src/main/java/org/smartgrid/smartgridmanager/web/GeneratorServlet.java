package org.smartgrid.smartgridmanager.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/generator")
public class GeneratorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Afficher la page du générateur
        req.getRequestDispatcher("/pages/generator.jsp").forward(req, resp);
    }
}