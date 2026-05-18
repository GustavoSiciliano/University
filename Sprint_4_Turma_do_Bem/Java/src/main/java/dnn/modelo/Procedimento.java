package dnn.modelo;

public class Procedimento {

    private int idProcedimento, idConsulta, duracaoMin;
    private String croDentista, cpfDentista, nome, tipo, orientacao;
    private double custo;

    public Procedimento() {
    }

    public Procedimento(int idConsulta, String croDentista, String cpfDentista,
                        String nome, String tipo, int duracaoMin,
                        double custo, String orientacao) {
        this.idConsulta = idConsulta;
        this.croDentista = croDentista;
        this.cpfDentista = cpfDentista;
        this.nome = nome;
        this.tipo = tipo;
        this.duracaoMin = duracaoMin;
        this.custo = custo;
        this.orientacao = orientacao;
    }

    // Nome obrigatório
    public boolean nomeValido() {
        return nome != null && !nome.trim().isEmpty();
    }

    // Custo não pode ser negativo (procedimentos da ONG são gratuitos = 0)
    public boolean custoValido() {
        return custo >= 0;
    }

    // Duração deve ser positiva
    public boolean duracaoValida() {
        return duracaoMin > 0;
    }

    @Override
    public String toString() {
        return String.format("Procedimento{id=%d, nome='%s', tipo='%s', duracao=%dmin, custo=%.2f}",
                idProcedimento, nome, tipo, duracaoMin, custo);
    }

    public int getIdProcedimento() {
        return idProcedimento;
    }

    public void setIdProcedimento(int v) {
        idProcedimento = v;
    }

    public int getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(int v) {
        idConsulta = v;
    }

    public int getDuracaoMin() {
        return duracaoMin;
    }

    public void setDuracaoMin(int v) {
        duracaoMin = v;
    }

    public String getCroDentista() {
        return croDentista;
    }

    public void setCroDentista(String v) {
        croDentista = v;
    }

    public String getCpfDentista() {
        return cpfDentista;
    }

    public void setCpfDentista(String v) {
        cpfDentista = v;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String v) {
        nome = v;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String v) {
        tipo = v;
    }

    public String getOrientacao() {
        return orientacao;
    }

    public void setOrientacao(String v) {
        orientacao = v;
    }

    public double getCusto() {
        return custo;
    }

    public void setCusto(double v) {
        custo = v;
    }
}
