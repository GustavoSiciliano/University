package dnn.boo;

import dnn.modelo.Dentista;
import dnn.servico.DentistaServico;

import java.sql.SQLException;
import java.util.List;

// Regras de negócio para Dentistas
public class DentistaBoo {

    private final DentistaServico srv = new DentistaServico();

    public List<Dentista> listar() {
        try {
            return srv.listar();
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao listar dentistas: " + e.getMessage(), 500);
        }
    }

    public Dentista buscar(String cro) {
        if (cro == null || cro.isBlank()) throw new ApiExcecao("CRO obrigatorio.", 400);
        try {
            Dentista d = srv.buscar(cro);
            if (d == null) throw new ApiExcecao("Dentista nao encontrado.", 404);
            return d;
        } catch (ApiExcecao e) {
            throw e;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao buscar dentista: " + e.getMessage(), 500);
        }
    }

    public Dentista cadastrar(Dentista d) {
        if (!d.nomeValido()) throw new ApiExcecao("Nome invalido (minimo 3 caracteres).", 400);
        if (!d.cpfValido()) throw new ApiExcecao("CPF invalido (informe 11 digitos).", 400);
        if (!d.croValido()) throw new ApiExcecao("CRO obrigatorio.", 400);
        if (!d.emailValido()) throw new ApiExcecao("Email invalido.", 400);
        if (!d.telefoneValido()) throw new ApiExcecao("Telefone invalido.", 400);
        try {
            return srv.cadastrar(d);
        } catch (ApiExcecao e) {
            throw e;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao cadastrar dentista: " + e.getMessage(), 500);
        }
    }

    public void atualizar(String cro, Dentista d) {
        if (!d.nomeValido()) throw new ApiExcecao("Nome invalido.", 400);
        if (!d.emailValido()) throw new ApiExcecao("Email invalido.", 400);
        try {
            srv.atualizar(cro, d);
        } catch (ApiExcecao e) {
            throw e;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao atualizar dentista: " + e.getMessage(), 500);
        }
    }

    public void excluir(String cro) {
        if (cro == null || cro.isBlank()) throw new ApiExcecao("CRO obrigatorio.", 400);
        try {
            srv.excluir(cro);
        } catch (ApiExcecao e) {
            throw e;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao excluir dentista: " + e.getMessage(), 500);
        }
    }
}
