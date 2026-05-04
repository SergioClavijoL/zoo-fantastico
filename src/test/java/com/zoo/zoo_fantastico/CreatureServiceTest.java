package com.zoo.zoo_fantastico;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CreatureServiceTest {

    @Mock
    private CreatureRepository creatureRepository;

    @InjectMocks
    private CreatureService creatureService;

    private Creature creature;

    @BeforeEach
    void configuracionInicial() {
        creature = new Creature();
        creature.setId(1L);
        creature.setName("Fénix");
        creature.setSpecies("Ave Mítica");
        creature.setSize(2.5);
        creature.setDangerLevel(3);
        creature.setHealthStatus("healthy");
    }

    @Test
    void testCreateCreature_ShouldReturnSavedCreature() {
        Creature creature = new Creature();
        creature.setName("Fénix");
        when(creatureRepository.save(any(Creature.class))).thenReturn(creature);
        Creature savedCreature = creatureService.createCreature(creature);
        assertNotNull(savedCreature);
        assertEquals("Fénix", savedCreature.getName());
    }

    @Test
    void obtenerCriaturaPorId_debeRetornarCriatura() {
        // Le decimos al repo falso que cuando busquen id=1, devuelva nuestra criatura
        when(creatureRepository.findById(1L)).thenReturn(java.util.Optional.of(creature));

        // Ejecutamos el método real
        Creature resultado = creatureService.getCreatureById(1L);

        // Verificamos
        assertNotNull(resultado);
        assertEquals("Fénix", resultado.getName());
    }

    @Test
    void obtenerCriaturaPorId_debeLanzarExcepcion_CuandoNoExiste() {
        // Le decimos al repo falso que cuando busquen id=99, no encuentre nada
        when(creatureRepository.findById(99L)).thenReturn(java.util.Optional.empty());

        // Verificamos que lanza el error correcto
        assertThrows(RuntimeException.class,
                () -> creatureService.getCreatureById(99L));
    }

    @Test
    void actualizarCriatura_debeRetornarCriaturaActualizada() {
        // Criatura con los datos nuevos
        Creature datosNuevos = new Creature();
        datosNuevos.setName("Dragón");
        datosNuevos.setSpecies("Reptil Mítico");
        datosNuevos.setSize(5.0);
        datosNuevos.setDangerLevel(9);
        datosNuevos.setHealthStatus("healthy");

        // El repo falso primero encuentra la criatura, luego la guarda
        when(creatureRepository.findById(1L)).thenReturn(java.util.Optional.of(creature));
        when(creatureRepository.save(any(Creature.class))).thenReturn(creature);

        // Ejecutamos
        Creature resultado = creatureService.updateCreature(1L, datosNuevos);

        // Verificamos
        assertNotNull(resultado);
        verify(creatureRepository, times(1)).save(creature);
    }

    @Test
    void eliminarCriatura_debeEliminar_CuandoEstadoNoEsCritico() {
        // La criatura está en estado "healthy", sí se puede borrar
        when(creatureRepository.findById(1L)).thenReturn(java.util.Optional.of(creature));

        // Ejecutamos y verificamos que NO lanza error
        assertDoesNotThrow(() -> creatureService.deleteCreature(1L));

        // Verificamos que sí llamó al delete
        verify(creatureRepository, times(1)).delete(creature);
    }

    @Test
    void eliminarCriatura_debeLanzarExcepcion_CuandoEstadoEsCritico() {
        // Cambiamos el estado a "critical"
        creature.setHealthStatus("critical");
        when(creatureRepository.findById(1L)).thenReturn(java.util.Optional.of(creature));

        // Verificamos que SÍ lanza el error
        IllegalStateException excepcion = assertThrows(IllegalStateException.class,
                () -> creatureService.deleteCreature(1L));

        assertEquals("Cannot delete a creature in critical health", excepcion.getMessage());

        // Verificamos que NUNCA llamó al delete
        verify(creatureRepository, never()).delete(any());
    }

    @Test
    void crearCriatura_debeGuardar_CuandoNombreEstaVacio() {
        // Intentamos crear una criatura con nombre vacío
        creature.setName("");
        when(creatureRepository.save(any(Creature.class))).thenReturn(creature);

        Creature resultado = creatureService.createCreature(creature);

        // El servicio la guarda igual — aquí documentamos que NO hay validación aún
        assertNotNull(resultado);
        assertEquals("", resultado.getName());
    }

    @Test
    void crearCriatura_debeGuardar_CuandoNivelPeligroEsNegativo() {
        // Intentamos crear una criatura con nivel de peligro negativo
        creature.setDangerLevel(-1);
        when(creatureRepository.save(any(Creature.class))).thenReturn(creature);

        Creature resultado = creatureService.createCreature(creature);

        // El servicio la guarda igual — documentamos que NO hay validación aún
        assertNotNull(resultado);
        assertEquals(-1, resultado.getDangerLevel());
    }
}