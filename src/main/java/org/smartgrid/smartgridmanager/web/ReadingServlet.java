package org.smartgrid.smartgridmanager.web;

import org.smartgrid.smartgridmanager.dao.DeviceDao;
import org.smartgrid.smartgridmanager.dao.ReadingDao;
import org.smartgrid.smartgridmanager.model.Device;
import org.smartgrid.smartgridmanager.model.Reading;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/readings")
public class ReadingServlet extends HttpServlet {
    private final ReadingDao readingDao = new ReadingDao();
    private final DeviceDao deviceDao = new DeviceDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Device> devices = deviceDao.findAll();
        req.setAttribute("devices", devices);

        String deviceIdParam = req.getParameter("deviceId");
        String typeParam = req.getParameter("type");

        List<Reading> readings;

        // 🔸 Logique de filtrage simple et claire
        if (deviceIdParam != null && !deviceIdParam.isEmpty() &&
                typeParam != null && !typeParam.isEmpty()) {
            readings = readingDao.findByDeviceAndType(Long.parseLong(deviceIdParam), typeParam);

        } else if (deviceIdParam != null && !deviceIdParam.isEmpty()) {
            readings = readingDao.findByDevice(Long.parseLong(deviceIdParam));

        } else if (typeParam != null && !typeParam.isEmpty()) {
            readings = readingDao.findByType(typeParam);

        } else {
            readings = readingDao.getRecentReadings(50); // défaut
        }

        // 🔹 On passe les données à la JSP
        req.setAttribute("readings", readings);
        req.setAttribute("selectedDevice", deviceIdParam);
        req.setAttribute("selectedType", typeParam);

        req.getRequestDispatcher("/pages/readings.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long deviceId = Long.parseLong(req.getParameter("deviceId"));
        String type = req.getParameter("type");
        double value = Double.parseDouble(req.getParameter("value"));

        Device d = deviceDao.findById(deviceId);
        Reading r = new Reading(LocalDateTime.now(), value, type, d);
        readingDao.save(r);

        resp.sendRedirect(req.getContextPath() + "/readings");
    }
}
