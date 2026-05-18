package dnn.api;

import dnn.boo.ApiExcecao;
import dnn.boo.RegistroIABoo;
import dnn.modelo.RegistroIA;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/api/registros-ia")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RegistroIAApi {

    private final RegistroIABoo boo = new RegistroIABoo();

    @GET
    public Response listar() {
        try {
            return Response.ok(boo.listarTodos()).build();
        } catch (ApiExcecao e) {
            return erro(e);
        }
    }

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") int id) {
        try {
            return Response.ok(boo.buscar(id)).build();
        } catch (ApiExcecao e) {
            return erro(e);
        }
    }

    @GET
    @Path("/tipo/{tipo}")
    public Response listarPorTipo(@PathParam("tipo") String tipo) {
        try {
            return Response.ok(boo.listarPorTipo(tipo.toUpperCase())).build();
        } catch (ApiExcecao e) {
            return erro(e);
        }
    }

    @POST
    public Response salvar(RegistroIA r) {
        try {
            return Response.status(201).entity(boo.salvar(r)).build();
        } catch (ApiExcecao e) {
            return erro(e);
        }
    }

    private Response erro(ApiExcecao e) {
        return Response.status(e.getStatus()).entity(Map.of("erro", e.getMessage())).build();
    }
}
