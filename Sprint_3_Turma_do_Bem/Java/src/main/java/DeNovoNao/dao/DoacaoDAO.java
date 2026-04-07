package DeNovoNao.dao;

import DeNovoNao.conexao.Conexao;
import DeNovoNao.modelo.Doacao;

import java.sql.*;
import java.util.Scanner;

// Classe responsável pelas operações da tabela DOACAO
public class DoacaoDAO {

    private Scanner sc;

    // Recebe o scanner vindo do menu
    public DoacaoDAO(Scanner sc) {
        this.sc = sc;
    }

    // Metodo para cadastrar doação
    public void cadastrar() {

        // Entrada de dados
        System.out.print("ID da doacao: ");
        int id = Integer.parseInt(sc.nextLine());

        System.out.print("ID do doador: ");
        int idDoador = Integer.parseInt(sc.nextLine());

        System.out.print("Valor (> 0): ");
        double valor = Double.parseDouble(sc.nextLine());

        System.out.print("Data (AAAA-MM-DD): ");
        String data = sc.nextLine();

        System.out.print("Tipo (Enter para pular): ");
        String tipo = sc.nextLine();

        System.out.print("Destino (Enter para pular): ");
        String destino = sc.nextLine();

        DoadorDAO doadorDAO = new DoadorDAO(sc);
        if (!doadorDAO.existe(idDoador)) {
            System.out.println("Doador nao encontrado. Cadastre o doador primeiro.");
            return;
        }

        Doacao d = new Doacao(id, idDoador, valor, data);

        if (!d.validarDoacao()) {
            System.out.println("Dados invalidos. Valor deve ser > 0 e data e obrigatoria.");
            return;
        }

        // SQL de inserção
        String sql = "INSERT INTO TDB_DOACAO (ID_DOACAO, VALOR, DATA, TIPO, DESTINO, ID_DOADOR) VALUES (?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.setDouble(2, valor);
            stmt.setString(3, data);
            stmt.setString(4, tipo.isEmpty() ? null : tipo);
            stmt.setString(5, destino.isEmpty() ? null : destino);
            stmt.setInt(6, idDoador);

            stmt.executeUpdate();

            System.out.println("Doacao cadastrada com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao cadastrar doacao: " + e.getMessage());
        }
    }

    // Metodo para listar as doações
    public void listar() {

        // Faz join com doador para mostrar o nome
        String sql = "SELECT d.ID_DOACAO, d.VALOR, d.DATA, d.TIPO, d.DESTINO, do.NOME AS NOME_DOADOR " +
                "FROM TDB_DOACAO d JOIN TDB_DOADOR do ON d.ID_DOADOR = do.ID_DOADOR";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n--- DOACOES ---");
            boolean achou = false;

            // Percorre os resultados
            while (rs.next()) {
                achou = true;

                System.out.println("ID: " + rs.getInt("ID_DOACAO"));
                System.out.println("Doador: " + rs.getString("NOME_DOADOR"));
                System.out.println("Valor: R$ " + rs.getDouble("VALOR"));
                System.out.println("Data: " + rs.getDate("DATA"));
                System.out.println("Tipo: " + rs.getString("TIPO"));
                System.out.println("Destino: " + rs.getString("DESTINO"));
                System.out.println("- - - - - - - - - - - - - -");
            }

            if (!achou) System.out.println("Nenhuma doacao cadastrada.");

        } catch (Exception e) {
            System.out.println("Erro ao listar doacoes: " + e.getMessage());
        }
    }

    // Metodo para excluir uma doação
    public void excluir() {

        System.out.print("ID da doacao a excluir: ");
        int id = Integer.parseInt(sc.nextLine());

        String sql = "DELETE FROM TDB_DOACAO WHERE ID_DOACAO = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int linhas = stmt.executeUpdate();

            System.out.println(linhas > 0 ? "Doacao excluida!" : "Doacao nao encontrada.");

        } catch (Exception e) {
            System.out.println("Erro ao excluir doacao: " + e.getMessage());
        }
    }

    // Metodo auxiliar
    public boolean existe(int id) {

        String sql = "SELECT 1 FROM TDB_DOACAO WHERE ID_DOACAO = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            return stmt.executeQuery().next();

        } catch (Exception e) {
            return false;
        }
    }
}