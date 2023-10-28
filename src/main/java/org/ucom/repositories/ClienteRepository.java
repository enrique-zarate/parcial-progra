package org.ucom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.ucom.entities.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

}
