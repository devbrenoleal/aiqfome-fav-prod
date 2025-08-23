package com.aiqfome.demo.controller;

import com.aiqfome.demo.domain.Cliente;
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
                                "https://fakestoreapi.com/products/" + produtoFavorito.getId(),
                                ProdutoFavoritoDTO.class
                        ));

                        if(dtoOptional.isEmpty()) {
                            throw new BusinessException("Erro ao buscar produto: " + produtoFavorito.getId());
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
    public ResponseEntity<?> marcarProdutoComoFavorito(@RequestBody @Valid ProdutoFavoritoDTO dto) {
        try {
            Optional<ProdutoFavorito> produtoJaMarcado = produtoFavoritoRepository.findById(dto.getId());

            if(produtoJaMarcado.isPresent()) {
                throw new BusinessException("Produto: " + dto.getId() + " já foi marcado como favorito");
            }

            ProdutoFavorito produtoFavorito = ProdutoFavorito.builder()
                    .id(dto.getId())
                    .review(dto.getReview())
                    .cliente(Cliente.builder().id(dto.getClienteId()).build())
                    .build();

            produtoFavoritoRepository.save(produtoFavorito);

            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorDTO(e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<?> atualizarProdutoFavorito(@RequestBody @Valid ProdutoFavoritoDTO dto) {
        try {
            ProdutoFavorito produtoFavorito = ProdutoFavorito.builder()
                    .id(dto.getId())
                    .review(dto.getReview())
                    .cliente(Cliente.builder().id(dto.getClienteId()).build())
                    .build();
            produtoFavoritoRepository.save(produtoFavorito);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorDTO(e.getMessage()));
        }
    }

    @DeleteMapping
    public ResponseEntity<?> excluirProdutoFavorito(@RequestBody @Valid ProdutoFavoritoDTO dto) {
        try {
            produtoFavoritoRepository.deleteByIdAndClienteId(dto.getId(), dto.getClienteId());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorDTO(e.getMessage()));
        }
    }
}
