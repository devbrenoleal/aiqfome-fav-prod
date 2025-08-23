package com.aiqfome.demo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter @Setter
@Table(
        name = "CLIENTE",
        uniqueConstraints = {@UniqueConstraint(columnNames = "email", name = "CLIENTE_EMAIL_UNIQUE")}
)
public class Cliente implements Serializable {

    @Id
    @Column(nullable = false, updatable = false, length = 36)
    private String id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String email;

    @PrePersist
    public void prePersist() {
        id = UUID.randomUUID().toString().toLowerCase();
    }
}
