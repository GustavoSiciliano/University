package dnn.modelo;

public class Dentista extends Pessoa {

    private String cro, especialidade, disponibilidade;

    public Dentista() {
    }

    public Dentista(String cro, String cpf, String nome, String telefone,
                    String email, String especialidade, String disponibilidade) {
        super(cpf, nome, null, telefone, email, null, null, null, null, null, null);
        this.cro = cro;
        this.especialidade = especialidade;
        this.disponibilidade = disponibilidade;
    }

    // CRO não pode ser vazio
    public boolean croValido() {
        return cro != null && !cro.trim().isEmpty();
    }

    // Dentista está disponível se disponibilidade foi informada
    public boolean temDisponibilidade() {
        return disponibilidade != null && !disponibilidade.isBlank();
    }

    @Override
    public String toString() {
        return String.format("Dentista{cro='%s', nome='%s', especialidade='%s'}", cro, getNome(), especialidade);
    }

    public String getCro() {
        return cro;
    }

    public void setCro(String v) {
        cro = v;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String v) {
        especialidade = v;
    }

    public String getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(String v) {
        disponibilidade = v;
    }
}
