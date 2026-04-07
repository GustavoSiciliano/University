package DeNovoNao.modelo;

// Classe Dentista herda de Pessoa
public class Dentista extends Pessoa {

    // Atributos específicos do dentista
    private String cro;
    private String especialidade;
    private String disponibilidade;

    // Construtor vazio
    public Dentista() {
    }

    // Construtor completo
    public Dentista(String nome, String cpf, String telefone, String email,
                    String dataNasc, String cro, String especialidade, String disponibilidade) {
        super(nome, cpf, telefone, email, dataNasc);

        this.cro = cro;
        this.especialidade = especialidade;
        this.disponibilidade = disponibilidade;
    }

    // Metodo para validar o CRO
    // O CRO não pode ser vazio
    public boolean validarCro() {
        return cro != null && !cro.trim().isEmpty();
    }

    // Metodo que verifica se o dentista pode atender
    // Precisa ter disponibilidade e cadastro válido
    public boolean podeAtender() {

        return disponibilidade != null && !disponibilidade.isEmpty()
                && cadastroValido();
    }

    // Sobrescreve o metodo da classe Pessoa
    // Aqui adicionamos a validação do CRO junto com as validações da Pessoa
    @Override
    public boolean cadastroValido() {

        return super.cadastroValido() && validarCro();
    }

    // Getters e Setters

    public String getCro() {
        return cro;
    }

    public void setCro(String cro) {
        this.cro = cro;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(String d) {
        this.disponibilidade = d;
    }
}