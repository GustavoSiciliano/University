package dnn.api;

import dnn.boo.ApiExcecao;
import dnn.boo.ProcedimentoBoo;
import dnn.modelo.Procedimento;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/api/procedimentos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProcedimentoApi {

    private final ProcedimentoBoo boo = new ProcedimentoBoo();

    @GET
    public Response listar() {
        try {
            return Response.ok(boo.listarTodos()).build();
        } catch (ApiExcecao e) {
            return erro(e);
        }
    }

    @GET
    @Path("/consulta/{idConsulta}")
    public Response listarPorConsulta(@PathParam("idConsulta") int idConsulta) {
        try {
            return Response.ok(boo.listarPorConsulta(idConsulta)).build();
        } catch (ApiExcecao e) {
            return erro(e);
        }
    }

    @POST
    public Response criar(Procedimento p) {
        try {
            return Response.status(201).entity(boo.criar(p)).build();
        } catch (ApiExcecao e) {
            return erro(e);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response excluir(@PathParam("id") int id) {
        try {
            boo.excluir(id);
            return Response.ok(Map.of("mensagem", "Procedimento removido!")).build();
        } catch (ApiExcecao e) {
            return erro(e);
        }
    }

    private Response erro(ApiExcecao e) {
        return Response.status(e.getStatus()).entity(Map.of("erro", e.getMessage())).build();
    }
}
