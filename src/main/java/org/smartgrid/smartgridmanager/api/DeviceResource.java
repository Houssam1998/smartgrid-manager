package org.smartgrid.smartgridmanager.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.smartgrid.smartgridmanager.dao.DeviceDao;
import org.smartgrid.smartgridmanager.model.Device;

import java.util.List;
import java.util.stream.Collectors;

// 1. @Path : Définit l'URL de base pour cette classe
//    (Résultat -> /api/devices)
@Path("/devices")
public class DeviceResource {

    // On réutilise le DAO que vous avez déjà fait !
    private DeviceDao deviceDao = new DeviceDao();

    // 2. @GET : Cette méthode répond aux requêtes HTTP GET
    // 3. @Produces : Cette méthode renvoie du JSON
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DeviceDTO> getAllDevices() {

        // 4. On récupère les entités de la BDD
        List<Device> deviceEntities = deviceDao.findAll();

        // 5. On convertit la liste d'Entités en liste de DTOs
        // C'est ce qui évite les erreurs de boucle infinie et de LazyLoading
        List<DeviceDTO> deviceDTOs = deviceEntities.stream()
                .map(device -> new DeviceDTO(device)) // Pour chaque 'device', crée un 'new DeviceDTO(device)'
                .collect(Collectors.toList());

        // 6. Jersey et Jackson s'occupent de transformer cette liste en JSON
        return deviceDTOs;
    }
}