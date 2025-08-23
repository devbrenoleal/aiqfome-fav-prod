package com.aiqfome.demo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProdutoFavoritoDTO implements Serializable {
    private String id;
    private String title;
    private String image;
    private Double price;
    private String review;
}
