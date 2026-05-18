package dnn.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Abre conexão com o Oracle da FIAP
public class Conexao {
    private static final String URL = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL";
    private static final String USER = "RM568419";
    private static final String PASS = "250204";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
