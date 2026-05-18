package dnn.boo;

// Exceção personalizada — carrega o código HTTP junto com a mensagem
public class ApiExcecao extends RuntimeException {
    private final int status;

    public ApiExcecao(String mensagem, int status) {
        super(mensagem);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
