package dnn.modelo;

public class Prontuario {

    private int idProntuario, idConsulta;
    private String descricao, observacoes, dtRegistro, nomePaciente, nomeDentista;

    public Prontuario() {
    }

    public Prontuario(int idConsulta, String descricao, String observacoes) {
        this.idConsulta = idConsulta;
        this.descricao = descricao;
        this.observacoes = observacoes;
    }

    // Descrição obrigatória e não vazia
    public boolean descricaoValida() {
        return descricao != null && !descricao.trim().isEmpty();
    }

    // ID de consulta deve ser positivo
    public boolean consultaValida() {
        return idConsulta > 0;
    }

    @Override
    public String toString() {
        return String.format("Prontuario{id=%d, consulta=%d, paciente='%s', data='%s'}",
                idProntuario, idConsulta, nomePaciente, dtRegistro);
    }

    public int getIdProntuario() {
        return idProntuario;
    }

    public void setIdProntuario(int v) {
        idProntuario = v;
    }

    public int getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(int v) {
        idConsulta = v;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String v) {
        descricao = v;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String v) {
        observacoes = v;
    }

    public String getDtRegistro() {
        return dtRegistro;
    }

    public void setDtRegistro(String v) {
        dtRegistro = v;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String v) {
        nomePaciente = v;
    }

    public String getNomeDentista() {
        return nomeDentista;
    }

    public void setNomeDentista(String v) {
        nomeDentista = v;
    }
}
