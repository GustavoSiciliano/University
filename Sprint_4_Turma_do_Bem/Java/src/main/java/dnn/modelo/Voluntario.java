package dnn.modelo;

public class Voluntario extends Pessoa {

    private int idVoluntario;
    private String area, disponibilidade;

    public Voluntario() {
    }

    public Voluntario(String cpf, String nome, String telefone,
                      String email, String area, String disponibilidade) {
        super(cpf, nome, null, telefone, email, null, null, null, null, null, null);
        this.area = area;
        this.disponibilidade = disponibilidade;
    }

    // Área de atuação deve ser informada
    public boolean areaValida() {
        return area != null && !area.trim().isEmpty();
    }

    @Override
    public String toString() {
        return String.format("Voluntario{id=%d, nome='%s', area='%s'}", idVoluntario, getNome(), area);
    }

    public int getIdVoluntario() {
        return idVoluntario;
    }

    public void setIdVoluntario(int v) {
        idVoluntario = v;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String v) {
        area = v;
    }

    public String getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(String v) {
        disponibilidade = v;
    }
}
