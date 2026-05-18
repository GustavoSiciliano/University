package dnn.boo;

import dnn.modelo.Doacao;
import dnn.servico.DoacaoServico;

import java.sql.SQLException;
import java.util.List;

// Regras de negócio para Doações
public class DoacaoBoo {

    private final DoacaoServico srv = new DoacaoServico();

    public List<Doacao> listar() {
        try {
            return srv.listar();
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao listar doacoes: " + e.getMessage(), 500);
        }
    }

    public Doacao criar(Doacao d) {
        if (!d.valorValido()) throw new ApiExcecao("Valor deve ser maior que zero.", 400);
        if (!d.formaPgtoValida())
            throw new ApiExcecao("Forma de pagamento invalida. Use: PIX, BOLETO, CARTAO, TRANSFERENCIA ou DINHEIRO.", 400);
        if (d.getIdDoador() <= 0) throw new ApiExcecao("ID do doador invalido.", 400);
        try {
            return srv.criar(d);
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao registrar doacao: " + e.getMessage(), 500);
        }
    }

    public void excluir(int id) {
        if (id <= 0) throw new ApiExcecao("ID invalido.", 400);
        try {
            srv.excluir(id);
        } catch (ApiExcecao e) {
            throw e;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao excluir doacao: " + e.getMessage(), 500);
        }
    }
}
