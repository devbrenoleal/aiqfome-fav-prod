package com.aiqfome.demo.controller;

import com.aiqfome.demo.domain.ProdutoFavorito;
import com.aiqfome.demo.dto.ErrorDTO;
import com.aiqfome.demo.dto.ProdutoFavoritoDTO;
import com.aiqfome.demo.exception.BusinessException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/produtos-favoritos")
@RequiredArgsConstructor
public class ProdutoFavoritoController {

    private final IProdutoFavoritoRepository produtoFavoritoRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public ResponseEntity<?> getProdutosFavoritos(@RequestParam String clienteId) {
        try {
            List<ProdutoFavorito> produtosFavoritos = produtoFavoritoRepository.findByClienteId(clienteId);
            List<ProdutoFavoritoDTO> dtos = produtosFavoritos
                    .stream()
                    .map(produtoFavorito -> {
                        Optional<ProdutoFavoritoDTO> dtoOptional = Optional.ofNullable(restTemplate.getForObject(
                                "https://fakestoreapi.com/products/" + produtoFavorito.getProdutoId(),
                                ProdutoFavoritoDTO.class
                        ));

                        if(dtoOptional.isEmpty()) {
                            throw new BusinessException("Erro ao buscar produto: " + produtoFavorito.getProdutoId());
                        }
                        ProdutoFavoritoDTO produtoFavoritoDTO = dtoOptional.get();

                        produtoFavoritoDTO.setId(produtoFavorito.getId());
                        produtoFavoritoDTO.setReview(produtoFavorito.getReview());

                        return produtoFavoritoDTO;
                    }).toList();

            return ResponseEntity.ok().body(dtos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorDTO(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> criarProdutoFavorito(@RequestBody @Valid ProdutoFavorito produtoFavorito) {
        if(produtoFavorito.getId() != null)
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorDTO("Não é permitido criar um produto favorito fornecendo parâmetro ID"));
        try {
            Optional<ProdutoFavorito> produtoJaMarcado = produtoFavoritoRepository.findByProdutoId(produtoFavorito.getProdutoId());

            if(produtoJaMarcado.isPresent()) {
                throw new BusinessException("Produto: " + produtoFavorito.getProdutoId() + " já foi marcado como favorito");
            }

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
