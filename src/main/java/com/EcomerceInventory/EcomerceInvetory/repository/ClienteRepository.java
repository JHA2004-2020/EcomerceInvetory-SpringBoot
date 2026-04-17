package com.EcomerceInventory.EcomerceInvetory.repository;

import com.EcomerceInventory.EcomerceInvetory.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByNombreStartingWith(String nombre);
    List<Cliente> findClienteByActivoAndNombreStartingWith(Boolean activo, String nombre);
}