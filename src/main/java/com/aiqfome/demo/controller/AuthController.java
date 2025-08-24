package com.aiqfome.demo.controller;

import com.aiqfome.demo.domain.Cliente;
import com.aiqfome.demo.dto.ClienteDTO;
import com.aiqfome.demo.dto.ErrorDTO;
import com.aiqfome.demo.dto.LoginDTO;
import com.aiqfome.demo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidParameterException;

import static com.aiqfome.demo.security.AuthFilter.AUTH_PREFIX;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Autentica o usuário no sistema",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Autenticou com sucesso",
                            headers = {@Header(name = HttpHeaders.AUTHORIZATION, description = "Armazena o token gerado para o usuário")}
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
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        try {
            if (dto.email() == null || dto.senha() == null) {
                return ResponseEntity.badRequest()
                        .body(new ErrorDTO("Email e senha são obrigatórios"));
            }

            String token = authService.login(dto);

            return ResponseEntity.noContent()
                    .header(HttpHeaders.AUTHORIZATION, AUTH_PREFIX + token).build();
        } catch (InvalidParameterException e) {
            return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorDTO(e.getMessage()));
        }
    }

    @Operation(
            summary = "Registra um novo cliente no sistema com nome, email e senha",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cliente registrado com sucesso",
                            headers = {@Header(name = HttpHeaders.AUTHORIZATION, description = "Armazena o token gerado para o usuário")},
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
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody @Valid ClienteDTO clienteDTO) {
        if(clienteDTO.getId() != null) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorDTO("Não é permitido criar clientes fornecendo parâmetro ID"));
        }

        if(clienteDTO.getSenha() == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorDTO("Senha é obrigatório"));
        }

        try {
            Cliente cliente = Cliente.builder()
                    .nome(clienteDTO.getNome())
                    .email(clienteDTO.getEmail())
                    .senha(clienteDTO.getSenha())
                    .build();

            String token = authService.registrar(cliente);

            clienteDTO.setId(cliente.getId());
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header(HttpHeaders.AUTHORIZATION, AUTH_PREFIX + token)
                    .body(clienteDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorDTO(e.getMessage()));
        }
    }
}
