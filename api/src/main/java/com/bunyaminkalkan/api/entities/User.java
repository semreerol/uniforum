package com.bunyaminkalkan.api.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_table")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String userName;
    String password;

    @Column(nullable = false)
    String email;
    String firstName;
    String lastName;
    String profilePhoto;

}
