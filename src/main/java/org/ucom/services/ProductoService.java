package org.ucom.services;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.ucom.config.IDAO;
import org.ucom.entities.Producto;
import org.ucom.repositories.ProductoRepository;

@ApplicationScoped
public class ProductoService implements IDAO<Producto, Integer> {
    @Inject
    private ProductoRepository repository;

    @Override
    public Producto obtener(Integer param) {
        return this.repository.findById(param).orElse(null);
    }

    @Override
    public Producto agregar(Producto param) {
        return this.repository.save(param);
    }

    @Override
    public Producto modificar(Producto param) {
        return this.repository.save(param);
    }

    @Override
    public void eliminar(Integer param) {

        this.repository.deleteById(param);
    }

    @Override
    public List<Producto> listar() {
        return this.repository.findAll();
    }

}
