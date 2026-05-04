package com.zoo.zoo_fantastico;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(CreatureService.class)
public class CreatureServiceIntegrationTest {

    @Autowired
    private CreatureService creatureService;

    @Autowired
    private CreatureRepository creatureRepository;

    @BeforeEach
    void limpiarBaseDeDatos() {
        creatureRepository.deleteAll();
    }


    @Test
    void testCreateCreature_ShouldPersistInDatabase() {
        Creature creature = new Creature();
        creature.setName("Unicornio");
        creature.setDangerLevel(5);
        creatureService.createCreature(creature);
        Optional<Creature> foundCreature = creatureRepository.findById(creature.getId());
        assertTrue(foundCreature.isPresent());
        assertEquals("Unicornio", foundCreature.get().getName());
    }

    @Test
    void actualizarCriatura_debeCambiarDatosEnBaseDeDatos() {
        // Creamos y guardamos una criatura
        Creature creature = new Creature();
        creature.setName("Fénix");
        creature.setHealthStatus("healthy");
        creatureService.createCreature(creature);

        // Actualizamos con datos nuevos
        Creature datosNuevos = new Creature();
        datosNuevos.setName("Fénix Dorado");
        datosNuevos.setHealthStatus("healthy");
        creatureService.updateCreature(creature.getId(), datosNuevos);

        // Verificamos que los cambios quedaron en la BD
        Optional<Creature> found = creatureRepository.findById(creature.getId());
        assertTrue(found.isPresent());
        assertEquals("Fénix Dorado", found.get().getName());
    }

    @Test
    void eliminarCriatura_debeQuitarlaDeBaseDeDatos() {
        // Creamos y guardamos una criatura
        Creature creature = new Creature();
        creature.setName("Basilisco");
        creature.setHealthStatus("healthy");
        creatureService.createCreature(creature);
        Long id = creature.getId();

        // Eliminamos
        creatureService.deleteCreature(id);

        // Verificamos que ya no existe en la BD
        Optional<Creature> found = creatureRepository.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    void eliminarCriatura_debeFallar_CuandoEstaCritica() {
        // Creamos una criatura en estado crítico
        Creature creature = new Creature();
        creature.setName("Quimera");
        creature.setHealthStatus("critical");
        creatureService.createCreature(creature);
        Long id = creature.getId();

        // Verificamos que lanza error
        assertThrows(IllegalStateException.class,
                () -> creatureService.deleteCreature(id));

        // Verificamos que sigue en la BD
        assertTrue(creatureRepository.findById(id).isPresent());
    }
}