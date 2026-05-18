package dnn.boo;

import dnn.modelo.Material;
import dnn.servico.MaterialServico;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

// Regras de negócio para Materiais
public class MaterialBoo {

    private final MaterialServico srv = new MaterialServico();

    public List<Material> listar() {
        try {
            return srv.listar();
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao listar materiais: " + e.getMessage(), 500);
        }
    }

    // Lista apenas os materiais com estoque abaixo do mínimo
    public List<Material> listarEstoqueBaixo() {
        return listar().stream().filter(Material::estoqueBaixo).collect(Collectors.toList());
    }

    public Material buscar(int id) {
        if (id <= 0) throw new ApiExcecao("ID invalido.", 400);
        try {
            Material m = srv.buscar(id);
            if (m == null) throw new ApiExcecao("Material nao encontrado.", 404);
            return m;
        } catch (ApiExcecao e) {
            throw e;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao buscar material: " + e.getMessage(), 500);
        }
    }

    public Material criar(Material m) {
        if (!m.nomeValido()) throw new ApiExcecao("Nome obrigatorio.", 400);
        if (!m.quantidadeValida()) throw new ApiExcecao("Quantidade nao pode ser negativa.", 400);
        if (m.getQuantidadeMinima() < 0) throw new ApiExcecao("Quantidade minima nao pode ser negativa.", 400);
        try {
            return srv.criar(m);
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao criar material: " + e.getMessage(), 500);
        }
    }

    public void atualizar(int id, Material m) {
        if (!m.nomeValido()) throw new ApiExcecao("Nome obrigatorio.", 400);
        if (!m.quantidadeValida()) throw new ApiExcecao("Quantidade nao pode ser negativa.", 400);
        try {
            srv.atualizar(id, m);
        } catch (ApiExcecao e) {
            throw e;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao atualizar material: " + e.getMessage(), 500);
        }
    }

    public void excluir(int id) {
        if (id <= 0) throw new ApiExcecao("ID invalido.", 400);
        try {
            srv.excluir(id);
        } catch (ApiExcecao e) {
            throw e;
        } catch (SQLException e) {
            throw new ApiExcecao("Erro ao excluir material: " + e.getMessage(), 500);
        }
    }
}
