package dnn.modelo;

public class Doacao {

    private int idDoacao, idDoador, idCampanha;
    private double valor;
    private String dataDoacao, formaPgto, observacoes, nomeDoador;

    public Doacao() {
    }

    public Doacao(int idDoador, int idCampanha, double valor, String formaPgto, String observacoes) {
        this.idDoador = idDoador;
        this.idCampanha = idCampanha;
        this.valor = valor;
        this.formaPgto = formaPgto;
        this.observacoes = observacoes;
    }

    // Valor deve ser positivo
    public boolean valorValido() {
        return valor > 0;
    }

    // Forma de pagamento válida
    public boolean formaPgtoValida() {
        return formaPgto != null &&
                (formaPgto.equals("PIX") || formaPgto.equals("BOLETO") ||
                        formaPgto.equals("CARTAO") || formaPgto.equals("TRANSFERENCIA") ||
                        formaPgto.equals("DINHEIRO"));
    }

    @Override
    public String toString() {
        return String.format("Doacao{id=%d, valor=%.2f, forma='%s', doador='%s'}",
                idDoacao, valor, formaPgto, nomeDoador);
    }

    public int getIdDoacao() {
        return idDoacao;
    }

    public void setIdDoacao(int v) {
        idDoacao = v;
    }

    public int getIdDoador() {
        return idDoador;
    }

    public void setIdDoador(int v) {
        idDoador = v;
    }

    public int getIdCampanha() {
        return idCampanha;
    }

    public void setIdCampanha(int v) {
        idCampanha = v;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double v) {
        valor = v;
    }

    public String getDataDoacao() {
        return dataDoacao;
    }

    public void setDataDoacao(String v) {
        dataDoacao = v;
    }

    public String getFormaPgto() {
        return formaPgto;
    }

    public void setFormaPgto(String v) {
        formaPgto = v;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String v) {
        observacoes = v;
    }

    public String getNomeDoador() {
        return nomeDoador;
    }

    public void setNomeDoador(String v) {
        nomeDoador = v;
    }
}
