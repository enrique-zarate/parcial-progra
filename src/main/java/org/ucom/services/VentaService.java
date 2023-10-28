package org.ucom.services;

import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.ucom.config.IDAO;
import org.ucom.entities.Cliente;
import org.ucom.entities.Venta;
import org.ucom.entities.VentaDetalle;
import org.ucom.entities.dto.ResumenVentaDTO;
import org.ucom.entities.dto.VentaDetalleDTO;
import org.ucom.repositories.VentaDetalleRepository;
import org.ucom.repositories.VentaRepository;

@ApplicationScoped
public class VentaService implements IDAO<Venta, Integer> {
    private static final Logger LOG = Logger.getLogger(VentaService.class);
    @Inject
    private VentaRepository repository;

    @Inject
    private VentaDetalleRepository repositoryDetalle;

    @Override
    public Venta obtener(Integer param) {
        // TODO Auto-generated method stub
        return this.repository.findById(param).orElse(null);
    }

    @Override
    @Transactional
    public Venta agregar(Venta param) {

        try {
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

}
