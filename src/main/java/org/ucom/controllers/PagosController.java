package org.ucom.controllers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.ucom.entities.Sistema.Pago;
import org.ucom.entities.Sistema.Producto;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import javax.ws.*;
import jakarta.ws.rs.core.Response;

@Path("/tienda/caja/{documentoCliente}/{documentoUsuario}")
public class PagosController {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registrarPago(List<Producto> productos, @PathParam("documentoCliente") String docCliente,
            @PathParam("documentoUsuario") String docUsuario) {
        // lógica para registrar el pago

        Pago pago = new Pago();
        pago.setDocumentoCliente(docCliente);
        pago.setDocumentoUsuario(docUsuario);
        pago.setProductos(productos);

        // Calcular el total del pago
        double total = 0;
        for (Producto producto : productos) {
            total += producto.getPrecio();
        }
        pago.setTotal(total);

        // Aquí podrías escribir el objeto Pago a un archivo JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("pagos.json"), pago);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Response.ok(pago).build();
    }
}
