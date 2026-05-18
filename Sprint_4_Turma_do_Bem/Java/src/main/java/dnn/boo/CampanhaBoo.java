package dnn.boo;

import dnn.modelo.Campanha;
import dnn.servico.CampanhaServico;

import java.sql.SQLException;
import java.util.List;

// Regras de negócio para Campanhas
public class CampanhaBoo {

    private final CampanhaServico srv = new CampanhaServico();

    public List<Campanha> listar() {
        try {
            return srv.listar();
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao listar campanhas: " + e.getMessage(), 500);
        }
    }

    public Campanha buscar(int id) {
        if (id <= 0) throw new ApiExcecao("ID invalido.", 400);
        try {
            Campanha c = srv.buscarPorId(id);
            if (c == null) throw new ApiExcecao("Campanha nao encontrada.", 404);
            return c;
        } catch (ApiExcecao e) {
            throw e;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao buscar campanha: " + e.getMessage(), 500);
        }
    }

    public Campanha criar(Campanha c) {
        if (!c.nomeValido()) throw new ApiExcecao("Nome invalido (minimo 3 caracteres).", 400);
        if (!c.metaValida()) throw new ApiExcecao("Meta deve ser maior que zero.", 400);
        if (c.getDataInicio() == null || c.getDataInicio().isBlank())
            throw new ApiExcecao("Data de inicio obrigatoria.", 400);
        if (c.getDataFim() == null || c.getDataFim().isBlank())
            throw new ApiExcecao("Data de fim obrigatoria.", 400);
        try {
            srv.salvar(c);
            return c;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao criar campanha: " + e.getMessage(), 500);
        }
    }

    public void atualizar(int id, Campanha c) {
        if (id <= 0) throw new ApiExcecao("ID invalido.", 400);
        if (!c.nomeValido()) throw new ApiExcecao("Nome invalido.", 400);
        if (!c.metaValida()) throw new ApiExcecao("Meta deve ser maior que zero.", 400);
        try {
            if (!srv.atualizar(id, c)) throw new ApiExcecao("Campanha nao encontrada.", 404);
        } catch (ApiExcecao e) {
            throw e;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao atualizar campanha: " + e.getMessage(), 500);
        }
    }

    public void excluir(int id) {
        if (id <= 0) throw new ApiExcecao("ID invalido.", 400);
        try {
            if (!srv.excluir(id)) throw new ApiExcecao("Campanha nao encontrada.", 404);
        } catch (ApiExcecao e) {
            throw e;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao excluir campanha: " + e.getMessage(), 500);
        }
    }
}
