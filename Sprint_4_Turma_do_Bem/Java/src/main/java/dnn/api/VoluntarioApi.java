package dnn.api;

import dnn.boo.ApiExcecao;
import dnn.boo.VoluntarioBoo;
import dnn.modelo.Voluntario;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/api/voluntarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VoluntarioApi {

    private final VoluntarioBoo boo = new VoluntarioBoo();

    @GET
    public Response listar() {
        try {
            return Response.ok(boo.listar()).build();
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

    @POST
    public Response cadastrar(Voluntario v) {
        try {
            return Response.status(201).entity(boo.cadastrar(v)).build();
        } catch (ApiExcecao e) {
            return erro(e);
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") int id, Voluntario v) {
        try {
            boo.atualizar(id, v);
            return Response.ok(Map.of("mensagem", "Voluntario atualizado!")).build();
        } catch (ApiExcecao e) {
            return erro(e);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response excluir(@PathParam("id") int id) {
        try {
            boo.excluir(id);
            return Response.ok(Map.of("mensagem", "Voluntario removido!")).build();
        } catch (ApiExcecao e) {
            return erro(e);
        }
    }

    private Response erro(ApiExcecao e) {
        return Response.status(e.getStatus()).entity(Map.of("erro", e.getMessage())).build();
    }
}
