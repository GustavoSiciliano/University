package DeNovoNao.dao;

import DeNovoNao.conexao.Conexao;
import DeNovoNao.modelo.Procedimento;

import java.sql.*;
import java.util.Scanner;

// Classe responsável pelas operações da tabela PROCEDIMENTO
public class ProcedimentoDAO {

    private Scanner sc;

    // Recebe o scanner do menu
    public ProcedimentoDAO(Scanner sc) {
        this.sc = sc;
    }

    // Metodo para cadastrar procedimento
    public void cadastrar() {

        // Entrada de dados
        System.out.print("ID do procedimento: ");
        int id = Integer.parseInt(sc.nextLine());

        System.out.print("Nome: ");
        String nome = sc.nextLine();

        System.out.print("Tipo (Enter para pular): ");
        String tipo = sc.nextLine();

        System.out.print("Duracao em minutos (0 para pular): ");
        int duracao = Integer.parseInt(sc.nextLine());

        System.out.print("Orientacao (Enter para pular): ");
        String orientacao = sc.nextLine();

        System.out.print("Custo >= 0 (0 se nao souber): ");
        double custo = Double.parseDouble(sc.nextLine());

        System.out.print("ID da consulta: ");
        int idConsulta = Integer.parseInt(sc.nextLine());

        System.out.print("CRO do dentista: ");
        String cro = sc.nextLine();

        System.out.print("CPF do dentista: ");
        String cpf = sc.nextLine();

        ConsultaDAO consultaDAO = new ConsultaDAO(sc);
        if (!consultaDAO.existe(idConsulta)) {
            System.out.println("Consulta nao encontrada.");
            return;
        }

        DentistaDAO dentistaDAO = new DentistaDAO(sc);
        if (!dentistaDAO.existe(cro, cpf)) {
            System.out.println("Dentista nao encontrado.");
            return;
        }

        // Cria objeto para validar regras
        Procedimento p = new Procedimento(id, nome, tipo, duracao, orientacao, custo, idConsulta, cro, cpf);

        if (!p.cadastroValido()) {
            System.out.println("Dados invalidos. Verifique nome, custo e dentista.");
            return;
        }

        // SQL de inserção
        String sql = "INSERT INTO TDB_PROCEDIMENTO (ID_PROCEDIMENTO, NOME, TIPO, DURACAO, ORIENTACAO, CUSTO, ID_CONSULTA, CRO_DENTISTA, CPF_DENTISTA) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define os valores
            stmt.setInt(1, id);
            stmt.setString(2, nome);
            stmt.setString(3, tipo.isEmpty() ? null : tipo);

            if (duracao > 0) stmt.setInt(4, duracao);
            else stmt.setNull(4, Types.INTEGER);

            stmt.setString(5, orientacao.isEmpty() ? null : orientacao);
            stmt.setDouble(6, custo);
            stmt.setInt(7, idConsulta);
            stmt.setString(8, cro);
            stmt.setString(9, cpf);

            stmt.executeUpdate();

            System.out.println("Procedimento cadastrado com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao cadastrar procedimento: " + e.getMessage());
        }
    }

    // Metodo para listar procedimentos
    public void listar() {

        // JOIN para trazer nome do dentista
        String sql = "SELECT pr.ID_PROCEDIMENTO, pr.NOME, pr.TIPO, pr.DURACAO, pr.CUSTO, " +
                "pr.ID_CONSULTA, p.NOME AS NOME_DENTISTA " +
                "FROM TDB_PROCEDIMENTO pr " +
                "JOIN TDB_DENTISTA d ON pr.CRO_DENTISTA = d.CRO AND pr.CPF_DENTISTA = d.CPF " +
                "JOIN TDB_PESSOA p ON d.CPF = p.CPF";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n--- PROCEDIMENTOS ---");
            boolean achou = false;

            // Percorre resultados
            while (rs.next()) {
                achou = true;

                System.out.println("ID: " + rs.getInt("ID_PROCEDIMENTO"));
                System.out.println("Nome: " + rs.getString("NOME"));
                System.out.println("Tipo: " + rs.getString("TIPO"));
                System.out.println("Duracao: " + rs.getString("DURACAO") + " min");
                System.out.println("Custo: R$ " + rs.getDouble("CUSTO"));
                System.out.println("Consulta: " + rs.getInt("ID_CONSULTA"));
                System.out.println("Dentista: " + rs.getString("NOME_DENTISTA"));
                System.out.println("- - - - - - - - - - - - - -");
            }

            if (!achou) System.out.println("Nenhum procedimento cadastrado.");

        } catch (Exception e) {
            System.out.println("Erro ao listar procedimentos: " + e.getMessage());
        }
    }

    // Metodo para atualizar procedimento
    public void atualizar() {

        System.out.print("ID do procedimento a atualizar: ");
        int id = Integer.parseInt(sc.nextLine());

        System.out.print("Novo nome (Enter para pular): ");
        String nome = sc.nextLine();

        System.out.print("Nova orientacao (Enter para pular): ");
        String orientacao = sc.nextLine();

        System.out.print("Novo custo >= 0 (-1 para pular): ");
        double custo = Double.parseDouble(sc.nextLine());

        String sql = "UPDATE TDB_PROCEDIMENTO SET NOME = ?, ORIENTACAO = ?, CUSTO = ? WHERE ID_PROCEDIMENTO = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome.isEmpty() ? null : nome);
            stmt.setString(2, orientacao.isEmpty() ? null : orientacao);

            if (custo >= 0) stmt.setDouble(3, custo);
            else stmt.setNull(3, Types.DOUBLE);

            stmt.setInt(4, id);

            int linhas = stmt.executeUpdate();

            System.out.println(linhas > 0 ? "Procedimento atualizado!" : "Procedimento nao encontrado.");

        } catch (Exception e) {
            System.out.println("Erro ao atualizar procedimento: " + e.getMessage());
        }
    }

    // Metodo para excluir procedimento
    public void excluir() {

        System.out.print("ID do procedimento a excluir: ");
        int id = Integer.parseInt(sc.nextLine());

        String sql = "DELETE FROM TDB_PROCEDIMENTO WHERE ID_PROCEDIMENTO = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int linhas = stmt.executeUpdate();

            System.out.println(linhas > 0 ? "Procedimento excluido!" : "Procedimento nao encontrado.");

        } catch (Exception e) {
            System.out.println("Erro ao excluir procedimento: " + e.getMessage());
        }
    }
}