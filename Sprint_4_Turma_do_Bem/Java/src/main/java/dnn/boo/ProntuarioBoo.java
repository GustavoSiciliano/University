package dnn.boo;

import dnn.modelo.Prontuario;
import dnn.servico.ProntuarioServico;

import java.sql.SQLException;
import java.util.List;

// Regras de negócio para Prontuários
public class ProntuarioBoo {

    private final ProntuarioServico srv = new ProntuarioServico();

    public List<Prontuario> listar() {
        try {
            return srv.listar();
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao listar prontuarios: " + e.getMessage(), 500);
        }
    }

    public Prontuario buscar(int id) {
        if (id <= 0) throw new ApiExcecao("ID invalido.", 400);
        try {
            Prontuario p = srv.buscar(id);
            if (p == null) throw new ApiExcecao("Prontuario nao encontrado.", 404);
            return p;
        } catch (ApiExcecao e) {
            throw e;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao buscar prontuario: " + e.getMessage(), 500);
        }
    }

    public Prontuario criar(Prontuario p) {
        if (!p.descricaoValida()) throw new ApiExcecao("Descricao obrigatoria.", 400);
        if (!p.consultaValida()) throw new ApiExcecao("ID da consulta invalido.", 400);
        try {
            return srv.criar(p);
        } catch (ApiExcecao e) {
            throw e;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao criar prontuario: " + e.getMessage(), 500);
        }
    }

    public void atualizar(int id, Prontuario p) {
        if (!p.descricaoValida()) throw new ApiExcecao("Descricao obrigatoria.", 400);
        try {
            srv.atualizar(id, p);
        } catch (ApiExcecao e) {
            throw e;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao atualizar prontuario: " + e.getMessage(), 500);
        }
    }

    public void excluir(int id) {
        if (id <= 0) throw new ApiExcecao("ID invalido.", 400);
        try {
            srv.excluir(id);
        } catch (ApiExcecao e) {
            throw e;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao excluir prontuario: " + e.getMessage(), 500);
        }
    }
}
