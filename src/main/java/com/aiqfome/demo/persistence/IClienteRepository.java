package com.aiqfome.demo.persistence;

import com.aiqfome.demo.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IClienteRepository extends JpaRepository<Cliente, String> {
}
