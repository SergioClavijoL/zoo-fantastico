package com.zoo.zoo_fantastico;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ZoneServiceTest {

    @Mock
    private ZoneRepository zoneRepository;

    @InjectMocks
    private ZoneService zoneService;

    private Zone zone;

    @BeforeEach
    void configuracionInicial() {
        zone = new Zone();
        zone.setId(1L);
        zone.setName("Zona Volcánica");
        zone.setDescription("Zona de alta temperatura");
        zone.setCapacity(10);
        zone.setCreatures(Collections.emptyList());
    }

    @Test
    void crearZona_debeRetornarZonaGuardada() {
        when(zoneRepository.save(any(Zone.class))).thenReturn(zone);

        Zone resultado = zoneService.createZone(zone);

        assertNotNull(resultado);
        assertEquals("Zona Volcánica", resultado.getName());
    }

    @Test
    void obtenerZonaPorId_debeRetornarZona() {
        when(zoneRepository.findById(1L)).thenReturn(Optional.of(zone));

        Zone resultado = zoneService.getZoneById(1L);

        assertNotNull(resultado);
        assertEquals("Zona Volcánica", resultado.getName());
    }

    @Test
    void obtenerZonaPorId_debeLanzarExcepcion_CuandoNoExiste() {
        when(zoneRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> zoneService.getZoneById(99L));
    }

    @Test
    void actualizarZona_debeRetornarZonaActualizada() {
        Zone datosNuevos = new Zone();
        datosNuevos.setName("Zona Ártica");
        datosNuevos.setDescription("Zona fría");
        datosNuevos.setCapacity(5);

        when(zoneRepository.findById(1L)).thenReturn(Optional.of(zone));
        when(zoneRepository.save(any(Zone.class))).thenReturn(zone);

        Zone resultado = zoneService.updateZone(1L, datosNuevos);

        assertNotNull(resultado);
        verify(zoneRepository, times(1)).save(zone);
    }

    @Test
    void eliminarZona_debeEliminar_CuandoNoTieneCriaturas() {
        zone.setCreatures(Collections.emptyList());
        when(zoneRepository.findById(1L)).thenReturn(Optional.of(zone));

        assertDoesNotThrow(() -> zoneService.deleteZone(1L));
        verify(zoneRepository, times(1)).delete(zone);
    }

    @Test
    void eliminarZona_debeLanzarExcepcion_CuandoTieneCriaturas() {
        Creature creature = new Creature();
        creature.setName("Fénix");
        zone.setCreatures(List.of(creature));

        when(zoneRepository.findById(1L)).thenReturn(Optional.of(zone));

        IllegalStateException excepcion = assertThrows(IllegalStateException.class,
                () -> zoneService.deleteZone(1L));

        assertEquals("No se puede eliminar una zona que tiene criaturas asignadas",
                excepcion.getMessage());
        verify(zoneRepository, never()).delete(any());
    }
}