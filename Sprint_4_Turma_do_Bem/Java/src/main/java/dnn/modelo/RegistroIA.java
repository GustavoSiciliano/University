package dnn.modelo;

public class RegistroIA {

    private int idRegistro;
    private Integer idConsulta; // null para predições de arrecadação
    private String tipoPredicao, entradaJson, classePrevista, risco, modeloVersao, acerto, dtPredicao;
    private double probResultado;

    public RegistroIA() {
    }

    public RegistroIA(Integer idConsulta, String tipoPredicao, String entradaJson,
                      double probResultado, String classePrevista, String risco, String modeloVersao) {
        this.idConsulta = idConsulta;
        this.tipoPredicao = tipoPredicao;
        this.entradaJson = entradaJson;
        this.probResultado = probResultado;
        this.classePrevista = classePrevista;
        this.risco = risco;
        this.modeloVersao = modeloVersao != null ? modeloVersao : "v1.0";
    }

    public boolean isFalta() {
        return "FALTA".equals(tipoPredicao);
    }

    public boolean isArrecadacao() {
        return "ARRECADACAO".equals(tipoPredicao);
    }

    public boolean isRiscoAlto() {
        return "ALTO".equals(risco);
    }

    public boolean isRiscoMedio() {
        return "MEDIO".equals(risco);
    }

    public boolean isRiscoBaixo() {
        return "BAIXO".equals(risco);
    }

    public boolean acertou() {
        return "S".equals(acerto);
    }

    // Tipo deve ser FALTA ou ARRECADACAO
    public boolean tipoValido() {
        return isFalta() || isArrecadacao();
    }

    // Probabilidade entre 0 e 1
    public boolean probValida() {
        return probResultado >= 0 && probResultado <= 1;
    }

    @Override
    public String toString() {
        return String.format("RegistroIA{id=%d, tipo='%s', risco='%s', prob=%.2f, classe='%s'}",
                idRegistro, tipoPredicao, risco, probResultado, classePrevista);
    }

    public int getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(int v) {
        idRegistro = v;
    }

    public Integer getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Integer v) {
        idConsulta = v;
    }

    public String getTipoPredicao() {
        return tipoPredicao;
    }

    public void setTipoPredicao(String v) {
        tipoPredicao = v;
    }

    public String getEntradaJson() {
        return entradaJson;
    }

    public void setEntradaJson(String v) {
        entradaJson = v;
    }

    public String getClassePrevista() {
        return classePrevista;
    }

    public void setClassePrevista(String v) {
        classePrevista = v;
    }

    public String getRisco() {
        return risco;
    }

    public void setRisco(String v) {
        risco = v;
    }

    public String getModeloVersao() {
        return modeloVersao;
    }

    public void setModeloVersao(String v) {
        modeloVersao = v;
    }

    public String getAcerto() {
        return acerto;
    }

    public void setAcerto(String v) {
        acerto = v;
    }

    public String getDtPredicao() {
        return dtPredicao;
    }

    public void setDtPredicao(String v) {
        dtPredicao = v;
    }

    public double getProbResultado() {
        return probResultado;
    }

    public void setProbResultado(double v) {
        probResultado = v;
    }
}
