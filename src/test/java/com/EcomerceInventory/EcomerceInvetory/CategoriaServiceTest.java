package com.EcomerceInventory.EcomerceInvetory;

import com.EcomerceInventory.EcomerceInvetory.model.Categoria;
import com.EcomerceInventory.EcomerceInvetory.repository.CategoriaRepository;
import com.EcomerceInventory.EcomerceInvetory.exception.ResourceNotFoundException;
import com.EcomerceInventory.EcomerceInvetory.service.CategoriaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.mockito.Mockito.*; // Para when, times, any, etc.
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;
    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoriaEjemplo;

    @BeforeEach
    void setUp(){
        categoriaEjemplo = new Categoria();
        categoriaEjemplo.setId(1L);
        categoriaEjemplo.setNombre("Ropa");
    }

    //Busqueda por id
    @Test
    @DisplayName("Respuesta exitosa de categoria id")
    void categoriaIdExistenteTest() {
        //Arrancar
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoriaEjemplo));

        //Actuar
        Categoria respuesta = categoriaService.consultarCategoriaId(1L);

        //Acertar
        assertNotNull(respuesta, "Debe existir un cliente");
        assertEquals(1L, categoriaEjemplo.getId(), "El cliente no fue traido");

        verify(categoriaRepository, times(1)).findById(1L);
    }
    @Test
    @DisplayName("Respuesta fallida de categoria id")
    void categoriaIdInexistenteTest() {
        //Arrancar
        Long idInvalido = 100L;

        when(categoriaRepository.findById(idInvalido)).thenReturn(Optional.empty());

        //Actuar/Acertar
        assertThrows(ResourceNotFoundException.class, () -> categoriaService.consultarCategoriaId(idInvalido));

        verify(categoriaRepository, times(1)).findById(idInvalido);
    }

    //Consultar id por nombre
    @Test
    @DisplayName("Busqueda exitosa clientes por nombre")
    void categoriaExitoNombreTest() {
        //Arrancar
        String nombre = "Ropa";

        when(categoriaRepository.findCategoriaByNombre(nombre)).thenReturn(categoriaEjemplo);

        //Actuar
        Categoria respuesta = categoriaService.consultarCategoriaNombre(nombre);

        //Acertar
        assertEquals(nombre, respuesta.getNombre());

        verify(categoriaRepository, times(1)).findCategoriaByNombre(nombre);
    }
}
