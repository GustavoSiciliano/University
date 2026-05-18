package dnn.api;

import dnn.boo.ApiExcecao;
import dnn.boo.UsuarioBoo;
import dnn.modelo.Usuario;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

// POST /api/usuarios/login — consumido pela tela de Login do React
@Path("/api/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioApi {

    private final UsuarioBoo boo = new UsuarioBoo();

    @POST
    @Path("/login")
    public Response login(Map<String, String> body) {
        try {
            return Response.ok(boo.login(body.get("login"), body.get("senha"))).build();
        } catch (ApiExcecao e) {
            return erro(e.getMessage(), e.getStatus());
        }
    }

    @GET
    public Response listar() {
        try {
            return Response.ok(boo.listar()).build();
        } catch (ApiExcecao e) {
            return erro(e.getMessage(), e.getStatus());
        }
    }

    @POST
    public Response cadastrar(Usuario u) {
        try {
            return Response.status(201).entity(boo.cadastrar(u)).build();
        } catch (ApiExcecao e) {
            return erro(e.getMessage(), e.getStatus());
        }
    }

    @DELETE
    @Path("/{id}")
    public Response desativar(@PathParam("id") int id) {
        try {
            boo.desativar(id);
            return Response.ok(Map.of("mensagem", "Usuario desativado.")).build();
        } catch (ApiExcecao e) {
            return erro(e.getMessage(), e.getStatus());
        }
    }

    private Response erro(String msg, int status) {
        return Response.status(status).entity(Map.of("erro", msg)).build();
    }
}
