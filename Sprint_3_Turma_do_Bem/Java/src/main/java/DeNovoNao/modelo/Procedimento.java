package DeNovoNao.modelo;

// Classe que representa um procedimento
public class Procedimento {

    // Atributos do procedimento
    private int idProcedimento;
    private String nome;
    private String tipo;
    private int duracao;
    private String orientacao;
    private double custo;
    private int idConsulta;
    private String croDentista;
    private String cpfDentista;

    // Construtor vazio
    public Procedimento() {}

    // Construtor completo
    public Procedimento(int idProcedimento, String nome, String tipo, int duracao,
                        String orientacao, double custo, int idConsulta,
                        String croDentista, String cpfDentista) {

        this.idProcedimento = idProcedimento;
        this.nome = nome;
        this.tipo = tipo;
        this.duracao = duracao;
        this.orientacao = orientacao;
        this.custo = custo;
        this.idConsulta = idConsulta;
        this.croDentista = croDentista;
        this.cpfDentista = cpfDentista;
    }

    // Metodo para validar duração
    // A duração deve ser maior que 0
    public boolean duracaoValida() {

        return duracao > 0;
    }

    // Metodo para validar custo
    // O custo não pode ser negativo
    public boolean custoValido() {

        return custo >= 0;
    }

    // Metodo que verifica se o cadastro está válido
    // Precisa ter nome, consulta válida e dentista informado
    public boolean cadastroValido() {

        return nome != null && !nome.trim().isEmpty()
                && idConsulta > 0
                && croDentista != null && !croDentista.trim().isEmpty()
                && cpfDentista != null && !cpfDentista.trim().isEmpty()
                && custoValido();
    }

    // Getters e Setters

    public int getIdProcedimento() {
        return idProcedimento;
    }

    public void setIdProcedimento(int idProcedimento) {
        this.idProcedimento = idProcedimento;
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

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public String getOrientacao() {
        return orientacao;
    }

    public void setOrientacao(String orientacao) {
        this.orientacao = orientacao;
    }

    public double getCusto() {
        return custo;
    }

    public void setCusto(double custo) {
        this.custo = custo;
    }

    public int getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(int idConsulta) {
        this.idConsulta = idConsulta;
    }

    public String getCroDentista() {
        return croDentista;
    }

    public void setCroDentista(String croDentista) {
        this.croDentista = croDentista;
    }

    public String getCpfDentista() {
        return cpfDentista;
    }

    public void setCpfDentista(String cpfDentista) {
        this.cpfDentista = cpfDentista;
    }
}