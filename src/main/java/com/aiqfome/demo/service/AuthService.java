package com.aiqfome.demo.service;

import com.aiqfome.demo.domain.Cliente;
import com.aiqfome.demo.dto.LoginDTO;
import com.aiqfome.demo.exception.BusinessException;
import com.aiqfome.demo.persistence.IClienteRepository;
import com.aiqfome.demo.security.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final IClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    public String login(LoginDTO loginDTO) {
        Optional<Cliente> optionalCliente = clienteRepository.findByEmail(loginDTO.email());

        if(optionalCliente.isEmpty()) {
            throw new InvalidParameterException("Usuário não encontrado para o e-mail: " + loginDTO.email());
        }

        Cliente cliente = optionalCliente.get();

        boolean authenticated = passwordEncoder.matches(loginDTO.senha(), cliente.getSenha());

        if(!authenticated) {
            throw new BusinessException("Senha inválida");
        }

        return JWTUtils.gerarToken(loginDTO.email());
    }

    public String registrar(Cliente cliente) {
        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));

        clienteRepository.save(cliente);

        return JWTUtils.gerarToken(cliente.getEmail());
    }
}
