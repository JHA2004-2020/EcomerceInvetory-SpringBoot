package com.EcomerceInventory.EcomerceInvetory.controller;

import com.EcomerceInventory.EcomerceInvetory.dto.DetallesFacturaDTO;
import com.EcomerceInventory.EcomerceInvetory.dto.FacturaDTO;
import com.EcomerceInventory.EcomerceInvetory.service.FacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/factura")
@Tag(name = "Factura", description = "Operaciones relacionadsa la compra de productos")
public class FacturaController {
    @Autowired
    FacturaService facturaService;

    //Consultas GET

    //Consultar Facturas por el id
    @Operation(summary = "Consulta Factura Id", description = "Consulta una factura por id")
    @GetMapping("/facturaId/{id}")
    public ResponseEntity<FacturaDTO> consultarFacturaId(@PathVariable Long id){
        return ResponseEntity.ok(facturaService.consultarFacturaId(id));
    }

    //Consultar varias facturas de un cliente por id
    @Operation(summary = "Consulta Factura Cliente Id", description = "Consulta las facturas de un cliente mediante su id de cliente")
    @GetMapping("/facturasClienteId/{clienteId}")
    public ResponseEntity<List<FacturaDTO>> consultarFacturaClienteId(@PathVariable Long clienteId){
        return ResponseEntity.ok(facturaService.consultarFacturaClienteId(clienteId));
    }

    //Consultas POST

    //Crear una factura
    @Operation(summary = "Crear Factura", description = "Consulta para la creacion de una factura con los detalles de sus productos")
    @PostMapping("/crearFactura/{clienteId}")
    public ResponseEntity<FacturaDTO> crearFacturaCompleta(@Valid @RequestBody List<DetallesFacturaDTO> detallesFacturaDTO,
                                                           @PathVariable Long clienteId) throws Exception {
        FacturaDTO guardado = facturaService.crearFacturaCompleta(detallesFacturaDTO, clienteId);
        return new ResponseEntity<>(guardado, HttpStatus.CREATED);
    }
}
