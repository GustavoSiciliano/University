package dnn.api;

import dnn.boo.ApiExcecao;
import dnn.boo.CampanhaBoo;
import dnn.modelo.Campanha;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

// CRUD campanhas — /api/campanhas
@Path("/api/campanhas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CampanhaApi {

    private final CampanhaBoo boo = new CampanhaBoo();

    @GET
    public Response listar() {
        try {
            return Response.ok(boo.listar()).build();
        } catch (ApiExcecao e) {
            return erro(e.getMessage(), e.getStatus());
        }
    }

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") int id) {
        try {
            return Response.ok(boo.buscar(id)).build();
        } catch (ApiExcecao e) {
            return erro(e.getMessage(), e.getStatus());
        }
    }

    @POST
    public Response criar(Campanha c) {
        try {
            return Response.status(201).entity(boo.criar(c)).build();
        } catch (ApiExcecao e) {
            return erro(e.getMessage(), e.getStatus());
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") int id, Campanha c) {
        try {
            boo.atualizar(id, c);
            return Response.ok(Map.of("mensagem", "Campanha atualizada!")).build();
        } catch (ApiExcecao e) {
            return erro(e.getMessage(), e.getStatus());
        }
    }

    @DELETE
    @Path("/{id}")
    public Response excluir(@PathParam("id") int id) {
        try {
            boo.excluir(id);
            return Response.ok(Map.of("mensagem", "Campanha excluida!")).build();
        } catch (ApiExcecao e) {
            return erro(e.getMessage(), e.getStatus());
        }
    }

    private Response erro(String msg, int status) {
        return Response.status(status).entity(Map.of("erro", msg)).build();
    }
}
