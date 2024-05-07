package com.bunyaminkalkan.api.entities;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "department_table")
@Data
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
