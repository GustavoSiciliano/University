package dnn.servico;

import dnn.boo.ApiExcecao;
import dnn.dao.Conexao;
import dnn.modelo.RegistroIA;

import java.sql.*;
import java.util.*;

// Leitura do histórico de predições da IA salvo no Oracle
public class RegistroIAServico {

    public List<RegistroIA> listarTodos() throws SQLException {
        List<RegistroIA> lista = new ArrayList<>();
        String sql = "SELECT id_registro,id_consulta,tipo_predicao,entrada_json,prob_resultado," +
                "classe_prevista,risco,modelo_versao,acerto," +
                "TO_CHAR(dt_predicao,'YYYY-MM-DD') AS dt_predicao " +
                "FROM tdb_RegistroIA ORDER BY dt_predicao DESC";
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(sql); ResultSet r = s.executeQuery()) {
            while (r.next()) lista.add(mapear(r));
        }
        return lista;
    }

    public List<RegistroIA> listarPorTipo(String tipo) throws SQLException {
        if (!List.of("FALTA", "ARRECADACAO").contains(tipo))
            throw new ApiExcecao("Tipo invalido. Use FALTA ou ARRECADACAO.", 400);
        List<RegistroIA> lista = new ArrayList<>();
        String sql = "SELECT id_registro,id_consulta,tipo_predicao,entrada_json,prob_resultado," +
                "classe_prevista,risco,modelo_versao,acerto," +
                "TO_CHAR(dt_predicao,'YYYY-MM-DD') AS dt_predicao " +
                "FROM tdb_RegistroIA WHERE tipo_predicao=? ORDER BY dt_predicao DESC";
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(sql)) {
            s.setString(1, tipo);
            ResultSet r = s.executeQuery();
            while (r.next()) lista.add(mapear(r));
        }
        return lista;
    }

    public RegistroIA buscar(int id) throws SQLException {
        String sql = "SELECT id_registro,id_consulta,tipo_predicao,entrada_json,prob_resultado," +
                "classe_prevista,risco,modelo_versao,acerto," +
                "TO_CHAR(dt_predicao,'YYYY-MM-DD') AS dt_predicao " +
                "FROM tdb_RegistroIA WHERE id_registro=?";
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, id);
            ResultSet r = s.executeQuery();
            return r.next() ? mapear(r) : null;
        }
    }

    // Salva registro de predicao no banco (chamado apos usar a API de IA)
    public RegistroIA salvar(RegistroIA reg) throws SQLException {
        if (!List.of("FALTA", "ARRECADACAO").contains(reg.getTipoPredicao()))
            throw new ApiExcecao("Tipo invalido.", 400);
        if (reg.getEntradaJson() == null || reg.getEntradaJson().isBlank())
            throw new ApiExcecao("Entrada JSON obrigatoria.", 400);

        int id;
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(
                "SELECT NVL(MAX(id_registro),0)+1 FROM tdb_RegistroIA"); ResultSet r = s.executeQuery()) {
            id = r.next() ? r.getInt(1) : 1;
        }
        String sql = "INSERT INTO tdb_RegistroIA(id_registro,id_consulta,tipo_predicao,entrada_json," +
                "prob_resultado,classe_prevista,risco,modelo_versao,dt_predicao) " +
                "VALUES(?,?,?,?,?,?,?,?,SYSDATE)";
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, id);
            if (reg.getIdConsulta() != null) s.setInt(2, reg.getIdConsulta());
            else s.setNull(2, Types.NUMERIC);
            s.setString(3, reg.getTipoPredicao());
            s.setString(4, reg.getEntradaJson());
            s.setDouble(5, reg.getProbResultado());
            s.setString(6, reg.getClassePrevista());
            s.setString(7, reg.getRisco());
            s.setString(8, reg.getModeloVersao() != null ? reg.getModeloVersao() : "v1.0");
            s.executeUpdate();
            reg.setIdRegistro(id);
            return reg;
        }
    }

    private RegistroIA mapear(ResultSet r) throws SQLException {
        RegistroIA reg = new RegistroIA();
        reg.setIdRegistro(r.getInt("id_registro"));
        int idC = r.getInt("id_consulta");
        reg.setIdConsulta(r.wasNull() ? null : idC);
        reg.setTipoPredicao(r.getString("tipo_predicao"));
        reg.setEntradaJson(r.getString("entrada_json"));
        reg.setProbResultado(r.getDouble("prob_resultado"));
        reg.setClassePrevista(r.getString("classe_prevista"));
        reg.setRisco(r.getString("risco"));
        reg.setModeloVersao(r.getString("modelo_versao"));
        reg.setAcerto(r.getString("acerto"));
        reg.setDtPredicao(r.getString("dt_predicao"));
        return reg;
    }
}
