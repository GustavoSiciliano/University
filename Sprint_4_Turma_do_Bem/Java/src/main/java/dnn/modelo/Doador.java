package dnn.modelo;

public class Doador extends Pessoa {

    private int idDoador;
    private String tipoDoador; // PF ou PJ

    public Doador() {
    }

    public Doador(String cpf, String nome, String telefone, String email, String tipoDoador) {
        super(cpf, nome, null, telefone, email, null, null, null, null, null, null);
        this.tipoDoador = tipoDoador;
    }

    // Tipo deve ser PF ou PJ
    public boolean tipoValido() {
        return "PF".equals(tipoDoador) || "PJ".equals(tipoDoador);
    }

    public boolean isPessoaFisica() {
        return "PF".equals(tipoDoador);
    }

    public boolean isPessoaJuridica() {
        return "PJ".equals(tipoDoador);
    }

    @Override
    public String toString() {
        return String.format("Doador{id=%d, nome='%s', tipo='%s'}", idDoador, getNome(), tipoDoador);
    }

    public int getIdDoador() {
        return idDoador;
    }

    public void setIdDoador(int v) {
        idDoador = v;
    }

    public String getTipoDoador() {
        return tipoDoador;
    }

    public void setTipoDoador(String v) {
        tipoDoador = v;
    }
}
