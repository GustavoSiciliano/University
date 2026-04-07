package DeNovoNao.dao;

import DeNovoNao.conexao.Conexao;

import java.sql.*;
import java.util.Scanner;

// Classe responsável por operações da tabela CONSULTA
public class ConsultaDAO {

    private Scanner sc;

    // Recebe o scanner do menu
    public ConsultaDAO(Scanner sc) {
        this.sc = sc;
    }

    // Metodo para cadastrar consulta
    public void cadastrar() {

        System.out.print("ID da consulta: ");
        int id = Integer.parseInt(sc.nextLine());

        System.out.print("ID do paciente: ");
        int idPaciente = Integer.parseInt(sc.nextLine());

        System.out.print("CPF do paciente: ");
        String cpfPaciente = sc.nextLine();

        System.out.print("CRO do dentista: ");
        String croDentista = sc.nextLine();

        System.out.print("CPF do dentista: ");
        String cpfDentista = sc.nextLine();

        System.out.print("Data (AAAA-MM-DD): ");
        String data = sc.nextLine();

        System.out.print("Horario (Enter para pular): ");
        String horario = sc.nextLine();

        System.out.print("Status (AGENDADA / REALIZADA / CANCELADA / REMARCADA): ");
        String status = sc.nextLine().toUpperCase();

        System.out.print("Tipo (Enter para pular): ");
        String tipo = sc.nextLine();

        if (!status.matches("AGENDADA|REALIZADA|CANCELADA|REMARCADA")) {
            System.out.println("Status invalido.");
            return;
        }

        PacienteDAO pacienteDAO = new PacienteDAO(sc);
        if (!pacienteDAO.existe(idPaciente, cpfPaciente)) {
            System.out.println("Paciente nao encontrado.");
            return;
        }

        DentistaDAO dentistaDAO = new DentistaDAO(sc);
        if (!dentistaDAO.existe(croDentista, cpfDentista)) {
            System.out.println("Dentista nao encontrado.");
            return;
        }

        // SQL de inserção
        String sql = "INSERT INTO TDB_CONSULTA (ID_CONSULTA, DATA, HORARIO, STATUS, TIPO, ID_PACIENTE, CPF_PACIENTE, CRO_DENTISTA, CPF_DENTISTA) " +
                "VALUES (?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.setString(2, data);
            stmt.setString(3, horario.isEmpty() ? null : horario);
            stmt.setString(4, status);
            stmt.setString(5, tipo.isEmpty() ? null : tipo);
            stmt.setInt(6, idPaciente);
            stmt.setString(7, cpfPaciente);
            stmt.setString(8, croDentista);
            stmt.setString(9, cpfDentista);

            stmt.executeUpdate();

            System.out.println("Consulta cadastrada com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao cadastrar consulta: " + e.getMessage());
        }
    }

    // Metodo para listar consultas
    public void listar() {

        // SQL com JOIN para trazer nome do paciente e dentista
        String sql = "SELECT c.ID_CONSULTA, c.DATA, c.HORARIO, c.STATUS, c.TIPO, " +
                "pp.NOME AS NOME_PACIENTE, pd.NOME AS NOME_DENTISTA, c.CRO_DENTISTA " +
                "FROM TDB_CONSULTA c " +
                "JOIN TDB_PACIENTE pa ON c.ID_PACIENTE = pa.ID_PACIENTE " +
                "JOIN TDB_PESSOA pp ON pa.CPF = pp.CPF " +
                "JOIN TDB_DENTISTA d ON c.CRO_DENTISTA = d.CRO AND c.CPF_DENTISTA = d.CPF " +
                "JOIN TDB_PESSOA pd ON d.CPF = pd.CPF";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n--- CONSULTAS ---");

            boolean achou = false;

            // Percorre os resultados
            while (rs.next()) {
                achou = true;

                System.out.println("ID: " + rs.getInt("ID_CONSULTA"));
                System.out.println("Data: " + rs.getDate("DATA"));
                System.out.println("Horario: " + rs.getString("HORARIO"));
                System.out.println("Status: " + rs.getString("STATUS"));
                System.out.println("Tipo: " + rs.getString("TIPO"));
                System.out.println("Paciente: " + rs.getString("NOME_PACIENTE"));
                System.out.println("Dentista: " + rs.getString("NOME_DENTISTA"));
                System.out.println("- - - - - - - - - - - - - -");
            }

            if (!achou) System.out.println("Nenhuma consulta cadastrada.");

        } catch (Exception e) {
            System.out.println("Erro ao listar consultas: " + e.getMessage());
        }
    }

    // Metodo para atualizar consulta
    public void atualizar() {

        System.out.print("ID da consulta a atualizar: ");
        int id = Integer.parseInt(sc.nextLine());

        System.out.print("Novo status (AGENDADA / REALIZADA / CANCELADA / REMARCADA): ");
        String status = sc.nextLine().toUpperCase();

        System.out.print("Nova data (AAAA-MM-DD, Enter para pular): ");
        String data = sc.nextLine();

        System.out.print("Novo horario (Enter para pular): ");
        String horario = sc.nextLine();

        if (!status.isEmpty() && !status.matches("AGENDADA|REALIZADA|CANCELADA|REMARCADA")) {
            System.out.println("Status invalido.");
            return;
        }

        String sql = "UPDATE TDB_CONSULTA SET STATUS = ?, DATA = TO_DATE(?, 'YYYY-MM-DD'), HORARIO = ? WHERE ID_CONSULTA = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.isEmpty() ? null : status);
            stmt.setString(2, data.isEmpty() ? null : data);
            stmt.setString(3, horario.isEmpty() ? null : horario);
            stmt.setInt(4, id);

            int linhas = stmt.executeUpdate();

            System.out.println(linhas > 0 ? "Consulta atualizada!" : "Consulta nao encontrada.");

        } catch (Exception e) {
            System.out.println("Erro ao atualizar consulta: " + e.getMessage());
        }
    }

    // Metodo para excluir consulta
    public void excluir() {

        System.out.print("ID da consulta a excluir: ");
        int id = Integer.parseInt(sc.nextLine());

        String sql = "DELETE FROM TDB_CONSULTA WHERE ID_CONSULTA = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int linhas = stmt.executeUpdate();

            System.out.println(linhas > 0 ? "Consulta excluida!" : "Consulta nao encontrada.");

        } catch (Exception e) {
            System.out.println("Erro ao excluir consulta: " + e.getMessage());
        }
    }

    // Metodo auxiliar
    public boolean existe(int id) {

        String sql = "SELECT 1 FROM TDB_CONSULTA WHERE ID_CONSULTA = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            return stmt.executeQuery().next();

        } catch (Exception e) {
            return false;
        }
    }
}