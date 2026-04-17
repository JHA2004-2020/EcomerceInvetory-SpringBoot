package com.EcomerceInventory.EcomerceInvetory.repository;

import com.EcomerceInventory.EcomerceInvetory.model.DetallesFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallesFacturaRepository extends JpaRepository<DetallesFactura, Long> {
}