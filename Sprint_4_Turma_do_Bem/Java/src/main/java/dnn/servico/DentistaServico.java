package dnn.servico;

import dnn.boo.ApiExcecao;
import dnn.dao.Conexao;
import dnn.modelo.Dentista;

import java.sql.*;
import java.util.*;

// CRUD de dentistas com validações
public class DentistaServico {

    public List<Dentista> listar() throws SQLException {
        List<Dentista> lista = new ArrayList<>();
        String sql = "SELECT d.cro,pe.cpf,pe.nome,pe.telefone,pe.email,pe.cep,pe.logradouro," +
                "pe.numero,pe.bairro,pe.cidade,pe.uf,d.especialidade,d.disponibilidade " +
                "FROM tdb_Dentista d JOIN tdb_Pessoa pe ON pe.cpf=d.cpf ORDER BY pe.nome";
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(sql); ResultSet r = s.executeQuery()) {
            while (r.next()) lista.add(mapear(r));
        }
        return lista;
    }

    public Dentista buscar(String cro) throws SQLException {
        String sql = "SELECT d.cro,pe.cpf,pe.nome,pe.telefone,pe.email,pe.cep,pe.logradouro," +
                "pe.numero,pe.bairro,pe.cidade,pe.uf,d.especialidade,d.disponibilidade " +
                "FROM tdb_Dentista d JOIN tdb_Pessoa pe ON pe.cpf=d.cpf WHERE d.cro=?";
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(sql)) {
            s.setString(1, cro);
            ResultSet r = s.executeQuery();
            return r.next() ? mapear(r) : null;
        }
    }

    public Dentista cadastrar(Dentista d) throws SQLException {
        if (!d.nomeValido()) throw new ApiExcecao("Nome obrigatorio.", 400);
        if (!d.croValido()) throw new ApiExcecao("CRO obrigatorio.", 400);
        if (!d.cpfValido()) throw new ApiExcecao("CPF invalido.", 400);
        if (!d.emailValido()) throw new ApiExcecao("Email invalido.", 400);
        try (Connection conn = Conexao.conectar()) {
            conn.setAutoCommit(false);
            try {
                PreparedStatement sp = conn.prepareStatement(
                        "INSERT INTO tdb_Pessoa(cpf,nome,data_nasc,telefone,email,cep,logradouro,numero,bairro,cidade,uf,ativo,dt_cadastro) " +
                                "VALUES(?,?,TO_DATE(?,'YYYY-MM-DD'),?,?,?,?,?,?,?,?,'S',SYSDATE)");
                sp.setString(1, d.getCpf());
                sp.setString(2, d.getNome());
                sp.setString(3, d.getDataNasc());
                sp.setString(4, d.getTelefone());
                sp.setString(5, d.getEmail());
                sp.setString(6, d.getCep());
                sp.setString(7, d.getLogradouro());
                sp.setString(8, d.getNumero());
                sp.setString(9, d.getBairro());
                sp.setString(10, d.getCidade());
                sp.setString(11, d.getUf());
                sp.executeUpdate();

                PreparedStatement sd = conn.prepareStatement("INSERT INTO tdb_Dentista(cro,cpf,especialidade,disponibilidade) VALUES(?,?,?,?)");
                sd.setString(1, d.getCro());
                sd.setString(2, d.getCpf());
                sd.setString(3, d.getEspecialidade());
                sd.setString(4, d.getDisponibilidade());
                sd.executeUpdate();

                conn.commit();
                return d;
            } catch (SQLException e) {
                conn.rollback();
                if (e.getMessage().contains("PK_TDB_PESSOA")) throw new ApiExcecao("CPF ja cadastrado.", 409);
                throw e;
            }
        }
    }

    public void atualizar(String cro, Dentista d) throws SQLException {
        if (!d.nomeValido()) throw new ApiExcecao("Nome obrigatorio.", 400);
        try (Connection conn = Conexao.conectar()) {
            conn.setAutoCommit(false);
            try {
                PreparedStatement sp = conn.prepareStatement(
                        "UPDATE tdb_Pessoa SET nome=?,telefone=?,email=?,cep=?,logradouro=?,numero=?,bairro=?,cidade=?,uf=? " +
                                "WHERE cpf=(SELECT cpf FROM tdb_Dentista WHERE cro=?)");
                sp.setString(1, d.getNome());
                sp.setString(2, d.getTelefone());
                sp.setString(3, d.getEmail());
                sp.setString(4, d.getCep());
                sp.setString(5, d.getLogradouro());
                sp.setString(6, d.getNumero());
                sp.setString(7, d.getBairro());
                sp.setString(8, d.getCidade());
                sp.setString(9, d.getUf());
                sp.setString(10, cro);
                sp.executeUpdate();

                PreparedStatement sd = conn.prepareStatement("UPDATE tdb_Dentista SET especialidade=?,disponibilidade=? WHERE cro=?");
                sd.setString(1, d.getEspecialidade());
                sd.setString(2, d.getDisponibilidade());
                sd.setString(3, cro);
                if (sd.executeUpdate() == 0) throw new ApiExcecao("Dentista nao encontrado.", 404);
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

    public void excluir(String cro) throws SQLException {
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(
                "DELETE FROM tdb_Pessoa WHERE cpf=(SELECT cpf FROM tdb_Dentista WHERE cro=?)")) {
            s.setString(1, cro);
            if (s.executeUpdate() == 0) throw new ApiExcecao("Dentista nao encontrado.", 404);
        }
    }

    private Dentista mapear(ResultSet r) throws SQLException {
        Dentista d = new Dentista();
        d.setCro(r.getString("cro"));
        d.setCpf(r.getString("cpf"));
        d.setNome(r.getString("nome"));
        d.setTelefone(r.getString("telefone"));
        d.setEmail(r.getString("email"));
        d.setCep(r.getString("cep"));
        d.setLogradouro(r.getString("logradouro"));
        d.setNumero(r.getString("numero"));
        d.setBairro(r.getString("bairro"));
        d.setCidade(r.getString("cidade"));
        d.setUf(r.getString("uf"));
        d.setEspecialidade(r.getString("especialidade"));
        d.setDisponibilidade(r.getString("disponibilidade"));
        return d;
    }
}
