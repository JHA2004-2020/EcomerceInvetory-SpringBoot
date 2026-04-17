package com.EcomerceInventory.EcomerceInvetory.service;

import com.EcomerceInventory.EcomerceInvetory.exception.ResourceNotFoundException;
import com.EcomerceInventory.EcomerceInvetory.model.Categoria;
import com.EcomerceInventory.EcomerceInvetory.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {
    @Autowired
    CategoriaRepository categoriaRepository;

    //Sentencias GET

    //Consulta categoria id
    public Categoria consultarCategoriaId(Long id){
        return categoriaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Esta categoria no existe"));
    }

    //Consulta categoria por nombre
    public List<Categoria> consultarCategoriaNombre(String nombre){
        if(categoriaRepository.findAll().isEmpty()){
            throw new ResourceNotFoundException("No hay categorias disponibles");
        }
        return categoriaRepository.findAll();
    }
}
