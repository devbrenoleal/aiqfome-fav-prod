package com.aiqfome.demo.persistence;

import com.aiqfome.demo.domain.ProdutoFavorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProdutoFavoritoRepository extends JpaRepository<ProdutoFavorito, String> {
}
