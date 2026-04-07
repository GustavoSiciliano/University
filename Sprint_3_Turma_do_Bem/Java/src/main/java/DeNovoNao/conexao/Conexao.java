package DeNovoNao.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Classe responsável por fazer a conexão com o banco de dados
public class Conexao {

    // Dados de conexão com o banco Oracle
    private static final String URL  = "jdbc:oracle:thin:@SEU_SERVIDOR:SUA_PORTA:SEU_BANCO";
    private static final String USER = "SEU_USUARIO";
    private static final String PASS = "SUA_SENHA";

    // Metodo que realiza a conexão com o banco
    public static Connection conectar() {

        try {
            Class.forName("oracle.jdbc.OracleDriver");
            return DriverManager.getConnection(URL, USER, PASS);

        } catch (ClassNotFoundException e)
        {
            System.out.println("Driver Oracle nao encontrado: " + e.getMessage());

        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco: " + e.getMessage());
        }

        return null;
    }
}