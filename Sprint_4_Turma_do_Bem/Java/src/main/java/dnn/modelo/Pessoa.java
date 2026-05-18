package dnn.modelo;

// Classe base — Dentista, Voluntario, Doador e Usuario herdam dela
public class Pessoa {

    private String cpf, nome, dataNasc, telefone, email;
    private String cep, logradouro, numero, bairro, cidade, uf, ativo;

    // Construtor vazio (necessário para o Jackson deserializar JSON)
    public Pessoa() {
    }

    // Construtor completo
    public Pessoa(String cpf, String nome, String dataNasc, String telefone,
                  String email, String cep, String logradouro, String numero,
                  String bairro, String cidade, String uf) {
        this.cpf = cpf;
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.telefone = telefone;
        this.email = email;
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.ativo = "S";
    }

    // Valida CPF: 11 dígitos numéricos
    public boolean cpfValido() {
        if (cpf == null) return false;
        String s = cpf.replaceAll("[^0-9]", "");
        return s.length() == 11;
    }

    // Valida nome: não vazio e mínimo 3 caracteres
    public boolean nomeValido() {
        return nome != null && nome.trim().length() >= 3;
    }

    // Valida email: contém @ e domínio
    public boolean emailValido() {
        if (email == null || email.isBlank()) return true; // opcional
        return email.contains("@") && email.contains(".");
    }

    // Valida telefone: entre 8 e 15 caracteres numéricos
    public boolean telefoneValido() {
        if (telefone == null || telefone.isBlank()) return true; // opcional
        String s = telefone.replaceAll("[^0-9]", "");
        return s.length() >= 8 && s.length() <= 11;
    }

    // Verifica se está ativo
    public boolean isAtivo() {
        return "S".equals(ativo);
    }

    @Override
    public String toString() {
        return String.format("Pessoa{cpf='%s', nome='%s', cidade='%s/%s'}", cpf, nome, cidade, uf);
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String v) {
        cpf = v;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String v) {
        nome = v;
    }

    public String getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(String v) {
        dataNasc = v;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String v) {
        telefone = v;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String v) {
        email = v;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String v) {
        cep = v;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String v) {
        logradouro = v;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String v) {
        numero = v;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String v) {
        bairro = v;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String v) {
        cidade = v;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String v) {
        uf = v;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String v) {
        ativo = v;
    }
}
