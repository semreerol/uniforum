package com.bunyaminkalkan.api.entities;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "university_table")
@Data
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
