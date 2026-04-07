package DeNovoNao.modelo;

public class Campanha {

    // Atributos da campanha
    private int idCampanha;
    private String nome;
    private String objetivo;
    private String dataInicio;
    private String dataFim;
    private int idDoacao;

    // Construtor vazio
    public Campanha() {
    }

    // Construtor completo (já cria a campanha com todos os dados)
    public Campanha(int idCampanha, String nome, String objetivo,
                    String dataInicio, String dataFim, int idDoacao) {
        this.idCampanha = idCampanha;
        this.nome = nome;
        this.objetivo = objetivo;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.idDoacao = idDoacao;
    }

    // Verifica se as datas estão corretas
    // A regra é: dataFim deve ser maior ou igual à dataInicio
    public boolean datasValidas() {
        if (dataFim == null || dataFim.trim().isEmpty()) return true;
        return dataFim.compareTo(dataInicio) >= 0;
    }

    // Verifica se os dados principais da campanha estão corretos
    // Nome e dataInicio são obrigatórios
    public boolean cadastroValido() {

        return nome != null && !nome.trim().isEmpty()
                && dataInicio != null && !dataInicio.trim().isEmpty()
                && datasValidas();
    }

    // Getters e Setters

    public int getIdCampanha() {
        return idCampanha;
    }

    public void setIdCampanha(int idCampanha) {
        this.idCampanha = idCampanha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public int getIdDoacao() {
        return idDoacao;
    }

    public void setIdDoacao(int idDoacao) {
        this.idDoacao = idDoacao;
    }
}