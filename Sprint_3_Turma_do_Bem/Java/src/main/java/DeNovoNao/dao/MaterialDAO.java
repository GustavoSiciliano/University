package DeNovoNao.dao;

import DeNovoNao.conexao.Conexao;
import DeNovoNao.modelo.Material;

import java.sql.*;
import java.util.Scanner;

// Classe responsável pelas operações da tabela MATERIAL
public class MaterialDAO {

    private Scanner sc;

    // Recebe o scanner do menu
    public MaterialDAO(Scanner sc) {
        this.sc = sc;
    }

    // Metodo para cadastrar material
    public void cadastrar() {

        // Entrada de dados
        System.out.print("ID do material: ");
        int id = Integer.parseInt(sc.nextLine());

        System.out.print("Nome: ");
        String nome = sc.nextLine();

        System.out.print("Tipo (Enter para pular): ");
        String tipo = sc.nextLine();

        System.out.print("Quantidade >= 0: ");
        double qtd = Double.parseDouble(sc.nextLine());

        System.out.print("Unidade (Enter para pular): ");
        String unidade = sc.nextLine();

        System.out.print("Validade (AAAA-MM-DD, Enter para pular): ");
        String validade = sc.nextLine();

        Material m = new Material(id, nome, tipo, qtd, unidade, validade);

        if (!m.cadastroValido()) {
            System.out.println("Dados invalidos. Nome e obrigatorio e quantidade deve ser >= 0.");
            return;
        }

        // SQL de inserção
        String sql = "INSERT INTO TDB_MATERIAL (ID_MATERIAL, NOME, TIPO, QUANTIDADE, UNIDADE, VALIDADE) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.setString(2, nome);
            stmt.setString(3, tipo.isEmpty() ? null : tipo);
            stmt.setDouble(4, qtd);
            stmt.setString(5, unidade.isEmpty() ? null : unidade);
            stmt.setString(6, validade.isEmpty() ? null : validade);

            stmt.executeUpdate();

            System.out.println("Material cadastrado com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao cadastrar material: " + e.getMessage());
        }
    }

    // Metodo para listar materiais
    public void listar() {

        String sql = "SELECT * FROM TDB_MATERIAL";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n--- MATERIAIS ---");
            boolean achou = false;

            // Percorre os resultados
            while (rs.next()) {
                achou = true;

                System.out.println("ID: " + rs.getInt("ID_MATERIAL"));
                System.out.println("Nome: " + rs.getString("NOME"));
                System.out.println("Tipo: " + rs.getString("TIPO"));
                System.out.println("Quantidade: " + rs.getDouble("QUANTIDADE") + " " + rs.getString("UNIDADE"));
                System.out.println("Validade: " + rs.getDate("VALIDADE"));
                System.out.println("- - - - - - - - - - - - - -");
            }

            if (!achou) System.out.println("Nenhum material cadastrado.");

        } catch (Exception e) {
            System.out.println("Erro ao listar materiais: " + e.getMessage());
        }
    }

    // Metodo para atualizar material
    public void atualizar() {

        System.out.print("ID do material a atualizar: ");
        int id = Integer.parseInt(sc.nextLine());

        System.out.print("Nova quantidade >= 0: ");
        double qtd = Double.parseDouble(sc.nextLine());

        System.out.print("Nova validade (AAAA-MM-DD, Enter para pular): ");
        String validade = sc.nextLine();

        if (qtd < 0) {
            System.out.println("Quantidade invalida.");
            return;
        }

        String sql = "UPDATE TDB_MATERIAL SET QUANTIDADE = ?, VALIDADE = ? WHERE ID_MATERIAL = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, qtd);
            stmt.setString(2, validade.isEmpty() ? null : validade);
            stmt.setInt(3, id);

            int linhas = stmt.executeUpdate();

            System.out.println(linhas > 0 ? "Material atualizado!" : "Material nao encontrado.");

        } catch (Exception e) {
            System.out.println("Erro ao atualizar material: " + e.getMessage());
        }
    }

    // Metodo para excluir material
    public void excluir() {

        System.out.print("ID do material a excluir: ");
        int id = Integer.parseInt(sc.nextLine());

        String sql = "DELETE FROM TDB_MATERIAL WHERE ID_MATERIAL = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int linhas = stmt.executeUpdate();

            System.out.println(linhas > 0 ? "Material excluido!" : "Material nao encontrado.");

        } catch (Exception e) {
            System.out.println("Erro ao excluir material: " + e.getMessage());
        }
    }
}