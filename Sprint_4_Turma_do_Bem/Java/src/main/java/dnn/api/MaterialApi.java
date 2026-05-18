package dnn.api;

import dnn.boo.ApiExcecao;
import dnn.boo.MaterialBoo;
import dnn.modelo.Material;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/api/materiais")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MaterialApi {

    private final MaterialBoo boo = new MaterialBoo();

    @GET
    public Response listar() {
        try {
            return Response.ok(boo.listar()).build();
        } catch (ApiExcecao e) {
            return erro(e);
        }
    }

    @GET
    @Path("/estoque-baixo")
    public Response listarEstoqueBaixo() {
        try {
            return Response.ok(boo.listarEstoqueBaixo()).build();
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
    public Response criar(Material m) {
        try {
            return Response.status(201).entity(boo.criar(m)).build();
        } catch (ApiExcecao e) {
            return erro(e);
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") int id, Material m) {
        try {
            boo.atualizar(id, m);
            return Response.ok(Map.of("mensagem", "Material atualizado!")).build();
        } catch (ApiExcecao e) {
            return erro(e);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response excluir(@PathParam("id") int id) {
        try {
            boo.excluir(id);
            return Response.ok(Map.of("mensagem", "Material removido!")).build();
        } catch (ApiExcecao e) {
            return erro(e);
        }
    }

    private Response erro(ApiExcecao e) {
        return Response.status(e.getStatus()).entity(Map.of("erro", e.getMessage())).build();
    }
}
