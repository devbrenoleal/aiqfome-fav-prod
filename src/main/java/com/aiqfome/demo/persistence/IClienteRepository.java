package com.aiqfome.demo.persistence;

import com.aiqfome.demo.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IClienteRepository extends JpaRepository<Cliente, String> {
    Optional<Cliente> findByEmail(String email);
}
