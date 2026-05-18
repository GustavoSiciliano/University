package dnn.api;

import dnn.boo.ApiExcecao;
import dnn.boo.DoacaoBoo;
import dnn.modelo.Doacao;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/api/doacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DoacaoApi {

    private final DoacaoBoo boo = new DoacaoBoo();

    @GET
    public Response listar() {
        try {
            return Response.ok(boo.listar()).build();
        } catch (ApiExcecao e) {
            return erro(e);
        }
    }

    @POST
    public Response criar(Doacao d) {
        try {
            return Response.status(201).entity(boo.criar(d)).build();
        } catch (ApiExcecao e) {
            return erro(e);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response excluir(@PathParam("id") int id) {
        try {
            boo.excluir(id);
            return Response.ok(Map.of("mensagem", "Doacao removida!")).build();
        } catch (ApiExcecao e) {
            return erro(e);
        }
    }

    private Response erro(ApiExcecao e) {
        return Response.status(e.getStatus()).entity(Map.of("erro", e.getMessage())).build();
    }
}
