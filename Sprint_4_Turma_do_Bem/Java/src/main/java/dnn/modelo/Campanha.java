package dnn.modelo;

public class Campanha {

    private int idCampanha;
    private String nome, descricao, dataInicio, dataFim, ativo;
    private double metaValor, totalArrecadado;

    public Campanha() {
    }

    public Campanha(String nome, String descricao, String dataInicio,
                    String dataFim, double metaValor) {
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.metaValor = metaValor;
        this.totalArrecadado = 0;
        this.ativo = "S";
    }

    // Percentual da meta atingido
    public double percentualMeta() {
        return metaValor > 0 ? (totalArrecadado / metaValor) * 100 : 0;
    }

    // Meta atingida quando arrecadado >= meta
    public boolean metaAtingida() {
        return totalArrecadado >= metaValor;
    }

    // Campanha ativa
    public boolean isAtiva() {
        return "S".equals(ativo);
    }

    // Nome deve ter pelo menos 3 caracteres
    public boolean nomeValido() {
        return nome != null && nome.trim().length() >= 3;
    }

    // Meta deve ser positiva
    public boolean metaValida() {
        return metaValor > 0;
    }

    @Override
    public String toString() {
        return String.format("Campanha{id=%d, nome='%s', meta=%.2f, arrecadado=%.2f (%.1f%%)}",
                idCampanha, nome, metaValor, totalArrecadado, percentualMeta());
    }

    public int getIdCampanha() {
        return idCampanha;
    }

    public void setIdCampanha(int v) {
        idCampanha = v;
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

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String v) {
        dataInicio = v;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String v) {
        dataFim = v;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String v) {
        ativo = v;
    }

    public double getMetaValor() {
        return metaValor;
    }

    public void setMetaValor(double v) {
        metaValor = v;
    }

    public double getTotalArrecadado() {
        return totalArrecadado;
    }

    public void setTotalArrecadado(double v) {
        totalArrecadado = v;
    }
}
