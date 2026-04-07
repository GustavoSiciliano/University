package DeNovoNao.dao;

import DeNovoNao.conexao.Conexao;

import java.sql.*;
import java.util.Scanner;

// Classe responsável pelas operações da tabela DENTISTA
public class DentistaDAO {

    private Scanner sc;

    // Recebe o scanner vindo do menu
    public DentistaDAO(Scanner sc) {
        this.sc = sc;
    }

    // Metodo para cadastrar dentista
    public void cadastrar() {

        // Entrada de dados
        System.out.print("CPF: ");
        String cpf = sc.nextLine();

        System.out.print("Nome: ");
        String nome = sc.nextLine();

        System.out.print("Data de nascimento (AAAA-MM-DD): ");
        String dataNasc = sc.nextLine();

        System.out.print("Telefone (Enter para pular): ");
        String tel = sc.nextLine();

        System.out.print("Email (Enter para pular): ");
        String email = sc.nextLine();

        System.out.print("CRO: ");
        String cro = sc.nextLine();

        System.out.print("Especialidade (Enter para pular): ");
        String especialidade = sc.nextLine();

        System.out.print("Disponibilidade (Enter para pular): ");
        String disponibilidade = sc.nextLine();

        if (cpf.isEmpty() || nome.isEmpty() || dataNasc.isEmpty() || cro.isEmpty()) {
            System.out.println("CPF, nome, data de nascimento e CRO sao obrigatorios.");
            return;
        }

        String sqlPessoa = "INSERT INTO TDB_PESSOA (CPF, NOME, DATA_NASC, TELEFONE, EMAIL) VALUES (?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?)";
        String sqlDentista = "INSERT INTO TDB_DENTISTA (CRO, CPF, ESPECIALIDADE, DISPONIBILIDADE) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar()) {

            conn.setAutoCommit(false);

            try {
                PreparedStatement sp = conn.prepareStatement(sqlPessoa);
                sp.setString(1, cpf);
                sp.setString(2, nome);
                sp.setString(3, dataNasc);
                sp.setString(4, tel.isEmpty() ? null : tel);
                sp.setString(5, email.isEmpty() ? null : email);
                sp.executeUpdate();

                PreparedStatement sd = conn.prepareStatement(sqlDentista);
                sd.setString(1, cro);
                sd.setString(2, cpf);
                sd.setString(3, especialidade.isEmpty() ? null : especialidade);
                sd.setString(4, disponibilidade.isEmpty() ? null : disponibilidade);
                sd.executeUpdate();

                conn.commit();

                System.out.println("Dentista cadastrado com sucesso!");

            } catch (SQLException e) {

                conn.rollback();

                System.out.println("Erro ao cadastrar dentista: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Erro de conexao: " + e.getMessage());
        }
    }

    // Metodo para listar dentistas
    public void listar() {

        // Join entre pessoa e dentista
        String sql = "SELECT p.CPF, p.NOME, p.DATA_NASC, p.TELEFONE, p.EMAIL, " +
                "d.CRO, d.ESPECIALIDADE, d.DISPONIBILIDADE " +
                "FROM TDB_PESSOA p JOIN TDB_DENTISTA d ON p.CPF = d.CPF";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n--- DENTISTAS ---");
            boolean achou = false;

            // Percorre os resultados
            while (rs.next()) {
                achou = true;

                System.out.println("CRO: " + rs.getString("CRO"));
                System.out.println("Nome: " + rs.getString("NOME"));
                System.out.println("CPF: " + rs.getString("CPF"));
                System.out.println("Nasc.: " + rs.getDate("DATA_NASC"));
                System.out.println("Especialidade: " + rs.getString("ESPECIALIDADE"));
                System.out.println("Disponibilidade: " + rs.getString("DISPONIBILIDADE"));
                System.out.println("- - - - - - - - - - - - - -");
            }

            if (!achou) System.out.println("Nenhum dentista cadastrado.");

        } catch (Exception e) {
            System.out.println("Erro ao listar dentistas: " + e.getMessage());
        }
    }

    // Metodo para atualizar dentista
    public void atualizar() {

        System.out.print("CPF do dentista: ");
        String cpf = sc.nextLine();

        System.out.print("Novo telefone (Enter para pular): ");
        String tel = sc.nextLine();

        System.out.print("Novo email (Enter para pular): ");
        String email = sc.nextLine();

        System.out.print("Nova especialidade (Enter para pular): ");
        String especialidade = sc.nextLine();

        System.out.print("Nova disponibilidade (Enter para pular): ");
        String disponibilidade = sc.nextLine();

        String sqlPessoa = "UPDATE TDB_PESSOA SET TELEFONE = ?, EMAIL = ? WHERE CPF = ?";
        String sqlDentista = "UPDATE TDB_DENTISTA SET ESPECIALIDADE = ?, DISPONIBILIDADE = ? WHERE CPF = ?";

        try (Connection conn = Conexao.conectar()) {

            conn.setAutoCommit(false);

            try {
                PreparedStatement sp = conn.prepareStatement(sqlPessoa);
                sp.setString(1, tel.isEmpty() ? null : tel);
                sp.setString(2, email.isEmpty() ? null : email);
                sp.setString(3, cpf);
                sp.executeUpdate();

                PreparedStatement sd = conn.prepareStatement(sqlDentista);
                sd.setString(1, especialidade.isEmpty() ? null : especialidade);
                sd.setString(2, disponibilidade.isEmpty() ? null : disponibilidade);
                sd.setString(3, cpf);

                int linhas = sd.executeUpdate();

                conn.commit();

                System.out.println(linhas > 0 ? "Dentista atualizado!" : "Dentista nao encontrado.");

            } catch (SQLException e) {

                conn.rollback();

                System.out.println("Erro ao atualizar: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Erro de conexao: " + e.getMessage());
        }
    }

    // Metodo para excluir dentista
    public void excluir() {

        System.out.print("CPF do dentista a excluir: ");
        String cpf = sc.nextLine();

        String sql = "DELETE FROM TDB_PESSOA WHERE CPF = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            int linhas = stmt.executeUpdate();

            System.out.println(linhas > 0 ? "Dentista excluido!" : "Dentista nao encontrado.");

        } catch (Exception e) {
            System.out.println("Erro ao excluir: " + e.getMessage());
        }
    }

    // Metodo auxiliar
    public boolean existe(String cro, String cpf) {

        String sql = "SELECT 1 FROM TDB_DENTISTA WHERE CRO = ? AND CPF = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cro);
            stmt.setString(2, cpf);

            return stmt.executeQuery().next();

        } catch (Exception e) {
            return false;
        }
    }
}