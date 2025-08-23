package com.aiqfome.demo.controller;

import com.aiqfome.demo.domain.Cliente;
import com.aiqfome.demo.domain.ProdutoFavorito;
import com.aiqfome.demo.dto.ClienteDTO;
import com.aiqfome.demo.dto.ErrorDTO;
import com.aiqfome.demo.dto.ProdutoFavoritoDTO;
import com.aiqfome.demo.exception.BusinessException;
import com.aiqfome.demo.persistence.IProdutoFavoritoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @Operation(
            summary = "Lista os produtos favoritos de um cliente",
            parameters = {
                    @Parameter(name = "clienteId", schema = @Schema(description = "ID do cliente que marcou produtos como favorito"))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Retornou a lista com sucesso",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProdutoFavoritoDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos, confira a requisição",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro no processamento dos dados",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
                    )
            }
    )
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

    @Operation(
            summary = "Cria um registro para marcar um produto como favorito",
            description = "Marca o produto como favorito",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Registro criado com sucesso",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProdutoFavoritoDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos, confira a requisição",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro no processamento dos dados",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
                    )
            }
    )
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

    @Operation(
            summary = "Atualiza um produto favorito",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Atualizou o produto com sucesso",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProdutoFavoritoDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos, confira a requisição",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro no processamento dos dados",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
                    )
            }
    )
    @PutMapping
    public ResponseEntity<?> atualizarProdutoFavorito(@RequestBody @Valid ProdutoFavoritoDTO dto) {
        try {
            ProdutoFavorito produtoFavorito = ProdutoFavorito.builder()
                    .id(dto.getId())
                    .review(dto.getReview())
                    .cliente(Cliente.builder().id(dto.getClienteId()).build())
                    .build();

            produtoFavoritoRepository.save(produtoFavorito);

            return ResponseEntity.ok().body(dto);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorDTO(e.getMessage()));
        }
    }

    @Operation(
            summary = "Exclui o registro de marcação do produto como favorito",
            description = "Desmarca o produto como favorito",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Registro excluído com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos, confira a requisição",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro no processamento dos dados",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
                    )
            }
    )
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
