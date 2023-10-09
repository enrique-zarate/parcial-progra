package org.ucom.controllers;

import org.ucom.entities.Ajedrez.AjedrezParam;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/ajedrez")
public class AjedrezController {
    @POST
    @Path("/movimiento-peon")
    public Boolean movimientoPeon(AjedrezParam ajedrezParam) {
        Peon p = new Peon();

        return true;

    }
}
