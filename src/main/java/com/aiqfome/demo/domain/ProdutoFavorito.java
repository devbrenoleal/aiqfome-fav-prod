package com.aiqfome.demo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PRODUTO_FAVORITO")
public class ProdutoFavorito implements Serializable {

    @Id
    @Column(nullable = false, updatable = false, length = 36)
    @NotNull(message = "ID do produto é obrigatório")
    private Long id;

    @Column
    private String review;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "PRODUTO_FAVORITO_CLIENTE_ID"))
    @NotNull(message = "ID do cliente é obrigatório")
    private Cliente cliente;
}
