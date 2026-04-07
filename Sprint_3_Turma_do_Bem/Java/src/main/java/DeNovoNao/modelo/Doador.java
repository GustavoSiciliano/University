package DeNovoNao.modelo;

// Classe que representa um doador
public class Doador {

    // Atributos do doador
    private int idDoador;
    private String nome;
    private String tipo;
    private String cpf;
    private String email;
    private String telefone;

    // Construtor vazio
    public Doador() {
    }

    // Construtor completo
    public Doador(int idDoador, String nome, String tipo, String cpf,
                  String email, String telefone) {

        this.idDoador = idDoador;
        this.nome = nome;
        this.tipo = tipo;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
    }

    // Metodo para validar o tipo do doador
    // Só aceita "PF" ou "PJ"
    public boolean tipoValido() {
        return "PF".equals(tipo) || "PJ".equals(tipo);
    }

    // Metodo que verifica se o cadastro está válido
    // Nome não pode ser vazio e o tipo deve ser correto
    public boolean cadastroValido() {

        return nome != null && !nome.trim().isEmpty()
                && tipoValido();
    }

    // Getters e Setters

    public int getIdDoador() {
        return idDoador;
    }

    public void setIdDoador(int idDoador) {
        this.idDoador = idDoador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}