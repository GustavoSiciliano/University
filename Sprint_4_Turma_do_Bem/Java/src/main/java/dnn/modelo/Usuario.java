package dnn.modelo;

public class Usuario {

    private int idUsuario;
    private String cpf, login, senhaHash, perfil, ativo, nome;

    public Usuario() {
    }

    public Usuario(String cpf, String login, String senhaHash, String perfil) {
        this.cpf = cpf;
        this.login = login;
        this.senhaHash = senhaHash;
        this.perfil = perfil;
        this.ativo = "S";
    }

    public boolean isAtivo() {
        return "S".equals(ativo);
    }

    public boolean isAdmin() {
        return "ADMIN".equals(perfil);
    }

    public boolean isDentista() {
        return "DENTISTA".equals(perfil);
    }

    public boolean isVoluntario() {
        return "VOLUNTARIO".equals(perfil);
    }

    public boolean isGestor() {
        return "GESTOR".equals(perfil);
    }

    // Login: mínimo 5 caracteres
    public boolean loginValido() {
        return login != null && login.trim().length() >= 5;
    }

    // Perfil deve ser um dos valores permitidos
    public boolean perfilValido() {
        return perfil != null &&
                (perfil.equals("ADMIN") || perfil.equals("DENTISTA") ||
                        perfil.equals("VOLUNTARIO") || perfil.equals("GESTOR"));
    }

    @Override
    public String toString() {
        return String.format("Usuario{id=%d, login='%s', perfil='%s'}", idUsuario, login, perfil);
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int v) {
        idUsuario = v;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String v) {
        cpf = v;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String v) {
        login = v;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String v) {
        senhaHash = v;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String v) {
        perfil = v;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String v) {
        ativo = v;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String v) {
        nome = v;
    }
}
