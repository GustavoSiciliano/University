package DeNovoNao.dao;

import DeNovoNao.conexao.Conexao;
import DeNovoNao.modelo.Pessoa;

import java.sql.*;
import java.util.Scanner;

// Classe responsável pelas operações da tabela VOLUNTARIO
public class VoluntarioDAO {

    private Scanner sc;

    // Recebe o scanner do menu
    public VoluntarioDAO(Scanner sc) {
        this.sc = sc;
    }

    // Metodo para cadastrar voluntario
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

        System.out.print("ID do voluntario: ");
        int id = Integer.parseInt(sc.nextLine());

        System.out.print("Atuacao (Enter para pular): ");
        String atuacao = sc.nextLine();

        System.out.print("Disponibilidade (Enter para pular): ");
        String disponibilidade = sc.nextLine();

        Pessoa pessoa = new Pessoa(nome, cpf, tel, email, dataNasc);

        if (!pessoa.cadastroValido()) {
            System.out.println("Dados invalidos. Nome, CPF e data de nascimento sao obrigatorios.");
            return;
        }

        // SQL para inserir pessoa e voluntario
        String sqlPessoa = "INSERT INTO TDB_PESSOA (CPF, NOME, DATA_NASC, TELEFONE, EMAIL) VALUES (?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?)";
        String sqlVoluntario = "INSERT INTO TDB_VOLUNTARIO (ID_VOLUNTARIO, ATUACAO, DISPONIBILIDADE, CPF) VALUES (?, ?, ?, ?)";

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

                PreparedStatement sv = conn.prepareStatement(sqlVoluntario);
                sv.setInt(1, id);
                sv.setString(2, atuacao.isEmpty() ? null : atuacao);
                sv.setString(3, disponibilidade.isEmpty() ? null : disponibilidade);
                sv.setString(4, cpf);
                sv.executeUpdate();

                conn.commit();

                System.out.println("Voluntario cadastrado com sucesso!");

            } catch (SQLException e) {

                conn.rollback();

                System.out.println("Erro ao cadastrar voluntario: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Erro de conexao: " + e.getMessage());
        }
    }

    // Metodo para listar voluntarios
    public void listar() {

        // JOIN para trazer dados da pessoa
        String sql = "SELECT p.CPF, p.NOME, p.DATA_NASC, p.TELEFONE, p.EMAIL, " +
                "v.ID_VOLUNTARIO, v.ATUACAO, v.DISPONIBILIDADE " +
                "FROM TDB_PESSOA p JOIN TDB_VOLUNTARIO v ON p.CPF = v.CPF";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n--- VOLUNTARIOS ---");
            boolean achou = false;

            // Percorre resultados
            while (rs.next()) {
                achou = true;

                System.out.println("ID: " + rs.getInt("ID_VOLUNTARIO"));
                System.out.println("Nome: " + rs.getString("NOME"));
                System.out.println("CPF: " + rs.getString("CPF"));
                System.out.println("Nasc.: " + rs.getDate("DATA_NASC"));
                System.out.println("Atuacao: " + rs.getString("ATUACAO"));
                System.out.println("Disponibilidade: " + rs.getString("DISPONIBILIDADE"));
                System.out.println("- - - - - - - - - - - - - -");
            }

            if (!achou) System.out.println("Nenhum voluntario cadastrado.");

        } catch (Exception e) {
            System.out.println("Erro ao listar voluntarios: " + e.getMessage());
        }
    }

    // Metodo para atualizar voluntario
    public void atualizar() {

        System.out.print("CPF do voluntario: ");
        String cpf = sc.nextLine();

        System.out.print("Nova atuacao (Enter para pular): ");
        String atuacao = sc.nextLine();

        System.out.print("Nova disponibilidade (Enter para pular): ");
        String disponibilidade = sc.nextLine();

        System.out.print("Novo telefone (Enter para pular): ");
        String tel = sc.nextLine();

        System.out.print("Novo email (Enter para pular): ");
        String email = sc.nextLine();

        String sqlPessoa = "UPDATE TDB_PESSOA SET TELEFONE = ?, EMAIL = ? WHERE CPF = ?";
        String sqlVoluntario = "UPDATE TDB_VOLUNTARIO SET ATUACAO = ?, DISPONIBILIDADE = ? WHERE CPF = ?";

        try (Connection conn = Conexao.conectar()) {

            conn.setAutoCommit(false);

            try {
                // Atualiza pessoa
                PreparedStatement sp = conn.prepareStatement(sqlPessoa);
                sp.setString(1, tel.isEmpty() ? null : tel);
                sp.setString(2, email.isEmpty() ? null : email);
                sp.setString(3, cpf);
                sp.executeUpdate();

                // Atualiza voluntario
                PreparedStatement sv = conn.prepareStatement(sqlVoluntario);
                sv.setString(1, atuacao.isEmpty() ? null : atuacao);
                sv.setString(2, disponibilidade.isEmpty() ? null : disponibilidade);
                sv.setString(3, cpf);

                int linhas = sv.executeUpdate();

                conn.commit();

                System.out.println(linhas > 0 ? "Voluntario atualizado!" : "Voluntario nao encontrado.");

            } catch (SQLException e) {

                conn.rollback();

                System.out.println("Erro ao atualizar: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Erro de conexao: " + e.getMessage());
        }
    }

    // Metodo para excluir voluntario
    public void excluir() {

        System.out.print("CPF do voluntario a excluir: ");
        String cpf = sc.nextLine();

        // DELETE na pessoa remove também voluntario (CASCADE)
        String sql = "DELETE FROM TDB_PESSOA WHERE CPF = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            int linhas = stmt.executeUpdate();

            System.out.println(linhas > 0 ? "Voluntario excluido!" : "Voluntario nao encontrado.");

        } catch (Exception e) {
            System.out.println("Erro ao excluir voluntario: " + e.getMessage());
        }
    }
}