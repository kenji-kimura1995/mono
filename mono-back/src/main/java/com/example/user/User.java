package com.example.user;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "users_SEQ", allocationSize = 1)
    public Long id;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public String address;
}
