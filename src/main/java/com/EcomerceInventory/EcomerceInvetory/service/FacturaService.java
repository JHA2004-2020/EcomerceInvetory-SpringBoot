package com.EcomerceInventory.EcomerceInvetory.service;

import com.EcomerceInventory.EcomerceInvetory.dto.DetallesFacturaDTO;
import com.EcomerceInventory.EcomerceInvetory.dto.FacturaDTO;
import com.EcomerceInventory.EcomerceInvetory.dto.ProductoDTO;
import com.EcomerceInventory.EcomerceInvetory.exception.ResourceNotFoundException;
import com.EcomerceInventory.EcomerceInvetory.model.Cliente;
import com.EcomerceInventory.EcomerceInvetory.model.DetallesFactura;
import com.EcomerceInventory.EcomerceInvetory.model.Factura;
import com.EcomerceInventory.EcomerceInvetory.model.Producto;
import com.EcomerceInventory.EcomerceInvetory.repository.ClienteRepository;
import com.EcomerceInventory.EcomerceInvetory.repository.DetallesFacturaRepository;
import com.EcomerceInventory.EcomerceInvetory.repository.FacturaRepository;
import com.EcomerceInventory.EcomerceInvetory.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FacturaService {
    @Autowired
    FacturaRepository facturaRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    DetallesFacturaRepository detallesFacturaRepository;
    @Autowired
    ProductoRepository productoRepository;

    //Convertir a DTO
    public FacturaDTO convertirADTO(Factura factura){
        FacturaDTO dto = new FacturaDTO();
        dto.setFecha(factura.getFecha());
        dto.setTotal(factura.getTotal());
        dto.setCliente(factura.getCliente().getId());

        List<DetallesFacturaDTO> detallesDTO = factura.getDetallesFacturas().stream()
                .map(detalle -> {
                    DetallesFacturaDTO dDto = new DetallesFacturaDTO();
                    dDto.setProductoId(detalle.getProducto().getId());
                    dDto.setNombreProducto(detalle.getProducto().getNombre());
                    dDto.setCantidad(detalle.getCantidad());
                    dDto.setPrecioMomento(detalle.getPrecioMomento());
                    return dDto;
                }).toList();
        dto.setDetallesFacturas(detallesDTO);
        return dto;
    }

    //Consultas GET

    //Consultar factura por id
    public FacturaDTO consultarFacturaId(Long id){
        Factura factura = facturaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Esta factura no existe"));
        return convertirADTO(factura);
    }

    //Consultar facturas por id del cliente
    public List<FacturaDTO> consultarFacturaClienteId(Long clienteId){
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(() -> new ResourceNotFoundException("Cliente no existente"));
        List<Factura> facturas = cliente.getFacturas();
        return facturas.stream().map(this::convertirADTO).toList();
    }

    //Consultas POST

    //Crear una lista de productos como detalles de la factura
    public List<DetallesFactura> crearDetallesFactura(List<DetallesFacturaDTO> detallesFacturasdto){
        List<DetallesFactura> detallesFactura = new ArrayList<>();
        Double total = 0.0;
        for (int i = 0; i < detallesFacturasdto.size(); i++) {
            Producto producto = productoRepository.findById(detallesFacturasdto.get(i).getProductoId()).orElseThrow(() -> new ResourceNotFoundException("Este producto no existe"));
            DetallesFactura df = new DetallesFactura();
            df.setCantidad(detallesFacturasdto.get(i).getCantidad());
            df.setPrecioMomento(producto.getPrecio()*detallesFacturasdto.get(i).getCantidad());
            df.setProducto(producto);
            detallesFactura.add(df);
        }
        return detallesFactura;
    }
    //Crear una factura inicial como contenedor
    public Factura crearFactura(Long clienteId){
        Factura factura = new Factura();

        factura.setCliente(clienteRepository.findById(clienteId).orElseThrow(() -> new ResourceNotFoundException("Este cliente no existe")));
        factura.setFecha(LocalDateTime.now());
        factura.setTotal(0.0);

        facturaRepository.save(factura);

        return factura;
    }

    //Crear una factura completa
    public FacturaDTO crearFacturaCompleta(List<DetallesFacturaDTO> detallesFacturasdto, Long clienteId) throws Exception {
        List<DetallesFactura> detallesFacturas = crearDetallesFactura(detallesFacturasdto);
        Factura factura = crearFactura(clienteId);

        factura.setTotal(0.0);
        for (int i = 0; i < detallesFacturas.size(); i++) {
            factura.setTotal(factura.getTotal()+detallesFacturas.get(i).getPrecioMomento());
            Producto producto = detallesFacturas.get(i).getProducto();
            if (producto.getStock() < detallesFacturasdto.get(i).getCantidad()){
                throw new Exception("Stock insuficiente");
            }
            producto.setStock(producto.getStock()-detallesFacturas.get(i).getCantidad());
            detallesFacturas.get(i).setFactura(factura);
            productoRepository.save(producto);
            detallesFacturaRepository.save(detallesFacturas.get(i));
        }
        facturaRepository.save(factura);

        return convertirADTO(factura);
    }

}
