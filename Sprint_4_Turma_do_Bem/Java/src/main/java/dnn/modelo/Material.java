package dnn.modelo;

public class Material {

    private int idMaterial;
    private String nome, descricao, unidade, validade;
    private double quantidade, quantidadeMinima;

    public Material() {
    }

    public Material(String nome, String descricao, double quantidade,
                    double quantidadeMinima, String unidade, String validade) {
        this.nome = nome;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.quantidadeMinima = quantidadeMinima;
        this.unidade = unidade;
        this.validade = validade;
    }

    // Estoque abaixo do mínimo — gera alerta no dashboard
    public boolean estoqueBaixo() {
        return quantidade < quantidadeMinima;
    }

    // Quantidade não pode ser negativa
    public boolean quantidadeValida() {
        return quantidade >= 0;
    }

    // Nome obrigatório
    public boolean nomeValido() {
        return nome != null && !nome.trim().isEmpty();
    }

    // Percentual do estoque em relação ao mínimo
    public double percentualEstoque() {
        return quantidadeMinima > 0 ? (quantidade / quantidadeMinima) * 100 : 100;
    }

    @Override
    public String toString() {
        return String.format("Material{id=%d, nome='%s', qtd=%.0f, min=%.0f, baixo=%b}",
                idMaterial, nome, quantidade, quantidadeMinima, estoqueBaixo());
    }

    public int getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(int v) {
        idMaterial = v;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String v) {
        nome = v;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String v) {
        descricao = v;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String v) {
        unidade = v;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String v) {
        validade = v;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double v) {
        quantidade = v;
    }

    public double getQuantidadeMinima() {
        return quantidadeMinima;
    }

    public void setQuantidadeMinima(double v) {
        quantidadeMinima = v;
    }
}
