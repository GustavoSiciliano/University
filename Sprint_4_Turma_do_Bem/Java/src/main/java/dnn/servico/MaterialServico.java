package dnn.servico;

import dnn.boo.ApiExcecao;
import dnn.dao.Conexao;
import dnn.modelo.Material;

import java.sql.*;
import java.util.*;

// CRUD de materiais com validações
public class MaterialServico {

    public List<Material> listar() throws SQLException {
        List<Material> lista = new ArrayList<>();
        String sql = "SELECT id_material,nome,descricao,quantidade,quantidade_minima,unidade," +
                "TO_CHAR(validade,'YYYY-MM-DD') AS validade FROM tdb_Material ORDER BY nome";
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(sql); ResultSet r = s.executeQuery()) {
            while (r.next()) lista.add(mapear(r));
        }
        return lista;
    }

    public Material buscar(int id) throws SQLException {
        String sql = "SELECT id_material,nome,descricao,quantidade,quantidade_minima,unidade," +
                "TO_CHAR(validade,'YYYY-MM-DD') AS validade FROM tdb_Material WHERE id_material=?";
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, id);
            ResultSet r = s.executeQuery();
            return r.next() ? mapear(r) : null;
        }
    }

    public Material criar(Material m) throws SQLException {
        if (m.getNome() == null || m.getNome().isBlank()) throw new ApiExcecao("Nome obrigatorio.", 400);
        if (m.getQuantidade() < 0) throw new ApiExcecao("Quantidade nao pode ser negativa.", 400);
        int id;
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement("SELECT NVL(MAX(id_material),0)+1 FROM tdb_Material"); ResultSet r = s.executeQuery()) {
            id = r.next() ? r.getInt(1) : 1;
        }
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(
                "INSERT INTO tdb_Material(id_material,nome,descricao,quantidade,quantidade_minima,unidade,validade) VALUES(?,?,?,?,?,?,TO_DATE(?,'YYYY-MM-DD'))")) {
            s.setInt(1, id);
            s.setString(2, m.getNome());
            s.setString(3, m.getDescricao());
            s.setDouble(4, m.getQuantidade());
            s.setDouble(5, m.getQuantidadeMinima());
            s.setString(6, m.getUnidade());
            s.setString(7, m.getValidade());
            s.executeUpdate();
            m.setIdMaterial(id);
            return m;
        }
    }

    public void atualizar(int id, Material m) throws SQLException {
        if (m.getQuantidade() < 0) throw new ApiExcecao("Quantidade nao pode ser negativa.", 400);
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement(
                "UPDATE tdb_Material SET nome=?,descricao=?,quantidade=?,quantidade_minima=?,unidade=?,validade=TO_DATE(?,'YYYY-MM-DD') WHERE id_material=?")) {
            s.setString(1, m.getNome());
            s.setString(2, m.getDescricao());
            s.setDouble(3, m.getQuantidade());
            s.setDouble(4, m.getQuantidadeMinima());
            s.setString(5, m.getUnidade());
            s.setString(6, m.getValidade());
            s.setInt(7, id);
            if (s.executeUpdate() == 0) throw new ApiExcecao("Material nao encontrado.", 404);
        }
    }

    public void excluir(int id) throws SQLException {
        try (Connection c = Conexao.conectar(); PreparedStatement s = c.prepareStatement("DELETE FROM tdb_Material WHERE id_material=?")) {
            s.setInt(1, id);
            if (s.executeUpdate() == 0) throw new ApiExcecao("Material nao encontrado.", 404);
        }
    }

    private Material mapear(ResultSet r) throws SQLException {
        Material m = new Material();
        m.setIdMaterial(r.getInt("id_material"));
        m.setNome(r.getString("nome"));
        m.setDescricao(r.getString("descricao"));
        m.setQuantidade(r.getDouble("quantidade"));
        m.setQuantidadeMinima(r.getDouble("quantidade_minima"));
        m.setUnidade(r.getString("unidade"));
        m.setValidade(r.getString("validade"));
        return m;
    }
}
