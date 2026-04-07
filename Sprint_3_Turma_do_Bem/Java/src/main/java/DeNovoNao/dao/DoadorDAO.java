package DeNovoNao.dao;

import DeNovoNao.conexao.Conexao;

import java.sql.*;
import java.util.Scanner;

// Classe responsável pelas operações da tabela DOADOR
public class DoadorDAO {

    private Scanner sc;

    // Recebe o scanner vindo do menu
    public DoadorDAO(Scanner sc) {
        this.sc = sc;
    }

    // Metodo para cadastrar doador
    public void cadastrar() {

        // Entrada de dados
        System.out.print("ID do doador: ");
        int id = Integer.parseInt(sc.nextLine());

        System.out.print("Nome: ");
        String nome = sc.nextLine();

        System.out.print("Tipo (PF ou PJ): ");
        String tipo = sc.nextLine().toUpperCase();

        System.out.print("CPF (Enter para pular): ");
        String cpf = sc.nextLine();

        System.out.print("Email (Enter para pular): ");
        String email = sc.nextLine();

        System.out.print("Telefone (Enter para pular): ");
        String tel = sc.nextLine();

        if (nome.isEmpty() || (!tipo.equals("PF") && !tipo.equals("PJ"))) {
            System.out.println("Nome obrigatorio e tipo deve ser PF ou PJ.");
            return;
        }

        // SQL de inserção
        String sql = "INSERT INTO TDB_DOADOR (ID_DOADOR, NOME, TIPO, CPF, EMAIL, TELEFONE) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.setString(2, nome);
            stmt.setString(3, tipo);
            stmt.setString(4, cpf.isEmpty() ? null : cpf);
            stmt.setString(5, email.isEmpty() ? null : email);
            stmt.setString(6, tel.isEmpty() ? null : tel);

            stmt.executeUpdate();

            System.out.println("Doador cadastrado com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao cadastrar doador: " + e.getMessage());
        }
    }

    // Metodo para listar doadores
    public void listar() {

        String sql = "SELECT * FROM TDB_DOADOR";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n--- DOADORES ---");
            boolean achou = false;

            // Percorre os resultados
            while (rs.next()) {
                achou = true;

                System.out.println("ID: " + rs.getInt("ID_DOADOR"));
                System.out.println("Nome: " + rs.getString("NOME"));
                System.out.println("Tipo: " + rs.getString("TIPO"));
                System.out.println("CPF: " + rs.getString("CPF"));
                System.out.println("Email: " + rs.getString("EMAIL"));
                System.out.println("Telefone: " + rs.getString("TELEFONE"));
                System.out.println("- - - - - - - - - - - - - -");
            }

            if (!achou) System.out.println("Nenhum doador cadastrado.");

        } catch (Exception e) {
            System.out.println("Erro ao listar doadores: " + e.getMessage());
        }
    }

    // Metodo para atualizar dados do doador
    public void atualizar() {

        System.out.print("ID do doador a atualizar: ");
        int id = Integer.parseInt(sc.nextLine());

        System.out.print("Novo email (Enter para pular): ");
        String email = sc.nextLine();

        System.out.print("Novo telefone (Enter para pular): ");
        String tel = sc.nextLine();

        String sql = "UPDATE TDB_DOADOR SET EMAIL = ?, TELEFONE = ? WHERE ID_DOADOR = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.isEmpty() ? null : email);
            stmt.setString(2, tel.isEmpty() ? null : tel);
            stmt.setInt(3, id);

            int linhas = stmt.executeUpdate();

            System.out.println(linhas > 0 ? "Doador atualizado!" : "Doador nao encontrado.");

        } catch (Exception e) {
            System.out.println("Erro ao atualizar doador: " + e.getMessage());
        }
    }

    // Metodo para excluir doador
    public void excluir() {

        System.out.print("ID do doador a excluir: ");
        int id = Integer.parseInt(sc.nextLine());

        String sql = "DELETE FROM TDB_DOADOR WHERE ID_DOADOR = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int linhas = stmt.executeUpdate();

            System.out.println(linhas > 0 ? "Doador excluido!" : "Doador nao encontrado.");

        } catch (Exception e) {
            System.out.println("Erro ao excluir doador: " + e.getMessage());
        }
    }

    // Metodo auxiliar
    public boolean existe(int id) {

        String sql = "SELECT 1 FROM TDB_DOADOR WHERE ID_DOADOR = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            return stmt.executeQuery().next();

        } catch (Exception e) {
            return false;
        }
    }
}