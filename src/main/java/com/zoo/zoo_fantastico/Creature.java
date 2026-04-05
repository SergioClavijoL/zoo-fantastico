package com.zoo.zoo_fantastico;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*; //Se cambio de javax a jakartan que es una version mas reciente
                              //y compatible
@Entity
@Data
@NoArgsConstructor
public class Creature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String species;
    private double size;
    private int dangerLevel;
    private String healthStatus;
}