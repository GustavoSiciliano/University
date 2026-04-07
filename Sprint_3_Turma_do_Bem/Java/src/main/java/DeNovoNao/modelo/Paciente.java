package DeNovoNao.modelo;

// Classe Paciente herda de Pessoa
public class Paciente extends Pessoa {

    // Atributos específicos do paciente
    private int idPaciente;
    private String endereco;
    private String convenio;

    // Construtor vazio
    public Paciente() {
    }

    // Construtor completo
    public Paciente(int idPaciente, String nome, String cpf, String telefone,
                    String email, String dataNasc, String endereco, String convenio) {
        super(nome, cpf, telefone, email, dataNasc);

        this.idPaciente = idPaciente;
        this.endereco = endereco;
        this.convenio = convenio;
    }

    // Metodo que verifica se o paciente pode agendar consulta
    // Só pode se o cadastro estiver válido
    public boolean podeAgendarConsulta() {

        return cadastroValido();
    }

    // Getters e Setters

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }
}