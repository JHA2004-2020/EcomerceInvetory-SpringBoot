package com.EcomerceInventory.EcomerceInvetory.repository;

import com.EcomerceInventory.EcomerceInvetory.model.Categoria;
import com.EcomerceInventory.EcomerceInvetory.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findProductoByNombreStartingWith(String nombre);
    List<Producto> findProductoByActivoAndNombreStartingWith(Boolean activo, String nombre);
    List<Producto> findProductoByCategoria(Categoria categoria);
}