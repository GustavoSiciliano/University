package dnn.servico;

import dnn.boo.ApiExcecao;
import dnn.dao.Conexao;
import dnn.modelo.Prontuario;

import java.sql.*;
import java.util.*;

// CRUD de prontuários com validações
public class ProntuarioServico {

    private static final String SQL_BASE =
            "SELECT pr.id_prontuario,pr.id_consulta,pr.descricao,pr.observacoes," +
                    "TO_CHAR(pr.dt_registro,'YYYY-MM-DD') AS dt_registro,pac.nome AS nome_paciente,den.nome AS nome_dentista " +
                    "FROM tdb_Prontuario pr " +
                    "JOIN tdb_Consulta c  ON c.id_consulta=pr.id_consulta " +
                    "JOIN tdb_Paciente pa ON pa.id_paciente=c.id_paciente " +
                    "JOIN tdb_Pessoa pac  ON pac.cpf=pa.cpf " +
                    "JOIN tdb_Dentista d  ON d.cro=c.cro_dentista AND d.cpf=c.cpf_dentista " +
                    "JOIN tdb_Pessoa den  ON den.cpf=d.cpf ";

    public List<Prontuario> listar() throws SQLException {
        List<Prontuario> lista = new ArrayList<>();
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(SQL_BASE + "ORDER BY pr.dt_registro DESC"); ResultSet r = s.executeQuery()) {
            while (r.next()) lista.add(mapear(r));
        }
        return lista;
    }

    public Prontuario buscar(int id) throws SQLException {
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(SQL_BASE + "WHERE pr.id_prontuario=?")) {
            s.setInt(1, id);
            ResultSet r = s.executeQuery();
            return r.next() ? mapear(r) : null;
        }
    }

    public Prontuario criar(Prontuario p) throws SQLException {
        if (!p.descricaoValida()) throw new ApiExcecao("Descricao obrigatoria.", 400);
        if (p.getIdConsulta() <= 0) throw new ApiExcecao("ID da consulta obrigatorio.", 400);
        int id;
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement("SELECT NVL(MAX(id_prontuario),0)+1 FROM tdb_Prontuario"); ResultSet r = s.executeQuery()) {
            id = r.next() ? r.getInt(1) : 1;
        }
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(
                "INSERT INTO tdb_Prontuario(id_prontuario,id_consulta,descricao,observacoes,dt_registro) VALUES(?,?,?,?,SYSDATE)")) {
            s.setInt(1, id);
            s.setInt(2, p.getIdConsulta());
            s.setString(3, p.getDescricao());
            s.setString(4, p.getObservacoes());
            s.executeUpdate();
            p.setIdProntuario(id);
            return p;
        } catch (SQLException e) {
            if (e.getMessage().contains("UK_TDB_PRONT_CONSULTA"))
                throw new ApiExcecao("Consulta ja possui prontuario.", 409);
            throw e;
        }
    }

    public void atualizar(int id, Prontuario p) throws SQLException {
        if (!p.descricaoValida()) throw new ApiExcecao("Descricao obrigatoria.", 400);
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(
                "UPDATE tdb_Prontuario SET descricao=?,observacoes=?,ultima_alt=SYSDATE WHERE id_prontuario=?")) {
            s.setString(1, p.getDescricao());
            s.setString(2, p.getObservacoes());
            s.setInt(3, id);
            if (s.executeUpdate() == 0) throw new ApiExcecao("Prontuario nao encontrado.", 404);
        }
    }

    public void excluir(int id) throws SQLException {
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement("DELETE FROM tdb_Prontuario WHERE id_prontuario=?")) {
            s.setInt(1, id);
            if (s.executeUpdate() == 0) throw new ApiExcecao("Prontuario nao encontrado.", 404);
        }
    }

    private Prontuario mapear(ResultSet r) throws SQLException {
        Prontuario p = new Prontuario();
        p.setIdProntuario(r.getInt("id_prontuario"));
        p.setIdConsulta(r.getInt("id_consulta"));
        p.setDescricao(r.getString("descricao"));
        p.setObservacoes(r.getString("observacoes"));
        p.setDtRegistro(r.getString("dt_registro"));
        p.setNomePaciente(r.getString("nome_paciente"));
        p.setNomeDentista(r.getString("nome_dentista"));
        return p;
    }
}
