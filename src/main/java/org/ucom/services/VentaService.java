package org.ucom.services;

import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.ucom.config.IDAO;
import org.ucom.customexceptions.InsufficientStockException;
import org.ucom.entities.Cliente;
import org.ucom.entities.MetodoPago;
import org.ucom.entities.Venta;
import org.ucom.entities.Producto;
import org.ucom.entities.VentaDetalle;
import org.ucom.entities.dto.ResumenVentaDTO;
import org.ucom.entities.dto.VentaDetalleDTO;
import org.ucom.repositories.VentaDetalleRepository;
import org.ucom.repositories.VentaRepository;

import org.ucom.entities.params.RegistrarVentaParam;
import org.ucom.repositories.ClienteRepository;
import org.ucom.repositories.MetodoPagoRepository;
import org.ucom.repositories.ProductoRepository;

@ApplicationScoped
public class VentaService implements IDAO<Venta, Integer> {
    private static final Logger LOG = Logger.getLogger(VentaService.class);
    @Inject
    private VentaRepository repository;

    @Inject
    private VentaDetalleRepository repositoryDetalle;

    @Inject
    private ClienteRepository clienteRepository;

    @Inject
    private ProductoRepository productoRepository;

    @Inject
    private MetodoPagoRepository metodoPagoRepository;

    @Override
    public Venta obtener(Integer param) {
        // TODO Auto-generated method stub
        return this.repository.findById(param).orElse(null);
    }

    @Override
    @Transactional
    public Venta agregar(Venta param) {

        try { // # 2
            LOG.info(param);

            Venta aux = new Venta();
            aux.setClienteId(param.getClienteId());
            aux.setFecha(param.getFecha());
            aux.setMetodoPagoId(param.getMetodoPagoId());
            aux.setTotal(param.getTotal());

            Venta saved = this.repository.save(aux);
            System.out.println(aux.toString());

            List<VentaDetalle> vdList = param.getVentaDetalleList();
            for (VentaDetalle item : vdList) {
                VentaDetalle vdt = new VentaDetalle();
                vdt.setVentaId(saved);
                vdt.setProductoId(item.getProductoId());
                vdt.setSubtotal(item.getSubtotal());

                this.repositoryDetalle.save(vdt);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return param;
    }

    @Override
    public Venta modificar(Venta param) {
        // TODO Auto-generated method stub
        return this.repository.save(param);
    }

    @Override
    public void eliminar(Integer param) {
        // TODO Auto-generated method stub

        this.repository.deleteById(param);
    }

    @Override
    public List<Venta> listar() {
        return this.repository.findAll();
    }

    public ResumenVentaDTO obtenerResumen(Integer ventaId) {
        ResumenVentaDTO data = new ResumenVentaDTO();
        Venta v = this.repository.findById(ventaId).orElse(null);
        Cliente clie = v.getClienteId();
        data.setRazonSocial(clie.getNombres() + " " + clie.getApellidos());
        data.setDocumento(clie.getDocumento());
        data.setFecha(v.getFecha());
        List<VentaDetalleDTO> detalle = new ArrayList<>();
        for (VentaDetalle item : v.getVentaDetalleList()) {
            VentaDetalleDTO vdto = new VentaDetalleDTO();
            vdto.setCantidad(item.getCantidad());
            vdto.setSubtotal(item.getSubtotal());
            vdto.setDescripcion(item.getProductoId().getDescripcion());
            detalle.add(vdto);
        }
        data.setDetalle(detalle);

        return data;
    }

    // implement registrarVenta
    public Venta registrarVenta(RegistrarVentaParam param) throws InsufficientStockException {

        // Crear cliente en base a su id
        Cliente cliente = this.clienteRepository.findById(param.getClienteId()).orElse(null);
        // Crear venta
        Venta venta = new Venta();
        // Setear el cliente
        venta.setClienteId(cliente);
        // setear el total
        venta.setTotal(param.getTotal());
        // setear la fecha
        venta.setFecha(param.getFecha());
        // Crear el metodo de pago en base a su id
        MetodoPago mp = this.metodoPagoRepository.findById(param.getMetodoPagoId()).orElse(null);
        // setear el metodo de pago
        venta.setMetodoPagoId(mp);

        // Obtener el producto para restar la cantidad vendida de su total
        Producto producto = this.productoRepository.findById(param.getProductoId()).orElse(null);

        // Comprobar que el stock del producto es mayor a la cantidad a vender
        int cantidadStock = producto.getStock();
        if (cantidadStock < param.getCantidad()) {
            throw new InsufficientStockException("No hay stock suficiente para la venta");
        }

        producto.setStock(cantidadStock - param.getCantidad());

        // Guardar venta
        this.repository.save(venta);

        // Guardar producto
        this.productoRepository.save(producto);

        return venta;
    }

}
