package com.EcomerceInventory.EcomerceInvetory.service;

import com.EcomerceInventory.EcomerceInvetory.dto.ProductoDTO;
import com.EcomerceInventory.EcomerceInvetory.exception.ResourceNotFoundException;
import com.EcomerceInventory.EcomerceInvetory.model.Categoria;
import com.EcomerceInventory.EcomerceInvetory.model.Producto;
import com.EcomerceInventory.EcomerceInvetory.repository.CategoriaRepository;
import com.EcomerceInventory.EcomerceInvetory.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductoService {
    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    CategoriaRepository categoriaRepository;
    //Convertir a dto
    public ProductoDTO convertirADTO(Producto producto){
        ProductoDTO dto = new ProductoDTO();

        dto.setNombre(producto.getNombre());
        dto.setDescripcin(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setCategoriaNombre(producto.getCategoria().getNombre());
        dto.setActivo(producto.getActivo());
        dto.setCategoriaId(producto.getCategoria().getId());

        return dto;
    }
    //Sentencias GET

    //paginacion
    public Page<ProductoDTO> listarPaginaProducto(int numeroPagina, int size){
        Pageable paginacion = PageRequest.of(numeroPagina, size, Sort.by("categoria_id").ascending());
        Page<Producto> paginaProductos = productoRepository.findAll(paginacion);
        if (paginaProductos.isEmpty()){
            throw new ResourceNotFoundException("No hay productos de momento");
        }
        return paginaProductos.map(producto -> convertirADTO(producto));
    }

    //Listar producto por id
    public ProductoDTO listarProductoId(Long id){
        Producto producto = productoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El producto con ese id no existe"));
        return convertirADTO(producto);
    }

    //Listar producto por nombre
    public List<ProductoDTO> listarProductoNombre(String nombre){
        List<Producto> productos = productoRepository.findProductoByNombreStartingWith(nombre);
        if (productos.isEmpty()){
            throw new ResourceNotFoundException("No hay productos con el nombre " + nombre);
        }
        return productos.stream().map(this::convertirADTO).toList();
    }

    //Listar producto por nombre y activo
    public List<ProductoDTO> listarProductoNombreActivo(String nombre, Boolean estado){
        List<Producto> productos = productoRepository.findProductoByActivoAndNombreStartingWith(estado, nombre);
        if (productos.isEmpty()){
            throw new ResourceNotFoundException("No hay productos con el nombre " + nombre);
        }
        return productos.stream().map(this::convertirADTO).toList();
    }

    //Listar producto por categoria
    public List<ProductoDTO> listarProductosCategoria(String nombreCategoria){
        Categoria cat = categoriaRepository.findCategoriaByNombre(nombreCategoria);
        if (cat == null){
            throw new ResourceNotFoundException("Esta categoria no existe");
        }
        List<Producto> productos = productoRepository.findProductoByCategoria(cat);
        return productos.stream().map(this::convertirADTO).toList();
    }

    //Sentencias POST

    //Crear nuebo producto
    public ProductoDTO crearProducto(ProductoDTO dto){
        Categoria cat = categoriaRepository.findById(dto.getCategoriaId()).orElseThrow(()-> new ResourceNotFoundException("Esta categoria no existe"));
        Producto producto = new Producto();

        producto.setActivo(true);
        producto.setCategoria(cat);
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcin());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());

        productoRepository.save(producto);

        return convertirADTO(producto);
    }

    //Sentecias PUT
    public ProductoDTO actualizarProductoId(Long id, ProductoDTO dto){
        Producto producto = productoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El producto con ese id no existe"));


        if(dto.getNombre() != null && !dto.getNombre().isBlank()){
            producto.setNombre(dto.getNombre());
        }
        if(dto.getDescripcin() != null && !dto.getDescripcin().isBlank()){
            producto.setDescripcion(dto.getDescripcin());
        }
        if(dto.getCategoriaId() != null){
            Categoria cat = categoriaRepository.findById(dto.getCategoriaId()).orElseThrow(() -> new ResourceNotFoundException("Esta categoria no existe"));
            producto.setCategoria(cat);
        }
        if(dto.getPrecio() != null && dto.getPrecio() > 0){
            producto.setPrecio(dto.getPrecio());
        }
        if(dto.getStock() != null && dto.getStock() > 0){
            producto.setStock(dto.getStock());
        }
        if(dto.getActivo() != null){
            producto.setActivo(dto.getActivo());
        }

        productoRepository.save(producto);

        return convertirADTO(producto);
    }
}
