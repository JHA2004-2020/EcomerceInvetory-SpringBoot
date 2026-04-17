package com.EcomerceInventory.EcomerceInvetory.controller;

import com.EcomerceInventory.EcomerceInvetory.model.Categoria;
import com.EcomerceInventory.EcomerceInvetory.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@Tag(name = "Categoria", description = "Operaciones relacionadsa con las categorias")
public class CategoriaController {
    @Autowired
    CategoriaService categoriaService;

    //Sentencias GET
    @Operation(summary = "Consulta Categoria id", description = "Consulta una categoria por id")
    @GetMapping("/categoria/{id}")
    public ResponseEntity<Categoria> consultarCategoriaID(@PathVariable Long id){
        return ResponseEntity.ok(categoriaService.consultarCategoriaId(id));
    }

    @Operation(summary = "Consulta Categoria Nombre", description = "Consulta categorias por nombre")
    @GetMapping("/categoria/{nombre}")
    public ResponseEntity<List<Categoria>> consultarCategoriaNombre(@PathVariable String nombre){
        return ResponseEntity.ok(categoriaService.consultarCategoriaNombre(nombre));
    }
}
