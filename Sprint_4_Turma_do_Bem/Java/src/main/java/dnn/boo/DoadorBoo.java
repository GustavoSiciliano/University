package dnn.boo;

import dnn.modelo.Doador;
import dnn.servico.DoadorServico;

import java.sql.SQLException;
import java.util.List;

// Regras de negócio para Doadores
public class DoadorBoo {

    private final DoadorServico srv = new DoadorServico();

    public List<Doador> listar() {
        try {
            return srv.listar();
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao listar doadores: " + e.getMessage(), 500);
        }
    }

    public Doador buscar(int id) {
        if (id <= 0) throw new ApiExcecao("ID invalido.", 400);
        try {
            Doador d = srv.buscar(id);
            if (d == null) throw new ApiExcecao("Doador nao encontrado.", 404);
            return d;
        } catch (ApiExcecao e) {
            throw e;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao buscar doador: " + e.getMessage(), 500);
        }
    }

    public Doador cadastrar(Doador d) {
        if (!d.nomeValido()) throw new ApiExcecao("Nome invalido (minimo 3 caracteres).", 400);
        if (!d.cpfValido()) throw new ApiExcecao("CPF invalido.", 400);
        if (!d.tipoValido()) throw new ApiExcecao("Tipo invalido. Use PF ou PJ.", 400);
        if (!d.emailValido()) throw new ApiExcecao("Email invalido.", 400);
        try {
            return srv.cadastrar(d);
        } catch (ApiExcecao e) {
            throw e;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao cadastrar doador: " + e.getMessage(), 500);
        }
    }

    public void atualizar(int id, Doador d) {
        if (!d.nomeValido()) throw new ApiExcecao("Nome invalido.", 400);
        if (!d.tipoValido()) throw new ApiExcecao("Tipo invalido. Use PF ou PJ.", 400);
        if (!d.emailValido()) throw new ApiExcecao("Email invalido.", 400);
        try {
            srv.atualizar(id, d);
        } catch (ApiExcecao e) {
            throw e;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao atualizar doador: " + e.getMessage(), 500);
        }
    }

    public void excluir(int id) {
        if (id <= 0) throw new ApiExcecao("ID invalido.", 400);
        try {
            srv.excluir(id);
        } catch (ApiExcecao e) {
            throw e;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao excluir doador: " + e.getMessage(), 500);
        }
    }
}
