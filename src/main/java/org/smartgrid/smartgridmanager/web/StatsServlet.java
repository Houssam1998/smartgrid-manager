package org.smartgrid.smartgridmanager.web;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

import org.smartgrid.smartgridmanager.dao.StatsDao;

@WebServlet("/stats")
public class StatsServlet extends HttpServlet {

    private final StatsDao statsDao = new StatsDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Object[]> stats = statsDao.getStats();
        List<Object[]> counts = statsDao.getCounts();

        List<Object[]> avgPower = statsDao.getAveragePowerByDevice();
        List<Object[]> avgTemp = statsDao.getAverageTemperatureByDevice();
        List<Object[]> alerts = statsDao.getAlerts();

        req.setAttribute("stats", stats);
        req.setAttribute("counts", counts);
        req.setAttribute("avgPower", avgPower);
        req.setAttribute("avgTemp", avgTemp);
        req.setAttribute("alerts", alerts);




        req.getRequestDispatcher("pages/stats.jsp").forward(req, resp);
    }
}
