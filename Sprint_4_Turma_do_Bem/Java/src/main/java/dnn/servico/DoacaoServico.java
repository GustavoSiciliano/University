package dnn.servico;

import dnn.boo.ApiExcecao;
import dnn.dao.Conexao;
import dnn.modelo.Doacao;

import java.sql.*;
import java.util.*;

// CRUD de doações com validações
public class DoacaoServico {

    private static final List<String> FORMAS = List.of("PIX", "BOLETO", "CARTAO", "TRANSFERENCIA", "DINHEIRO");

    public List<Doacao> listar() throws SQLException {
        List<Doacao> lista = new ArrayList<>();
        String sql = "SELECT d.id_doacao,d.id_doador,d.id_campanha,d.valor," +
                "TO_CHAR(d.data_doacao,'YYYY-MM-DD') AS data_doacao,d.forma_pgto,d.observacoes,pe.nome AS nome_doador " +
                "FROM tdb_Doacao d LEFT JOIN tdb_Doador do ON do.id_doador=d.id_doador " +
                "LEFT JOIN tdb_Pessoa pe ON pe.cpf=do.cpf ORDER BY d.data_doacao DESC";
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(sql); ResultSet r = s.executeQuery()) {
            while (r.next()) lista.add(mapear(r));
        }
        return lista;
    }

    public Doacao criar(Doacao d) throws SQLException {
        if (!d.valorValido()) throw new ApiExcecao("Valor deve ser maior que zero.", 400);
        if (!FORMAS.contains(d.getFormaPgto())) throw new ApiExcecao("Forma de pagamento invalida: " + FORMAS, 400);
        int id;
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement("SELECT NVL(MAX(id_doacao),0)+1 FROM tdb_Doacao"); ResultSet r = s.executeQuery()) {
            id = r.next() ? r.getInt(1) : 1;
        }
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(
                "INSERT INTO tdb_Doacao(id_doacao,id_doador,id_campanha,valor,data_doacao,forma_pgto,observacoes) VALUES(?,?,?,?,SYSDATE,?,?)")) {
            s.setInt(1, id);
            s.setInt(2, d.getIdDoador());
            s.setInt(3, d.getIdCampanha());
            s.setDouble(4, d.getValor());
            s.setString(5, d.getFormaPgto());
            s.setString(6, d.getObservacoes());
            s.executeUpdate();
            d.setIdDoacao(id);
            return d;
        }
    }

    public void excluir(int id) throws SQLException {
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement("DELETE FROM tdb_Doacao WHERE id_doacao=?")) {
            s.setInt(1, id);
            if (s.executeUpdate() == 0) throw new ApiExcecao("Doacao nao encontrada.", 404);
        }
    }

    private Doacao mapear(ResultSet r) throws SQLException {
        Doacao d = new Doacao();
        d.setIdDoacao(r.getInt("id_doacao"));
        d.setIdDoador(r.getInt("id_doador"));
        d.setIdCampanha(r.getInt("id_campanha"));
        d.setValor(r.getDouble("valor"));
        d.setDataDoacao(r.getString("data_doacao"));
        d.setFormaPgto(r.getString("forma_pgto"));
        d.setObservacoes(r.getString("observacoes"));
        d.setNomeDoador(r.getString("nome_doador"));
        return d;
    }
}
