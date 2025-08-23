package com.aiqfome.demo.controller;

import com.aiqfome.demo.domain.Cliente;
import com.aiqfome.demo.dto.ClienteDTO;
import com.aiqfome.demo.dto.ErrorDTO;
import com.aiqfome.demo.persistence.IClienteRepository;
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
@RequestMapping("api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final IClienteRepository clienteRepository;

    @GetMapping
    public ResponseEntity<List<Cliente>> getClientes() {
        return ResponseEntity.ok().body(clienteRepository.findAll());
    }

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
    }

    @PutMapping
    public ResponseEntity<?> atualizarCliente(@RequestBody @Valid ClienteDTO dto) {
        if(dto.getId() == null)
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorDTO("ID do cliente não fornecido para atualização"));

        try {
            Cliente cliente = Cliente.builder()
                    .id(dto.getId())
                    .nome(dto.getNome())
                    .email(dto.getEmail())
                    .build();

            clienteRepository.save(cliente);

            return ResponseEntity.ok().body(dto);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorDTO(e.getMessage()));
        }
    }

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
