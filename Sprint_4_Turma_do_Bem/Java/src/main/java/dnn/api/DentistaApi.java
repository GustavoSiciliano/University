package dnn.api;

import dnn.boo.ApiExcecao;
import dnn.boo.DentistaBoo;
import dnn.modelo.Dentista;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/api/dentistas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DentistaApi {

    private final DentistaBoo boo = new DentistaBoo();

    @GET
    public Response listar() {
        try {
            return Response.ok(boo.listar()).build();
        } catch (ApiExcecao e) {
            return erro(e);
        }
    }

    @GET
    @Path("/{cro}")
    public Response buscar(@PathParam("cro") String cro) {
        try {
            return Response.ok(boo.buscar(cro)).build();
        } catch (ApiExcecao e) {
            return erro(e);
        }
    }

    @POST
    public Response cadastrar(Dentista d) {
        try {
            return Response.status(201).entity(boo.cadastrar(d)).build();
        } catch (ApiExcecao e) {
            return erro(e);
        }
    }

    @PUT
    @Path("/{cro}")
    public Response atualizar(@PathParam("cro") String cro, Dentista d) {
        try {
            boo.atualizar(cro, d);
            return Response.ok(Map.of("mensagem", "Dentista atualizado!")).build();
        } catch (ApiExcecao e) {
            return erro(e);
        }
    }

    @DELETE
    @Path("/{cro}")
    public Response excluir(@PathParam("cro") String cro) {
        try {
            boo.excluir(cro);
            return Response.ok(Map.of("mensagem", "Dentista removido!")).build();
        } catch (ApiExcecao e) {
            return erro(e);
        }
    }

    private Response erro(ApiExcecao e) {
        return Response.status(e.getStatus()).entity(Map.of("erro", e.getMessage())).build();
    }
}
