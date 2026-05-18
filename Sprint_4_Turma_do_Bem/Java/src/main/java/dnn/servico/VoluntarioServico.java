package dnn.servico;

import dnn.boo.ApiExcecao;
import dnn.dao.Conexao;
import dnn.modelo.Voluntario;

import java.sql.*;
import java.util.*;

// CRUD de voluntários com validações
public class VoluntarioServico {

    public List<Voluntario> listar() throws SQLException {
        List<Voluntario> lista = new ArrayList<>();
        String sql = "SELECT v.id_voluntario,pe.cpf,pe.nome,pe.telefone,pe.email,pe.cep,pe.logradouro," +
                "pe.numero,pe.bairro,pe.cidade,pe.uf,v.area,v.disponibilidade " +
                "FROM tdb_Voluntario v JOIN tdb_Pessoa pe ON pe.cpf=v.cpf ORDER BY pe.nome";
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(sql); ResultSet r = s.executeQuery()) {
            while (r.next()) lista.add(mapear(r));
        }
        return lista;
    }

    public Voluntario buscar(int id) throws SQLException {
        String sql = "SELECT v.id_voluntario,pe.cpf,pe.nome,pe.telefone,pe.email,pe.cep,pe.logradouro," +
                "pe.numero,pe.bairro,pe.cidade,pe.uf,v.area,v.disponibilidade " +
                "FROM tdb_Voluntario v JOIN tdb_Pessoa pe ON pe.cpf=v.cpf WHERE v.id_voluntario=?";
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, id);
            ResultSet r = s.executeQuery();
            return r.next() ? mapear(r) : null;
        }
    }

    public Voluntario cadastrar(Voluntario v) throws SQLException {
        if (!v.nomeValido()) throw new ApiExcecao("Nome obrigatorio.", 400);
        if (!v.cpfValido()) throw new ApiExcecao("CPF invalido.", 400);
        try (Connection conn = Conexao.conectar()) {
            conn.setAutoCommit(false);
            try {
                int novoId;
                try (PreparedStatement si = conn.prepareStatement("SELECT NVL(MAX(id_voluntario),0)+1 FROM tdb_Voluntario");
                     ResultSet ri = si.executeQuery()) {
                    novoId = ri.next() ? ri.getInt(1) : 1;
                }

                PreparedStatement sp = conn.prepareStatement(
                        "INSERT INTO tdb_Pessoa(cpf,nome,data_nasc,telefone,email,cep,logradouro,numero,bairro,cidade,uf,ativo,dt_cadastro) " +
                                "VALUES(?,?,TO_DATE(?,'YYYY-MM-DD'),?,?,?,?,?,?,?,?,'S',SYSDATE)");
                sp.setString(1, v.getCpf());
                sp.setString(2, v.getNome());
                sp.setString(3, v.getDataNasc());
                sp.setString(4, v.getTelefone());
                sp.setString(5, v.getEmail());
                sp.setString(6, v.getCep());
                sp.setString(7, v.getLogradouro());
                sp.setString(8, v.getNumero());
                sp.setString(9, v.getBairro());
                sp.setString(10, v.getCidade());
                sp.setString(11, v.getUf());
                sp.executeUpdate();

                PreparedStatement sv = conn.prepareStatement("INSERT INTO tdb_Voluntario(id_voluntario,cpf,area,disponibilidade) VALUES(?,?,?,?)");
                sv.setInt(1, novoId);
                sv.setString(2, v.getCpf());
                sv.setString(3, v.getArea());
                sv.setString(4, v.getDisponibilidade());
                sv.executeUpdate();
                conn.commit();
                v.setIdVoluntario(novoId);
                return v;
            } catch (SQLException e) {
                conn.rollback();
                if (e.getMessage().contains("PK_TDB_PESSOA")) throw new ApiExcecao("CPF ja cadastrado.", 409);
                throw e;
            }
        }
    }

    public void atualizar(int id, Voluntario v) throws SQLException {
        if (!v.nomeValido()) throw new ApiExcecao("Nome obrigatorio.", 400);
        try (Connection conn = Conexao.conectar()) {
            conn.setAutoCommit(false);
            try {
                PreparedStatement sp = conn.prepareStatement(
                        "UPDATE tdb_Pessoa SET nome=?,telefone=?,email=? WHERE cpf=(SELECT cpf FROM tdb_Voluntario WHERE id_voluntario=?)");
                sp.setString(1, v.getNome());
                sp.setString(2, v.getTelefone());
                sp.setString(3, v.getEmail());
                sp.setInt(4, id);
                sp.executeUpdate();
                PreparedStatement sv = conn.prepareStatement("UPDATE tdb_Voluntario SET area=?,disponibilidade=? WHERE id_voluntario=?");
                sv.setString(1, v.getArea());
                sv.setString(2, v.getDisponibilidade());
                sv.setInt(3, id);
                if (sv.executeUpdate() == 0) throw new ApiExcecao("Voluntario nao encontrado.", 404);
                conn.commit();
            } catch (ApiExcecao e) {
                conn.rollback();
                throw e;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public void excluir(int id) throws SQLException {
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(
                "DELETE FROM tdb_Pessoa WHERE cpf=(SELECT cpf FROM tdb_Voluntario WHERE id_voluntario=?)")) {
            s.setInt(1, id);
            if (s.executeUpdate() == 0) throw new ApiExcecao("Voluntario nao encontrado.", 404);
        }
    }

    private Voluntario mapear(ResultSet r) throws SQLException {
        Voluntario v = new Voluntario();
        v.setIdVoluntario(r.getInt("id_voluntario"));
        v.setCpf(r.getString("cpf"));
        v.setNome(r.getString("nome"));
        v.setTelefone(r.getString("telefone"));
        v.setEmail(r.getString("email"));
        v.setCep(r.getString("cep"));
        v.setLogradouro(r.getString("logradouro"));
        v.setNumero(r.getString("numero"));
        v.setBairro(r.getString("bairro"));
        v.setCidade(r.getString("cidade"));
        v.setUf(r.getString("uf"));
        v.setArea(r.getString("area"));
        v.setDisponibilidade(r.getString("disponibilidade"));
        return v;
    }
}
