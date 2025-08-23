package com.aiqfome.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class ProdutoFavoritoDTO implements Serializable {
    @NotNull(message = "ID do produto é obrigatório")
    private Long id;

    private String title;

    private String image;

    private Double price;

    private String review;

    @NotBlank(message = "ID do cliente é obrigatório")
    private String clienteId;
}
