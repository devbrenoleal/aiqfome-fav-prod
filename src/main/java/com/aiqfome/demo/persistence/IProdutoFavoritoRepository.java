package com.aiqfome.demo.persistence;

import com.aiqfome.demo.domain.ProdutoFavorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProdutoFavoritoRepository extends JpaRepository<ProdutoFavorito, String> {
    List<ProdutoFavorito> findByClienteId(String clienteId);
    Optional<ProdutoFavorito> findByProdutoId(Long produtoId);
}
