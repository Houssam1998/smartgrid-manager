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

        // 🔹 AJOUTÉ : Gérer le message de succès de la session (après redirection)
        HttpSession session = req.getSession(false); // Ne pas créer de nouvelle session
        if (session != null) {
            String message = (String) session.getAttribute("message");
            if (message != null) {
                req.setAttribute("message", message);
                // Retirer le message pour qu'il n'apparaisse qu'une fois
                session.removeAttribute("message");
            }
        }

        // Afficher la page du générateur
        req.getRequestDispatcher("/pages/generator.jsp").forward(req, resp);
    }
}