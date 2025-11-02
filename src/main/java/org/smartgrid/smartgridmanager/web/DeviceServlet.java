package org.smartgrid.smartgridmanager.web;


import org.smartgrid.smartgridmanager.dao.DeviceDao;
import org.smartgrid.smartgridmanager.model.Device;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/devices")
public class DeviceServlet extends HttpServlet {
    private final DeviceDao deviceDao = new DeviceDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Device> devices = deviceDao.findAll();
        req.setAttribute("devices", devices);
        req.getRequestDispatcher("pages/devices.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String deleteIdParam = req.getParameter("deleteId");
        if (deleteIdParam != null && !deleteIdParam.isEmpty()) {
            // Suppression
            Long deleteId = Long.parseLong(deleteIdParam);
            deviceDao.delete(deleteId);
        } else {
            // Ajout
            String name = req.getParameter("name");
            String type = req.getParameter("type");
            String location = req.getParameter("location");

            Device d = new Device(name, type, location);
            deviceDao.save(d);
        }

        resp.sendRedirect("devices");
    }
}
