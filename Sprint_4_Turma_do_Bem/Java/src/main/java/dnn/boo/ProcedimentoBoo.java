package dnn.boo;

import dnn.modelo.Procedimento;
import dnn.servico.ProcedimentoServico;

import java.sql.SQLException;
import java.util.List;

// Regras de negócio para Procedimentos
public class ProcedimentoBoo {

    private final ProcedimentoServico srv = new ProcedimentoServico();

    public List<Procedimento> listarTodos() {
        try {
            return srv.listarTodos();
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao listar procedimentos: " + e.getMessage(), 500);
        }
    }

    public List<Procedimento> listarPorConsulta(int idConsulta) {
        if (idConsulta <= 0) throw new ApiExcecao("ID da consulta invalido.", 400);
        try {
            return srv.listarPorConsulta(idConsulta);
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao listar procedimentos: " + e.getMessage(), 500);
        }
    }

    public Procedimento criar(Procedimento p) {
        if (!p.nomeValido()) throw new ApiExcecao("Nome do procedimento obrigatorio.", 400);
        if (!p.custoValido()) throw new ApiExcecao("Custo nao pode ser negativo.", 400);
        if (!p.duracaoValida()) throw new ApiExcecao("Duracao deve ser maior que zero.", 400);
        if (p.getIdConsulta() <= 0) throw new ApiExcecao("ID da consulta invalido.", 400);
        try {
            return srv.criar(p);
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao criar procedimento: " + e.getMessage(), 500);
        }
    }

    public void excluir(int id) {
        if (id <= 0) throw new ApiExcecao("ID invalido.", 400);
        try {
            srv.excluir(id);
        } catch (ApiExcecao e) {
            throw e;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao excluir procedimento: " + e.getMessage(), 500);
        }
    }
}
