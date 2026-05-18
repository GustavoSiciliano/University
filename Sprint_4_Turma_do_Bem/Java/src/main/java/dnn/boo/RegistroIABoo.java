package dnn.boo;

import dnn.modelo.RegistroIA;
import dnn.servico.RegistroIAServico;

import java.sql.SQLException;
import java.util.List;

// Regras de negócio para Registros de IA
public class RegistroIABoo {

    private final RegistroIAServico srv = new RegistroIAServico();

    public List<RegistroIA> listarTodos() {
        try {
            return srv.listarTodos();
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao listar registros: " + e.getMessage(), 500);
        }
    }

    public List<RegistroIA> listarPorTipo(String tipo) {
        if (tipo == null || (!tipo.equals("FALTA") && !tipo.equals("ARRECADACAO")))
            throw new ApiExcecao("Tipo invalido. Use FALTA ou ARRECADACAO.", 400);
        try {
            return srv.listarPorTipo(tipo);
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao listar registros: " + e.getMessage(), 500);
        }
    }

    public RegistroIA buscar(int id) {
        if (id <= 0) throw new ApiExcecao("ID invalido.", 400);
        try {
            RegistroIA r = srv.buscar(id);
            if (r == null) throw new ApiExcecao("Registro nao encontrado.", 404);
            return r;
        } catch (ApiExcecao e) {
            throw e;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao buscar registro: " + e.getMessage(), 500);
        }
    }

    public RegistroIA salvar(RegistroIA r) {
        if (!r.tipoValido()) throw new ApiExcecao("Tipo invalido. Use FALTA ou ARRECADACAO.", 400);
        if (!r.probValida()) throw new ApiExcecao("Probabilidade deve estar entre 0 e 1.", 400);
        if (r.getEntradaJson() == null || r.getEntradaJson().isBlank())
            throw new ApiExcecao("Entrada JSON obrigatoria.", 400);
        try {
            return srv.salvar(r);
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao salvar registro: " + e.getMessage(), 500);
        }
    }
}
