package org.smartgrid.smartgridmanager.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam; // Nouvel import pour les URLs dynamiques
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.smartgrid.smartgridmanager.dao.ReadingDao; // On utilise le ReadingDao
import org.smartgrid.smartgridmanager.model.Reading;

import java.util.List;
import java.util.stream.Collectors;

@Path("/readings") // URL de base : /api/readings
public class ReadingResource {

    private ReadingDao readingDao = new ReadingDao();

    /**
     * Endpoint 1 : Récupère les 100 lectures les plus récentes
     * URL: GET /api/readings
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReadingDTO> getRecentReadings() {

        // IMPORTANT : Ne jamais appeler findAll() sur les readings,
        // ce serait des millions de lignes.
        // On réutilise la méthode du DAO que vous avez déjà :
        List<Reading> readingEntities = readingDao.getRecentReadings(100);

        // On convertit la liste d'Entités en liste de DTOs
        return readingEntities.stream()
                .map(ReadingDTO::new) // Raccourci pour 'reading -> new ReadingDTO(reading)'
                .collect(Collectors.toList());
    }

    /**
     * Endpoint 2 : Récupère les lectures pour un ID de device spécifique
     * URL: GET /api/readings/device/5 (par exemple)
     */
    @GET
    @Path("/device/{deviceId}") // L'URL contiendra l'ID
    @Produces(MediaType.APPLICATION_JSON)
    // @PathParam lie le "{deviceId}" de l'URL à la variable "id"
    public List<ReadingDTO> getReadingsByDevice(@PathParam("deviceId") Long id) {

        // On réutilise une autre méthode de votre DAO
        List<Reading> readingEntities = readingDao.findByDevice(id);

        // On convertit de la même manière
        return readingEntities.stream()
                .map(ReadingDTO::new)
                .collect(Collectors.toList());
    }
}