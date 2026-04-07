package DeNovoNao.dao;

import DeNovoNao.conexao.Conexao;

import java.sql.*;
import java.util.Scanner;

// Classe responsável pelas operações da tabela PRONTUARIO
public class ProntuarioDAO {

    private Scanner sc;

    // Recebe o scanner do menu
    public ProntuarioDAO(Scanner sc) {
        this.sc = sc;
    }

    // Metodo para cadastrar prontuario
    public void cadastrar() {

        // Entrada de dados
        System.out.print("ID do prontuario: ");
        int id = Integer.parseInt(sc.nextLine());

        System.out.print("ID do paciente: ");
        int idPaciente = Integer.parseInt(sc.nextLine());

        System.out.print("CPF do paciente: ");
        String cpf = sc.nextLine();

        System.out.print("Data (AAAA-MM-DD): ");
        String data = sc.nextLine();

        System.out.print("Descricao (Enter para pular): ");
        String descricao = sc.nextLine();

        System.out.print("Observacoes (Enter para pular): ");
        String obs = sc.nextLine();

        if (data.isEmpty()) {
            System.out.println("Data e obrigatoria.");
            return;
        }

        PacienteDAO pacienteDAO = new PacienteDAO(sc);
        if (!pacienteDAO.existe(idPaciente, cpf)) {
            System.out.println("Paciente nao encontrado.");
            return;
        }

        // SQL de inserção
        String sql = "INSERT INTO TDB_PRONTUARIO (ID_PRONTUARIO, DESCRICAO, DATA, OBSERVACOES, ID_PACIENTE, CPF_PACIENTE) " +
                "VALUES (?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.setString(2, descricao.isEmpty() ? null : descricao);
            stmt.setString(3, data);
            stmt.setString(4, obs.isEmpty() ? null : obs);
            stmt.setInt(5, idPaciente);
            stmt.setString(6, cpf);

            stmt.executeUpdate();

            System.out.println("Prontuario cadastrado com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao cadastrar prontuario: " + e.getMessage());
        }
    }

    // Metodo para listar prontuarios
    public void listar() {

        // JOIN para trazer nome do paciente
        String sql = "SELECT pr.ID_PRONTUARIO, pr.DESCRICAO, pr.DATA, pr.ULTIMA_ALT, pr.OBSERVACOES, " +
                "p.NOME, pa.CPF " +
                "FROM TDB_PRONTUARIO pr " +
                "JOIN TDB_PACIENTE pa ON pr.ID_PACIENTE = pa.ID_PACIENTE " +
                "JOIN TDB_PESSOA p ON pa.CPF = p.CPF";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n--- PRONTUARIOS ---");
            boolean achou = false;

            // Percorre resultados
            while (rs.next()) {
                achou = true;

                System.out.println("ID: " + rs.getInt("ID_PRONTUARIO"));
                System.out.println("Paciente: " + rs.getString("NOME"));
                System.out.println("Data: " + rs.getDate("DATA"));
                System.out.println("Ultima alt.: " + rs.getDate("ULTIMA_ALT"));
                System.out.println("Descricao: " + rs.getString("DESCRICAO"));
                System.out.println("Observacoes: " + rs.getString("OBSERVACOES"));
                System.out.println("- - - - - - - - - - - - - -");
            }

            if (!achou) System.out.println("Nenhum prontuario cadastrado.");

        } catch (Exception e) {
            System.out.println("Erro ao listar prontuarios: " + e.getMessage());
        }
    }

    // Metodo para atualizar prontuario
    public void atualizar() {

        System.out.print("ID do prontuario a atualizar: ");
        int id = Integer.parseInt(sc.nextLine());

        System.out.print("Nova descricao (Enter para pular): ");
        String descricao = sc.nextLine();

        System.out.print("Novas observacoes (Enter para pular): ");
        String obs = sc.nextLine();

        System.out.print("Data da alteracao (AAAA-MM-DD): ");
        String ultimaAlt = sc.nextLine();

        String sql = "UPDATE TDB_PRONTUARIO SET DESCRICAO = ?, OBSERVACOES = ?, ULTIMA_ALT = TO_DATE(?, 'YYYY-MM-DD') WHERE ID_PRONTUARIO = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, descricao.isEmpty() ? null : descricao);
            stmt.setString(2, obs.isEmpty() ? null : obs);
            stmt.setString(3, ultimaAlt.isEmpty() ? null : ultimaAlt);
            stmt.setInt(4, id);

            int linhas = stmt.executeUpdate();

            System.out.println(linhas > 0 ? "Prontuario atualizado!" : "Prontuario nao encontrado.");

        } catch (Exception e) {
            System.out.println("Erro ao atualizar prontuario: " + e.getMessage());
        }
    }

    // Metodo para excluir prontuario
    public void excluir() {

        System.out.print("ID do prontuario a excluir: ");
        int id = Integer.parseInt(sc.nextLine());

        String sql = "DELETE FROM TDB_PRONTUARIO WHERE ID_PRONTUARIO = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int linhas = stmt.executeUpdate();

            System.out.println(linhas > 0 ? "Prontuario excluido!" : "Prontuario nao encontrado.");

        } catch (Exception e) {
            System.out.println("Erro ao excluir prontuario: " + e.getMessage());
        }
    }
}