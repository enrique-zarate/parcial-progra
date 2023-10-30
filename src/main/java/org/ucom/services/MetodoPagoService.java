package org.ucom.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.ucom.config.IDAO;
import org.ucom.entities.MetodoPago;
import org.ucom.repositories.MetodoPagoRepository;

@ApplicationScoped
public class MetodoPagoService implements IDAO<MetodoPago, Integer> {

    @Inject
    private MetodoPagoRepository repository;

    @Override
    public MetodoPago obtener(Integer param) {
        return this.repository.findById(param).orElse(null);
    }

    @Override
    public MetodoPago agregar(MetodoPago param) {
        return this.repository.save(param);
    }

    @Override
    public MetodoPago modificar(MetodoPago param) {
        return this.repository.save(param);
    }

    @Override
    public void eliminar(Integer param) {
        this.repository.deleteById(param);
    }

    @Override
    public List<MetodoPago> listar() {
        return this.repository.findAll();
    }

    public List<MetodoPago> buscarPorCodigo(String cod) {
        return this.repository.findByCodigo(cod);
    }

    public Long sumIds() {
        return this.repository.sumId();
    }

    public List<MetodoPago> paginado(Integer pagina, Integer cantidad) {

        Page<MetodoPago> lista = this.repository.findAll(
                PageRequest.of(pagina, cantidad));
        return lista.getContent();
    }

}