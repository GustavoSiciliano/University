package dnn.boo;

import dnn.modelo.Voluntario;
import dnn.servico.VoluntarioServico;

import java.sql.SQLException;
import java.util.List;

// Regras de negócio para Voluntários
public class VoluntarioBoo {

    private final VoluntarioServico srv = new VoluntarioServico();

    public List<Voluntario> listar() {
        try {
            return srv.listar();
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao listar voluntarios: " + e.getMessage(), 500);
        }
    }

    public Voluntario buscar(int id) {
        if (id <= 0) throw new ApiExcecao("ID invalido.", 400);
        try {
            Voluntario v = srv.buscar(id);
            if (v == null) throw new ApiExcecao("Voluntario nao encontrado.", 404);
            return v;
        } catch (ApiExcecao e) {
            throw e;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao buscar voluntario: " + e.getMessage(), 500);
        }
    }

    public Voluntario cadastrar(Voluntario v) {
        if (!v.nomeValido()) throw new ApiExcecao("Nome invalido (minimo 3 caracteres).", 400);
        if (!v.cpfValido()) throw new ApiExcecao("CPF invalido.", 400);
        if (!v.emailValido()) throw new ApiExcecao("Email invalido.", 400);
        if (!v.areaValida()) throw new ApiExcecao("Area de atuacao obrigatoria.", 400);
        try {
            return srv.cadastrar(v);
        } catch (ApiExcecao e) {
            throw e;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao cadastrar voluntario: " + e.getMessage(), 500);
        }
    }

    public void atualizar(int id, Voluntario v) {
        if (!v.nomeValido()) throw new ApiExcecao("Nome invalido.", 400);
        if (!v.emailValido()) throw new ApiExcecao("Email invalido.", 400);
        try {
            srv.atualizar(id, v);
        } catch (ApiExcecao e) {
            throw e;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao atualizar voluntario: " + e.getMessage(), 500);
        }
    }

    public void excluir(int id) {
        if (id <= 0) throw new ApiExcecao("ID invalido.", 400);
        try {
            srv.excluir(id);
        } catch (ApiExcecao e) {
            throw e;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao excluir voluntario: " + e.getMessage(), 500);
        }
    }
}
