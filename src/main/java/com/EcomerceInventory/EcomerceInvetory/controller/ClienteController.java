package com.EcomerceInventory.EcomerceInvetory.controller;

import com.EcomerceInventory.EcomerceInvetory.dto.ClienteDTO;
import com.EcomerceInventory.EcomerceInvetory.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "Operaciones relacionadsa con el cliente")
public class ClienteController {
    @Autowired
    ClienteService clienteService;

    //Consultas GET

    //Generara pagina clientes
    @Operation(summary = "/paginacion", description = "Pagina los clientes disponibles como un valor de vista de 10 clientes")
    @GetMapping("/clientes")
    public ResponseEntity<Page<ClienteDTO>> listarPaginaCliente(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(clienteService.listarPaginaCliente(page,size));
    }


    //Consulta cliente id
    @Operation(summary = "Consulta Cliente Id", description = "Consulta para encontrar un cliente mediante id")
    @GetMapping("/clienteId/{id}")
    public ResponseEntity<ClienteDTO> consultarClienteId(@Valid @PathVariable Long id){
        return ResponseEntity.ok(clienteService.listarClienteId(id));
    }

    //Consulta cliente nombre
    @Operation(summary = "Consulta Cliente Nombre", description = "Consulta para encontrar clientes mediante sus nombres")
    @GetMapping("/clienteNombre/{nombre}")
    public ResponseEntity<List<ClienteDTO>> consultaClienteNombre(@Valid @PathVariable String nombre){
        return ResponseEntity.ok(clienteService.listarClienteNombre(nombre));
    }

    //Consulta cliente nombre activo
    @Operation(summary = "Consulta Clientes Nombre Activo", description = "Consulta para encontrar clientes mediante sus nombres y si estan activos o no")
    @GetMapping("/clienteNombreEstado/{nombre}/{estado}")
    public ResponseEntity<List<ClienteDTO>> consultaClienteNombreActivo(@Valid @PathVariable String nombre, @PathVariable Boolean estado){
        return ResponseEntity.ok(clienteService.listarClienteNombreActivo(nombre, estado));
    }

    //Consultas POST

    //Crear cliente
    @Operation(summary = "Crear cliente", description = "Consulta para crear un cliente")
    @PostMapping("/crear")
    public ResponseEntity<ClienteDTO> crearCliente(@Valid @RequestBody ClienteDTO clienteDTO){
        ClienteDTO guardado = clienteService.crearCliente(clienteDTO);
        return new ResponseEntity<>(guardado,HttpStatus.CREATED);
    }

    //Consultas PUT
    @Operation(summary = "Actualizar cliente", description = "Consulta para actualizar un cliente mediante su id")
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ClienteDTO> actualizarCliente(@PathVariable Long id,@RequestBody ClienteDTO clienteDTO){
        return ResponseEntity.ok(clienteService.actualizarCliente(id, clienteDTO));
    }

}
