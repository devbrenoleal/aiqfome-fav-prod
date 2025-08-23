package com.aiqfome.demo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "CLIENTE",
        uniqueConstraints = {@UniqueConstraint(columnNames = "email", name = "CLIENTE_EMAIL_UNIQUE")}
)
public class Cliente implements Serializable {

    @Id
    @Column(nullable = false, updatable = false, length = 36)
    private String id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Column(nullable = false)
    private String email;

    @PrePersist
    public void prePersist() {
        id = UUID.randomUUID().toString().toLowerCase();
    }
}
