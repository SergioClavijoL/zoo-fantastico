package com.zoo.zoo_fantastico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
//tarea completa
@Service
public class CreatureService {
    private final CreatureRepository creatureRepository;
    @Autowired
    public CreatureService(CreatureRepository creatureRepository) {
        this.creatureRepository = creatureRepository;
    }
    public Creature createCreature(Creature creature) {
        return creatureRepository.save(creature);
    }
    public List<Creature> getAllCreatures() {
        return creatureRepository.findAll();
    }
    public Creature getCreatureById(Long id) {
        return creatureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Creature not found")); //Dado que ResourceNotFoundException no
        // es una funcion aparentemente aplicada al proyecto actual, se utiliza la funcion RuntimeException que es una
        // funcion que ya viene incluida en java y que de igual manera cumple la funcion de lanzar el error.
    }
    public Creature updateCreature(Long id, Creature updatedCreature) {
        Creature creature = getCreatureById(id);
        creature.setName(updatedCreature.getName());
        creature.setSpecies(updatedCreature.getSpecies());
        creature.setSize(updatedCreature.getSize());
        creature.setDangerLevel(updatedCreature.getDangerLevel());
        creature.setHealthStatus(updatedCreature.getHealthStatus());
        return creatureRepository.save(creature);
    }
    public void deleteCreature(Long id) {
        Creature creature = getCreatureById(id);
        if (!"critical".equals(creature.getHealthStatus())) {
            creatureRepository.delete(creature);
        } else {
            throw new IllegalStateException("Cannot delete a creature in critical health");
        }
    }
}
