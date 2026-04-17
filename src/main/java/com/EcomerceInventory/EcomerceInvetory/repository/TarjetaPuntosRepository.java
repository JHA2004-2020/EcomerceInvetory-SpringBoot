package com.EcomerceInventory.EcomerceInvetory.repository;

import com.EcomerceInventory.EcomerceInvetory.model.TarjetaPuntos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarjetaPuntosRepository extends JpaRepository<TarjetaPuntos, Long> {
}