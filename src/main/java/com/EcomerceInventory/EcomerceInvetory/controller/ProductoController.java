package com.EcomerceInventory.EcomerceInvetory.controller;

import com.EcomerceInventory.EcomerceInvetory.dto.ProductoDTO;
import com.EcomerceInventory.EcomerceInvetory.service.CategoriaService;
import com.EcomerceInventory.EcomerceInvetory.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/productos")
@Tag(name = "Productos", description = "Operaciones relacionadas a los productos")
public class ProductoController {
    @Autowired
    ProductoService productoService;
    @Autowired
    CategoriaService categoriaService;

    //Consultas GET

    //Listar productos paginados
    @Operation(summary = "Paginacion", description = "Pagina los productos disponibles como un valor de vista de 10 productos")
    @GetMapping("/productos")
    public ResponseEntity<Page<ProductoDTO>> listarPaginaProducto(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(productoService.listarPaginaProducto(page,size));
        }

    //Consultar producto por id
    @Operation(summary = "Consulta Producto Id", description = "Consulta los productos por su id")
    @GetMapping("/productoId/{id}")
    public ResponseEntity<ProductoDTO> consultarProductoId(@PathVariable Long id){
        return ResponseEntity.ok(productoService.listarProductoId(id));
    }

    //Consultar producto por nombre
    @Operation(summary = "Paginacion", description = "Pagina los productos disponibles como un valor de vista de 10 productos")
    @GetMapping("/productoNombre/{nombre}")
    public ResponseEntity<List<ProductoDTO>> consultarProductoNombre(@PathVariable String nombre){
        return ResponseEntity.ok(productoService.listarProductoNombre(nombre));
    }

    //Consultar productos por nombre y activos
    @Operation(summary = "Consulta Nombre Activo", description = "Consulta los productos segun si estan operativos o no")
    @GetMapping("/productoNombreEstado/{nombre}/{estado}")
    public ResponseEntity<List<ProductoDTO>> consultarProductoNombreActivo(@PathVariable String nombre,
                                                                           @PathVariable Boolean estado){
        return ResponseEntity.ok(productoService.listarProductoNombreActivo(nombre, estado));
    }

    //Listar producto por categoria
    @Operation(summary = "Consulta Producto Categoria", description = "Consulta los productos por categoria")
    @GetMapping("/producto/categoria/{nombreCategoria}")
    public ResponseEntity<List<ProductoDTO>> consultarProductoCategoria(@PathVariable String nombreCategoria){
        return ResponseEntity.ok(productoService.listarProductosCategoria(nombreCategoria));
    }

    //Consultas POST
    @PostMapping("/crearProducto")
    @Operation(summary = "Crear Producto", description = "Consulta para la creacion de un producto")
    public ResponseEntity<ProductoDTO> crearProducto(@Valid @RequestBody ProductoDTO productoDTO){
        ProductoDTO guardado = productoService.crearProducto(productoDTO);
        return new ResponseEntity<>(guardado, HttpStatus.CREATED);
    }

    //Consultas PUT
    @Operation(summary = "Actualizacion Producto", description = "Consulta para actualizar un producto por su id")
    @PutMapping("/actualizarProducto/{id}")
    public ResponseEntity<ProductoDTO> actualizarProducto(@PathVariable Long id,@RequestBody ProductoDTO productoDTO){
        return ResponseEntity.ok(productoService.actualizarProductoId(id, productoDTO));
    }

}
