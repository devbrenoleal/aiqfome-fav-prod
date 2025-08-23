package com.aiqfome.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class ProdutoFavoritoDTO implements Serializable {

    @Schema(description = "ID do produto retornado na API https://fakestoreapi.com/products [READONLY]")
    @NotNull(message = "ID do produto é obrigatório")
    private Long id;

    @Schema(description = "Título definido na API https://fakestoreapi.com/products [READONLY]")
    private String title;

    @Schema(description = "Imagem retornada pela API https://fakestoreapi.com/products [READONLY]")
    private String image;

    @Schema(description = "Preço do produto informado pela API https://fakestoreapi.com/products [READONLY]")
    private Double price;

    @Schema(description = "Avaliação do produto informada pelo usuário ao marcar o produto como favorito")
    private String review;

    @NotBlank(message = "ID do cliente é obrigatório")
    @Schema(description = "ID do cliente que marcou o produto como favorito")
    private String clienteId;
}
