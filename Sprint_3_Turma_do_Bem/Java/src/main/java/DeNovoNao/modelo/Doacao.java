package DeNovoNao.modelo;

// Classe que representa uma doação
public class Doacao {

    // Atributos da doação
    private int id;
    private int idDoador;
    private double valor;
    private String data;

    // Construtor vazio
    public Doacao() {
    }

    // Construtor completo
    public Doacao(int id, int idDoador, double valor, String data) {
        this.id = id;
        this.idDoador = idDoador;
        this.valor = valor;
        this.data = data;
    }

    // Metodo para validar a doação
    public boolean validarDoacao() {

        return idDoador > 0
                && valor > 0
                && data != null
                && !data.isEmpty();
    }

    // Metodo que aplica um bônus no valor da doação
    // Ex: se for 10%, aumenta o valor em 10%
    public double aplicarBonus(double percentual) {

        if (percentual > 0) {
            return valor + (valor * percentual / 100);
        }
        return valor;
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdDoador() {
        return idDoador;
    }

    public void setIdDoador(int idDoador) {
        this.idDoador = idDoador;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}