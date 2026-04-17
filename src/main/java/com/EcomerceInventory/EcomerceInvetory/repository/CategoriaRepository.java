package com.EcomerceInventory.EcomerceInvetory.repository;

import com.EcomerceInventory.EcomerceInvetory.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Categoria findCategoriaByNombre(String nombre);
}