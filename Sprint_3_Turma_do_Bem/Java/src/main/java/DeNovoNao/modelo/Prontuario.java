package DeNovoNao.modelo;

// Classe que representa o prontuário
public class Prontuario {

    // Atributos do prontuário
    private int idProntuario;
    private Paciente paciente;
    private String descricao;
    private String data;
    private String ultimaAlt;
    private String observacoes;

    // Construtor vazio
    public Prontuario() {
    }

    // Construtor completo
    public Prontuario(int idProntuario, Paciente paciente, String descricao,
                      String data, String ultimaAlt, String observacoes) {

        this.idProntuario = idProntuario;
        this.paciente = paciente;
        this.descricao = descricao;
        this.data = data;
        this.ultimaAlt = ultimaAlt;
        this.observacoes = observacoes;
    }

    // Metodo que verifica se o prontuário está válido
    // Precisa ter paciente e data no formato correto (YYYY-MM-DD)
    public boolean cadastroValido() {

        return paciente != null
                && data != null
                && data.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    // Metodo para atualizar o prontuário
    // Atualiza observações e data de alteração
    public boolean atualizarProntuario(String novaObservacao, String novaDataAlteracao) {

        // Não pode atualizar sem paciente
        if (paciente == null)
            return false;

        // Observação não pode ser vazia
        if (novaObservacao == null || novaObservacao.trim().isEmpty())
            return false;

        // Data precisa estar no formato correto
        if (novaDataAlteracao == null
                || !novaDataAlteracao.matches("\\d{4}-\\d{2}-\\d{2}"))
            return false;

        this.observacoes = novaObservacao;
        this.ultimaAlt = novaDataAlteracao;

        return true;
    }

    // Getters e Setters

    public int getIdProntuario() {
        return idProntuario;
    }

    public void setIdProntuario(int idProntuario) {
        this.idProntuario = idProntuario;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUltimaAlt() {
        return ultimaAlt;
    }

    public void setUltimaAlt(String ultimaAlt) {
        this.ultimaAlt = ultimaAlt;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}