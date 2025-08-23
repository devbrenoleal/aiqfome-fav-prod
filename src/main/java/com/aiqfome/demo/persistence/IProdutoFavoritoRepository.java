package com.aiqfome.demo.persistence;

import com.aiqfome.demo.domain.ProdutoFavorito;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProdutoFavoritoRepository extends JpaRepository<ProdutoFavorito, Long> {
    List<ProdutoFavorito> findByClienteId(String clienteId);

    @Transactional
    @Modifying
    @Query("DELETE FROM ProdutoFavorito p WHERE p.id = ?1 AND p.cliente.id = ?2")
    void deleteByIdAndClienteId(Long id, String clienteId);
}
