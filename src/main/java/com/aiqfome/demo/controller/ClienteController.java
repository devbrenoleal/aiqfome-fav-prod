package com.aiqfome.demo.controller;

import com.aiqfome.demo.domain.Cliente;
import com.aiqfome.demo.dto.ClienteDTO;
import com.aiqfome.demo.dto.ErrorDTO;
import com.aiqfome.demo.persistence.IClienteRepository;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final IClienteRepository clienteRepository;

    @Operation(
            summary = "Lista os clientes",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Retornou a lista com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> getClientes() {
        return ResponseEntity.ok()
                .body(
                        clienteRepository.findAll()
                                .stream()
                                .map(c -> new ClienteDTO(c.getId(), c.getNome(), c.getEmail(), null))
                                .toList()
                );
    }


    /* @Replaced pela API de login
    @Operation(
            summary = "Cria novos clientes com nome e e-mail",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Criou o cliente com sucesso",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ClienteDTO.class))
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
    public ResponseEntity<?> criarCliente(@RequestBody @Valid ClienteDTO dto) {
        if(dto.getId() != null)
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorDTO("Não é permitido criar clientes fornecendo parâmetro ID"));
        try {
            Cliente cliente = Cliente.builder()
                    .nome(dto.getNome())
                    .email(dto.getEmail())
                    .build();

            clienteRepository.save(cliente);

            dto.setId(cliente.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorDTO(e.getMessage()));
        }
    }*/

    @Operation(
            summary = "Atualiza o cliente",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Atualizou com sucesso o cliente",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ClienteDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cliente não encontrado",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class))
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
    public ResponseEntity<?> atualizarCliente(@RequestBody @Valid ClienteDTO dto) {
        if(dto.getId() == null)
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorDTO("ID do cliente não fornecido para atualização"));

        try {
            Optional<Cliente> optionalCliente = clienteRepository.findById(dto.getId());

            if(optionalCliente.isEmpty()) {
                throw new InvalidParameterException("Nenhum cliente encontrado para o parâmetro informado");
            }

            Cliente cliente = optionalCliente.get();
            cliente.setEmail(dto.getEmail());
            cliente.setNome(dto.getNome());

            clienteRepository.save(cliente);

            return ResponseEntity.ok().body(dto);
        } catch (InvalidParameterException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO(ex.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorDTO(e.getMessage()));
        }
    }

    @Operation(
            summary = "Exclui o cliente",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Excluiu o cliente com sucesso"
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
    public ResponseEntity<?> excluirCliente(@RequestBody ClienteDTO dto) {
        if(dto.getId() == null)
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorDTO("ID do cliente não fornecido para exclusão"));

        try {

            clienteRepository.deleteById(dto.getId());

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorDTO(e.getMessage()));
        }
    }
}
