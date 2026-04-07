package DeNovoNao.modelo;

// Classe base Pessoa
public class Pessoa {

    // Atributos básicos de uma pessoa
    private String cpf;
    private String nome;
    private String telefone;
    private String email;
    private String dataNasc;

    // Construtor vazio
    public Pessoa() {
    }

    // Construtor completo
    public Pessoa(String nome, String cpf, String telefone, String email, String dataNasc) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.dataNasc = dataNasc;
    }

    // Metodo para validar CPF
    // Remove tudo que não for número e verifica se tem 11 dígitos
    public boolean validarCpf() {
        if (cpf == null)
            return false;

        // Remove pontos e traços
        String soDigitos = cpf.replaceAll("[^0-9]", "");

        // CPF válido deve ter 11 números
        return soDigitos.length() == 11;
    }

    // Metodo para validar data de nascimento
    // Espera formato: YYYY-MM-DD
    public boolean validarDataNasc() {
        return dataNasc != null
                && dataNasc.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    // Metodo para validar nome
    // Nome não pode ser vazio
    public boolean validarNome() {
        return nome != null && !nome.trim().isEmpty();
    }

    // Metodo geral que verifica se o cadastro está válido
    // Usa as validações acima
    public boolean cadastroValido() {
        return validarNome()
                && validarCpf()
                && validarDataNasc();
    }

    // Getters e Setters

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(String dataNasc) {
        this.dataNasc = dataNasc;
    }
}