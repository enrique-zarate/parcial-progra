package org.ucom.services;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.ucom.config.IDAO;
import org.ucom.entities.Cliente;
import org.ucom.repositories.ClienteRepository;

@ApplicationScoped
public class ClienteService implements IDAO<Cliente, Integer> {
    @Inject
    private ClienteRepository repository;

    @Override
    public Cliente obtener(Integer param) {
        return this.repository.findById(param).orElse(null);
    }

    @Override
    public Cliente agregar(Cliente param) {
        return this.repository.save(param);
    }

    @Override
    public Cliente modificar(Cliente param) {
        return this.repository.save(param);
    }

    @Override
    public void eliminar(Integer param) {

        this.repository.deleteById(param);
    }

    @Override
    public List<Cliente> listar() {
        return this.repository.findAll();
    }

}
