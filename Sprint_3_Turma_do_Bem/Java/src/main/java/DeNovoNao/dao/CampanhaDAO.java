package DeNovoNao.dao;

import DeNovoNao.conexao.Conexao;
import DeNovoNao.modelo.Campanha;

import java.sql.*;
import java.util.Scanner;

// Classe responsável por operações no banco da tabela CAMPANHA
public class CampanhaDAO {

    private Scanner sc;

    // Recebe o scanner (usado no menu)
    public CampanhaDAO(Scanner sc) {
        this.sc = sc;
    }

    // Metodo para cadastrar campanha
    public void cadastrar() {

        System.out.print("ID da campanha: ");
        int id = Integer.parseInt(sc.nextLine());

        System.out.print("Nome: ");
        String nome = sc.nextLine();

        System.out.print("Objetivo (Enter para pular): ");
        String objetivo = sc.nextLine();

        System.out.print("Data de inicio (AAAA-MM-DD): ");
        String dataInicio = sc.nextLine();

        System.out.print("Data de fim (AAAA-MM-DD, Enter para pular): ");
        String dataFim = sc.nextLine();

        System.out.print("ID da doacao vinculada (0 para pular): ");
        int idDoacao = Integer.parseInt(sc.nextLine());

        // Verifica se a doação existe (se foi informada)
        if (idDoacao > 0) {
            DoacaoDAO doacaoDAO = new DoacaoDAO(sc);
            if (!doacaoDAO.existe(idDoacao)) {
                System.out.println("Doacao nao encontrada.");
                return;
            }
        }

        Campanha c = new Campanha(id, nome, objetivo,
                dataInicio, dataFim.isEmpty() ? null : dataFim, idDoacao);

        // Valida os dados antes de salvar
        if (!c.cadastroValido()) {
            System.out.println("Dados invalidos. Nome e data de inicio sao obrigatorios.");
            return;
        }

        // SQL de inserção
        String sql = "INSERT INTO TDB_CAMPANHA (ID_CAMPANHA, NOME, OBJETIVO, DATA_INICIO, DATA_FIM, ID_DOACAO) " +
                "VALUES (?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), TO_DATE(?, 'YYYY-MM-DD'), ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define os valores no SQL
            stmt.setInt(1, id);
            stmt.setString(2, nome);
            stmt.setString(3, objetivo.isEmpty() ? null : objetivo);
            stmt.setString(4, dataInicio);
            stmt.setString(5, dataFim.isEmpty() ? null : dataFim);

            if (idDoacao > 0) stmt.setInt(6, idDoacao);
            else stmt.setNull(6, Types.INTEGER);

            stmt.executeUpdate();

            System.out.println("Campanha cadastrada com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao cadastrar campanha: " + e.getMessage());
        }
    }

    // Metodo para listar campanhas
    public void listar() {

        String sql = "SELECT ID_CAMPANHA, NOME, OBJETIVO, DATA_INICIO, DATA_FIM, ID_DOACAO FROM TDB_CAMPANHA";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n--- CAMPANHAS ---");

            boolean achou = false;

            // Percorre os resultados
            while (rs.next()) {
                achou = true;

                System.out.println("ID: " + rs.getInt("ID_CAMPANHA"));
                System.out.println("Nome: " + rs.getString("NOME"));
                System.out.println("Objetivo: " + rs.getString("OBJETIVO"));
                System.out.println("Inicio: " + rs.getDate("DATA_INICIO"));
                System.out.println("Fim: " + rs.getDate("DATA_FIM"));
                System.out.println("ID Doacao: " + rs.getString("ID_DOACAO"));
                System.out.println("- - - - - - - - - - - - - -");
            }

            if (!achou) System.out.println("Nenhuma campanha cadastrada.");

        } catch (Exception e) {
            System.out.println("Erro ao listar campanhas: " + e.getMessage());
        }
    }

    // Metodo para atualizar campanha
    public void atualizar() {

        System.out.print("ID da campanha a atualizar: ");
        int id = Integer.parseInt(sc.nextLine());

        System.out.print("Novo objetivo (Enter para pular): ");
        String objetivo = sc.nextLine();

        System.out.print("Nova data de fim (AAAA-MM-DD, Enter para pular): ");
        String dataFim = sc.nextLine();

        String sql = "UPDATE TDB_CAMPANHA SET OBJETIVO = ?, DATA_FIM = TO_DATE(?, 'YYYY-MM-DD') WHERE ID_CAMPANHA = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, objetivo.isEmpty() ? null : objetivo);
            stmt.setString(2, dataFim.isEmpty() ? null : dataFim);
            stmt.setInt(3, id);

            int linhas = stmt.executeUpdate();

            System.out.println(linhas > 0 ? "Campanha atualizada!" : "Campanha nao encontrada.");

        } catch (Exception e) {
            System.out.println("Erro ao atualizar campanha: " + e.getMessage());
        }
    }

    // Metodo para excluir campanha
    public void excluir() {

        System.out.print("ID da campanha a excluir: ");
        int id = Integer.parseInt(sc.nextLine());

        String sql = "DELETE FROM TDB_CAMPANHA WHERE ID_CAMPANHA = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int linhas = stmt.executeUpdate();

            System.out.println(linhas > 0 ? "Campanha excluida!" : "Campanha nao encontrada.");

        } catch (Exception e) {
            System.out.println("Erro ao excluir campanha: " + e.getMessage());
        }
    }
}