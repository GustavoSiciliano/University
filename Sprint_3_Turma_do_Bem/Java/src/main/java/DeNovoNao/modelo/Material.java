package DeNovoNao.modelo;

// Classe que representa um material
public class Material {

    // Atributos do material
    private int idMaterial;
    private String nome;
    private String tipo;
    private double quantidade;
    private String unidade;
    private String validade;

    // Construtor vazio
    public Material() {
    }

    // Construtor completo
    public Material(int idMaterial, String nome, String tipo, double quantidade,
                    String unidade, String validade) {

        this.idMaterial = idMaterial;
        this.nome = nome;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.unidade = unidade;
        this.validade = validade;
    }

    // Metodo para validar a quantidade
    // Não pode ser negativa
    public boolean quantidadeValida() {
        return quantidade >= 0;
    }

    // Metodo que verifica se o cadastro está válido
    // Nome é obrigatório e quantidade precisa ser válida
    public boolean cadastroValido() {
        return nome != null && !nome.trim().isEmpty()
                && quantidadeValida();
    }

    // Getters e Setters

    public int getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double q) {
        this.quantidade = q;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }
}