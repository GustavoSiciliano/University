package dnn.servico;

import dnn.boo.ApiExcecao;
import dnn.dao.Conexao;
import dnn.modelo.Campanha;

import java.sql.*;
import java.util.*;

// Acesso ao banco para tdb_Campanha
public class CampanhaServico {

    public List<Campanha> listar() throws SQLException {
        List<Campanha> lista = new ArrayList<>();
        String sql = "SELECT id_campanha,nome,descricao," +
                "TO_CHAR(data_inicio,'YYYY-MM-DD') AS data_inicio," +
                "TO_CHAR(data_fim,'YYYY-MM-DD') AS data_fim," +
                "meta_valor,total_arrecadado,ativo " +
                "FROM tdb_Campanha ORDER BY data_inicio DESC";
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(sql); ResultSet r = s.executeQuery()) {
            while (r.next()) lista.add(mapear(r));
        }
        return lista;
    }

    public Campanha buscarPorId(int id) throws SQLException {
        String sql = "SELECT id_campanha,nome,descricao," +
                "TO_CHAR(data_inicio,'YYYY-MM-DD') AS data_inicio," +
                "TO_CHAR(data_fim,'YYYY-MM-DD') AS data_fim," +
                "meta_valor,total_arrecadado,ativo " +
                "FROM tdb_Campanha WHERE id_campanha=?";
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, id);
            ResultSet r = s.executeQuery();
            return r.next() ? mapear(r) : null;
        }
    }

    public void salvar(Campanha c) throws SQLException {
        int id = proximoId();
        String sql = "INSERT INTO tdb_Campanha" +
                "(id_campanha,nome,descricao,data_inicio,data_fim,meta_valor,total_arrecadado,ativo) " +
                "VALUES(?,?,?,TO_DATE(?,'YYYY-MM-DD'),TO_DATE(?,'YYYY-MM-DD'),?,0,'S')";
        try (Connection conn = Conexao.conectar(); PreparedStatement s = conn.prepareStatement(sql)) {
            s.setInt(1, id);
            s.setString(2, c.getNome());
            s.setString(3, c.getDescricao());
            s.setString(4, c.getDataInicio());
            s.setString(5, c.getDataFim());
            s.setDouble(6, c.getMetaValor());
            s.executeUpdate();
            c.setIdCampanha(id);
        }
    }

    public boolean atualizar(int id, Campanha c) throws SQLException {
        String sql = "UPDATE tdb_Campanha SET nome=?,descricao=?," +
                "data_fim=TO_DATE(?,'YYYY-MM-DD'),meta_valor=?,ativo=? " +
                "WHERE id_campanha=?";
        try (Connection conn = Conexao.conectar(); PreparedStatement s = conn.prepareStatement(sql)) {
            s.setString(1, c.getNome());
            s.setString(2, c.getDescricao());
            s.setString(3, c.getDataFim());
            s.setDouble(4, c.getMetaValor());
            s.setString(5, c.getAtivo());
            s.setInt(6, id);
            return s.executeUpdate() > 0;
        }
    }

    public boolean excluir(int id) throws SQLException {
        try (Connection c = Conexao.conectar();
             PreparedStatement s = c.prepareStatement("DELETE FROM tdb_Campanha WHERE id_campanha=?")) {
            s.setInt(1, id);
            return s.executeUpdate() > 0;
        }
    }

    private int proximoId() throws SQLException {
        try (Connection c = Conexao.conectar();
             PreparedStatement s = c.prepareStatement("SELECT NVL(MAX(id_campanha),0)+1 FROM tdb_Campanha");
             ResultSet r = s.executeQuery()) {
            return r.next() ? r.getInt(1) : 1;
        }
    }

    private Campanha mapear(ResultSet r) throws SQLException {
        Campanha c = new Campanha();
        c.setIdCampanha(r.getInt("id_campanha"));
        c.setNome(r.getString("nome"));
        c.setDescricao(r.getString("descricao"));
        c.setDataInicio(r.getString("data_inicio"));
        c.setDataFim(r.getString("data_fim"));
        c.setMetaValor(r.getDouble("meta_valor"));
        c.setTotalArrecadado(r.getDouble("total_arrecadado"));
        c.setAtivo(r.getString("ativo"));
        return c;
    }
}
