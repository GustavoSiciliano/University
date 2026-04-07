package DeNovoNao.dao;

import DeNovoNao.conexao.Conexao;

import java.sql.*;
import java.util.Scanner;

// Classe responsável pelas operações da tabela PACIENTE
public class PacienteDAO {

    private Scanner sc;

    // Recebe o scanner do menu
    public PacienteDAO(Scanner sc) {
        this.sc = sc;
    }

    // Metodo para cadastrar paciente
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

        System.out.print("ID do paciente: ");
        int id = Integer.parseInt(sc.nextLine());

        System.out.print("Endereco (Enter para pular): ");
        String endereco = sc.nextLine();

        System.out.print("Convenio (Enter para pular): ");
        String convenio = sc.nextLine();

        if (cpf.isEmpty() || nome.isEmpty() || dataNasc.isEmpty()) {
            System.out.println("CPF, nome e data de nascimento sao obrigatorios.");
            return;
        }

        // SQL para inserir pessoa e paciente
        String sqlPessoa = "INSERT INTO TDB_PESSOA (CPF, NOME, DATA_NASC, TELEFONE, EMAIL) VALUES (?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?)";
        String sqlPaciente = "INSERT INTO TDB_PACIENTE (ID_PACIENTE, ENDERECO, CONVENIO, CPF) VALUES (?, ?, ?, ?)";

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

                PreparedStatement spa = conn.prepareStatement(sqlPaciente);
                spa.setInt(1, id);
                spa.setString(2, endereco.isEmpty() ? null : endereco);
                spa.setString(3, convenio.isEmpty() ? null : convenio);
                spa.setString(4, cpf);
                spa.executeUpdate();

                conn.commit();

                System.out.println("Paciente cadastrado com sucesso!");

            } catch (SQLException e) {

                conn.rollback();

                System.out.println("Erro ao cadastrar paciente: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Erro de conexao: " + e.getMessage());
        }
    }

    // Metodo para listar pacientes
    public void listar() {

        // JOIN entre pessoa e paciente
        String sql = "SELECT p.CPF, p.NOME, p.DATA_NASC, p.TELEFONE, p.EMAIL, " +
                "pa.ID_PACIENTE, pa.ENDERECO, pa.CONVENIO " +
                "FROM TDB_PESSOA p JOIN TDB_PACIENTE pa ON p.CPF = pa.CPF";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n--- PACIENTES ---");
            boolean achou = false;

            // Percorre resultados
            while (rs.next()) {
                achou = true;

                System.out.println("ID: " + rs.getInt("ID_PACIENTE"));
                System.out.println("Nome: " + rs.getString("NOME"));
                System.out.println("CPF: " + rs.getString("CPF"));
                System.out.println("Nasc.: " + rs.getDate("DATA_NASC"));
                System.out.println("Telefone: " + rs.getString("TELEFONE"));
                System.out.println("Email: " + rs.getString("EMAIL"));
                System.out.println("Endereco: " + rs.getString("ENDERECO"));
                System.out.println("Convenio: " + rs.getString("CONVENIO"));
                System.out.println("- - - - - - - - - - - - - -");
            }

            if (!achou) System.out.println("Nenhum paciente cadastrado.");

        } catch (Exception e) {
            System.out.println("Erro ao listar pacientes: " + e.getMessage());
        }
    }

    // Metodo para atualizar paciente
    public void atualizar() {

        System.out.print("CPF do paciente: ");
        String cpf = sc.nextLine();

        System.out.print("Novo telefone (Enter para pular): ");
        String tel = sc.nextLine();

        System.out.print("Novo email (Enter para pular): ");
        String email = sc.nextLine();

        System.out.print("Novo endereco (Enter para pular): ");
        String endereco = sc.nextLine();

        System.out.print("Novo convenio (Enter para pular): ");
        String convenio = sc.nextLine();

        String sqlPessoa = "UPDATE TDB_PESSOA SET TELEFONE = ?, EMAIL = ? WHERE CPF = ?";
        String sqlPaciente = "UPDATE TDB_PACIENTE SET ENDERECO = ?, CONVENIO = ? WHERE CPF = ?";

        try (Connection conn = Conexao.conectar()) {

            conn.setAutoCommit(false);

            try {
                PreparedStatement sp = conn.prepareStatement(sqlPessoa);
                sp.setString(1, tel.isEmpty() ? null : tel);
                sp.setString(2, email.isEmpty() ? null : email);
                sp.setString(3, cpf);
                sp.executeUpdate();

                PreparedStatement spa = conn.prepareStatement(sqlPaciente);
                spa.setString(1, endereco.isEmpty() ? null : endereco);
                spa.setString(2, convenio.isEmpty() ? null : convenio);
                spa.setString(3, cpf);

                int linhas = spa.executeUpdate();

                conn.commit();

                System.out.println(linhas > 0 ? "Paciente atualizado!" : "Paciente nao encontrado.");

            } catch (SQLException e) {

                conn.rollback();

                System.out.println("Erro ao atualizar: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Erro de conexao: " + e.getMessage());
        }
    }

    // Metodo para excluir paciente
    public void excluir() {

        System.out.print("CPF do paciente a excluir: ");
        String cpf = sc.nextLine();

        // DELETE na pessoa remove também paciente (CASCADE)
        String sql = "DELETE FROM TDB_PESSOA WHERE CPF = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            int linhas = stmt.executeUpdate();

            System.out.println(linhas > 0 ? "Paciente excluido!" : "Paciente nao encontrado.");

        } catch (Exception e) {
            System.out.println("Erro ao excluir: " + e.getMessage());
        }
    }

    // Metodo auxiliar
    public boolean existe(int id, String cpf) {

        String sql = "SELECT 1 FROM TDB_PACIENTE WHERE ID_PACIENTE = ? AND CPF = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.setString(2, cpf);

            return stmt.executeQuery().next();

        } catch (Exception e) {
            return false;
        }
    }
}