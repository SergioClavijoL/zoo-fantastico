package com.zoo.zoo_fantastico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneService {

    private final ZoneRepository zoneRepository;

    @Autowired
    public ZoneService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    public Zone createZone(Zone zone) {
        return zoneRepository.save(zone);
    }

    public List<Zone> getAllZones() {
        return zoneRepository.findAll();
    }

    public Zone getZoneById(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zona no encontrada"));
    }

    public Zone updateZone(Long id, Zone updatedZone) {
        Zone zone = getZoneById(id);
        zone.setName(updatedZone.getName());
        zone.setDescription(updatedZone.getDescription());
        zone.setCapacity(updatedZone.getCapacity());
        return zoneRepository.save(zone);
    }

    public void deleteZone(Long id) {
        Zone zone = getZoneById(id);
        if (zone.getCreatures() != null && !zone.getCreatures().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar una zona que tiene criaturas asignadas");
        }
        zoneRepository.delete(zone);
    }
}