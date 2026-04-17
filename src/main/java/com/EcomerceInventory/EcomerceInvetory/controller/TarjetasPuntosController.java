package com.EcomerceInventory.EcomerceInvetory.controller;

import com.EcomerceInventory.EcomerceInvetory.dto.TarjetaPuntosDTO;
import com.EcomerceInventory.EcomerceInvetory.model.TarjetaPuntos;
import com.EcomerceInventory.EcomerceInvetory.service.TarjetaPuntosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/tarjetaPuntos")
@Tag(name = "Productos", description = "Operaciones relacionadas a los productos")
public class TarjetasPuntosController {
    @Autowired
    TarjetaPuntosService tarjetaPuntosService;

    //Consultas GET

    //Consultar tarjeta puntos por id
    @Operation(summary = "Consulta Tarjeta Id", description = "Consulta las tarjetas por su id")
    @GetMapping("/tarjetaPunto/{id}")
    public ResponseEntity<TarjetaPuntosDTO> consultarTarjetaPuntosId(@PathVariable Long id){
        return ResponseEntity.ok(tarjetaPuntosService.consultaTarjetaPuntosId(id));
    }

    //Consultas POST

    //Crear tarjeta de puntos
    @Operation(summary = "Creacion Tarjeta", description = "Consulta para la creacion de una tarjeta asignada a su cliente")
    @PostMapping("/crearTarjetaPuntos/{clienteId}")
    public ResponseEntity<TarjetaPuntosDTO> crearTarjetaPuntos(@PathVariable Long clienteId){
        TarjetaPuntosDTO guardado = tarjetaPuntosService.crearTarjetaPuntos(clienteId);
        return new ResponseEntity<>(guardado,HttpStatus.CREATED);
    }

    //Consultas PUT

    //Actualizar estado de la tarjeta
    @Operation(summary = "Actualizacion Tarjeta Activo", description = "Actualiza la tarjeta de estado")
    @PutMapping("/actualizarTarjetaPuntosActivo/{clienteId}")
    public ResponseEntity<TarjetaPuntosDTO> actualizarTarjetaPuntosActivo(@PathVariable Long clienteId){
        return ResponseEntity.ok(tarjetaPuntosService.actualizarTarjetaPuntosActivo(clienteId));
    }
}
