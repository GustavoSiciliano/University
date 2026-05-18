package dnn.servico;

import dnn.boo.ApiExcecao;
import dnn.dao.Conexao;
import dnn.modelo.Procedimento;

import java.sql.*;
import java.util.*;

// CRUD de procedimentos odontológicos vinculados a consultas
public class ProcedimentoServico {

    public List<Procedimento> listarPorConsulta(int idConsulta) throws SQLException {
        List<Procedimento> lista = new ArrayList<>();
        String sql = "SELECT id_procedimento,id_consulta,cro_dentista,cpf_dentista,nome,tipo,duracao_min,custo,orientacao " +
                "FROM tdb_Procedimento WHERE id_consulta=? ORDER BY id_procedimento";
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, idConsulta);
            ResultSet r = s.executeQuery();
            while (r.next()) lista.add(mapear(r));
        }
        return lista;
    }

    public List<Procedimento> listarTodos() throws SQLException {
        List<Procedimento> lista = new ArrayList<>();
        String sql = "SELECT id_procedimento,id_consulta,cro_dentista,cpf_dentista,nome,tipo,duracao_min,custo,orientacao " +
                "FROM tdb_Procedimento ORDER BY id_procedimento DESC";
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(sql); ResultSet r = s.executeQuery()) {
            while (r.next()) lista.add(mapear(r));
        }
        return lista;
    }

    public Procedimento criar(Procedimento p) throws SQLException {
        if (!p.nomeValido()) throw new ApiExcecao("Nome do procedimento obrigatorio.", 400);
        if (!p.custoValido()) throw new ApiExcecao("Custo nao pode ser negativo.", 400);
        if (p.getIdConsulta() <= 0) throw new ApiExcecao("ID da consulta obrigatorio.", 400);

        int id;
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(
                "SELECT NVL(MAX(id_procedimento),0)+1 FROM tdb_Procedimento"); ResultSet r = s.executeQuery()) {
            id = r.next() ? r.getInt(1) : 1;
        }
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(
                "INSERT INTO tdb_Procedimento(id_procedimento,id_consulta,cro_dentista,cpf_dentista,nome,tipo,duracao_min,custo,orientacao) " +
                        "VALUES(?,?,?,?,?,?,?,?,?)")) {
            s.setInt(1, id);
            s.setInt(2, p.getIdConsulta());
            s.setString(3, p.getCroDentista());
            s.setString(4, p.getCpfDentista());
            s.setString(5, p.getNome());
            s.setString(6, p.getTipo());
            s.setInt(7, p.getDuracaoMin());
            s.setDouble(8, p.getCusto());
            s.setString(9, p.getOrientacao());
            s.executeUpdate();
            p.setIdProcedimento(id);
            return p;
        }
    }

    public void excluir(int id) throws SQLException {
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(
                "DELETE FROM tdb_Procedimento WHERE id_procedimento=?")) {
            s.setInt(1, id);
            if (s.executeUpdate() == 0) throw new ApiExcecao("Procedimento nao encontrado.", 404);
        }
    }

    private Procedimento mapear(ResultSet r) throws SQLException {
        Procedimento p = new Procedimento();
        p.setIdProcedimento(r.getInt("id_procedimento"));
        p.setIdConsulta(r.getInt("id_consulta"));
        p.setCroDentista(r.getString("cro_dentista"));
        p.setCpfDentista(r.getString("cpf_dentista"));
        p.setNome(r.getString("nome"));
        p.setTipo(r.getString("tipo"));
        p.setDuracaoMin(r.getInt("duracao_min"));
        p.setCusto(r.getDouble("custo"));
        p.setOrientacao(r.getString("orientacao"));
        return p;
    }
}
