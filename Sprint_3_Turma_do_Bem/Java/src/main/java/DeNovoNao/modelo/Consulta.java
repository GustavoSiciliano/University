package DeNovoNao.modelo;

public class Consulta {

    // Atributos principais da consulta
    private int id;
    private Paciente paciente;
    private Dentista dentista;
    private String data;
    private String status;

    // Construtor vazio
    public Consulta() {
    }

    // Construtor completo
    public Consulta(int id, Paciente paciente, Dentista dentista, String data, String status) {
        this.id = id;
        this.paciente = paciente;
        this.dentista = dentista;
        this.data = data;
        this.status = status;
    }

    // Metodo para agendar consulta
    // Só agenda se tiver paciente, dentista e data
    public boolean agendarConsulta() {
        if (paciente == null || dentista == null)
            return false;

        if (data == null || data.isEmpty())
            return false;

        this.status = "AGENDADA";
        return true;
    }

    // Metodo para cancelar consulta
    // Não pode cancelar se já foi realizada
    public boolean cancelarConsulta() {

        if ("REALIZADA".equals(this.status))
            return false;

        this.status = "CANCELADA";
        return true;
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Dentista getDentista() {
        return dentista;
    }

    public void setDentista(Dentista dentista) {
        this.dentista = dentista;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}