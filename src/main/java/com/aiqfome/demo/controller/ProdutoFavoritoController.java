package com.aiqfome.demo.controller;

import com.aiqfome.demo.domain.Cliente;
import com.aiqfome.demo.domain.ProdutoFavorito;
import com.aiqfome.demo.dto.ErrorDTO;
import com.aiqfome.demo.persistence.IProdutoFavoritoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/produtos-favoritos")
@RequiredArgsConstructor
public class ProdutoFavoritoController {

    private final IProdutoFavoritoRepository produtoFavoritoRepository;

    @GetMapping
    public ResponseEntity<List<ProdutoFavorito>> getClientes() {
        return ResponseEntity.ok().body(produtoFavoritoRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> criarProdutoFavorito(@RequestBody @Valid ProdutoFavorito produtoFavorito) {
        if(produtoFavorito.getId() != null)
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorDTO("Não é permitido criar um produto favorito fornecendo parâmetro ID"));
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(produtoFavoritoRepository.save(produtoFavorito));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorDTO(e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<?> atualizarProdutoFavorito(@RequestBody @Valid ProdutoFavorito produtoFavorito) {
        if(produtoFavorito.getId() == null)
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorDTO("ID do produto favorito não fornecido para atualização"));

        try {
            return ResponseEntity.ok().body(produtoFavoritoRepository.save(produtoFavorito));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorDTO(e.getMessage()));
        }
    }

    @DeleteMapping
    public ResponseEntity<?> excluirProdutoFavorito(@RequestBody ProdutoFavorito produtoFavorito) {
        if(produtoFavorito.getId() == null)
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorDTO("ID do produto favorito não fornecido para exclusão"));

        try {
            produtoFavoritoRepository.delete(produtoFavorito);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorDTO(e.getMessage()));
        }
    }
}
