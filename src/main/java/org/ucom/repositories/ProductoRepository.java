package org.ucom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ucom.entities.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

}
