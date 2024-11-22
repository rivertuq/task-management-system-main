package ru.ostritsov.projectTSM.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;


@Entity
@Data
@AllArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;
}
