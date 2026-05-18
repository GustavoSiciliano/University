package dnn.servico;

import dnn.boo.ApiExcecao;
import dnn.dao.Conexao;
import dnn.modelo.Doador;

import java.sql.*;
import java.util.*;

// CRUD de doadores com validações
public class DoadorServico {

    public List<Doador> listar() throws SQLException {
        List<Doador> lista = new ArrayList<>();
        String sql = "SELECT d.id_doador,d.tipo_doador,pe.cpf,pe.nome,pe.telefone,pe.email," +
                "pe.cep,pe.logradouro,pe.numero,pe.bairro,pe.cidade,pe.uf " +
                "FROM tdb_Doador d JOIN tdb_Pessoa pe ON pe.cpf=d.cpf ORDER BY pe.nome";
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(sql); ResultSet r = s.executeQuery()) {
            while (r.next()) lista.add(mapear(r));
        }
        return lista;
    }

    public Doador buscar(int id) throws SQLException {
        String sql = "SELECT d.id_doador,d.tipo_doador,pe.cpf,pe.nome,pe.telefone,pe.email," +
                "pe.cep,pe.logradouro,pe.numero,pe.bairro,pe.cidade,pe.uf " +
                "FROM tdb_Doador d JOIN tdb_Pessoa pe ON pe.cpf=d.cpf WHERE d.id_doador=?";
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, id);
            ResultSet r = s.executeQuery();
            return r.next() ? mapear(r) : null;
        }
    }

    public Doador cadastrar(Doador d) throws SQLException {
        if (!d.nomeValido()) throw new ApiExcecao("Nome obrigatorio.", 400);
        if (!d.cpfValido()) throw new ApiExcecao("CPF invalido.", 400);
        if (!d.tipoValido()) throw new ApiExcecao("Tipo deve ser PF ou PJ.", 400);
        if (!d.emailValido()) throw new ApiExcecao("Email invalido.", 400);

        try (Connection conn = Conexao.conectar()) {
            conn.setAutoCommit(false);
            try {
                int novoId;
                try (PreparedStatement si = conn.prepareStatement("SELECT NVL(MAX(id_doador),0)+1 FROM tdb_Doador");
                     ResultSet ri = si.executeQuery()) {
                    novoId = ri.next() ? ri.getInt(1) : 1;
                }

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

                PreparedStatement sd = conn.prepareStatement("INSERT INTO tdb_Doador(id_doador,cpf,tipo_doador) VALUES(?,?,?)");
                sd.setInt(1, novoId);
                sd.setString(2, d.getCpf());
                sd.setString(3, d.getTipoDoador());
                sd.executeUpdate();
                conn.commit();
                d.setIdDoador(novoId);
                return d;
            } catch (SQLException e) {
                conn.rollback();
                if (e.getMessage().contains("PK_TDB_PESSOA") || e.getMessage().contains("UK_TDB_DOADOR_CPF"))
                    throw new ApiExcecao("CPF ja cadastrado.", 409);
                throw e;
            }
        }
    }

    public void atualizar(int id, Doador d) throws SQLException {
        if (!d.nomeValido()) throw new ApiExcecao("Nome obrigatorio.", 400);
        try (Connection conn = Conexao.conectar()) {
            conn.setAutoCommit(false);
            try {
                PreparedStatement sp = conn.prepareStatement(
                        "UPDATE tdb_Pessoa SET nome=?,telefone=?,email=? WHERE cpf=(SELECT cpf FROM tdb_Doador WHERE id_doador=?)");
                sp.setString(1, d.getNome());
                sp.setString(2, d.getTelefone());
                sp.setString(3, d.getEmail());
                sp.setInt(4, id);
                sp.executeUpdate();

                PreparedStatement sd = conn.prepareStatement("UPDATE tdb_Doador SET tipo_doador=? WHERE id_doador=?");
                sd.setString(1, d.getTipoDoador());
                sd.setInt(2, id);
                if (sd.executeUpdate() == 0) throw new ApiExcecao("Doador nao encontrado.", 404);
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
                "DELETE FROM tdb_Pessoa WHERE cpf=(SELECT cpf FROM tdb_Doador WHERE id_doador=?)")) {
            s.setInt(1, id);
            if (s.executeUpdate() == 0) throw new ApiExcecao("Doador nao encontrado.", 404);
        }
    }

    private Doador mapear(ResultSet r) throws SQLException {
        Doador d = new Doador();
        d.setIdDoador(r.getInt("id_doador"));
        d.setTipoDoador(r.getString("tipo_doador"));
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
        return d;
    }
}
