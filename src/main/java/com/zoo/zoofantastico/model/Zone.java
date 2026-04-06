package com.zoo.zoofantastico.model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
@Entity
@Data
@NoArgsConstructor
public class Zone {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private int capacity;

    @OneToMany(mappedBy = "zone")
    private List<Creature> creatures;
}
