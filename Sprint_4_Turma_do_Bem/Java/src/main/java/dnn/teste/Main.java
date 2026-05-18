package dnn.teste;

import dnn.boo.CampanhaBoo;
import dnn.boo.UsuarioBoo;
import dnn.dao.Conexao;
import dnn.modelo.*;
import dnn.servico.*;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

/**
 * Main — demonstracao interativa do projeto Java
 * <p>
 * Gustavo Rodrigues Siciliano — RM568419
 * Gustavo de Jesus Silva — RM567926
 * Samuel Keniti Kina de Lima — RM567614
 * <p>
 * Executar: mvn compile exec:java
 */
public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        cabecalho();
        System.out.println("Verificando conexao com Oracle...");
        if (!testarConexao()) {
            System.out.println("Verifique a rede FIAP ou VPN e tente novamente.");
            return;
        }
        menuPrincipal();
    }

    static void menuPrincipal() {
        while (true) {
            titulo("MENU PRINCIPAL");
            System.out.println("1. Validar regras de negocio");
            System.out.println("2. Login");
            System.out.println("3. Dentistas");
            System.out.println("4. Voluntarios");
            System.out.println("5. Doadores");
            System.out.println("6. Campanhas");
            System.out.println("7. Doacoes");
            System.out.println("8. Materiais");
            System.out.println("9. Prontuarios");
            System.out.println("10. Procedimentos");
            System.out.println("11. Registros de IA");
            System.out.println("12. Dashboard");
            System.out.println("0. Sair");
            String op = input("Opcao");
            switch (op) {
                case "1" -> menuModels();
                case "2" -> menuLogin();
                case "3" -> menuDentistas();
                case "4" -> menuVoluntarios();
                case "5" -> menuDoadores();
                case "6" -> menuCampanhas();
                case "7" -> menuDoacoes();
                case "8" -> menuMateriais();
                case "9" -> menuProntuarios();
                case "10" -> menuProcedimentos();
                case "11" -> menuRegistrosIA();
                case "12" -> menuDashboard();
                case "0" -> {
                    System.out.println("\nAte logo!\n");
                    return;
                }
                default -> System.out.println("Opcao invalida.");
            }
        }
    }

    // 1. Modelos

    static void menuModels() {
        titulo("VALIDACAO DAS REGRAS DE NEGOCIO");

        Campanha c = new Campanha();
        c.setMetaValor(20000);
        c.setTotalArrecadado(12500);
        print("Campanha com meta R$20.000 e R$12.500 arrecadados");
        print("Percentual atingido: " + String.format("%.1f%%", c.percentualMeta()));
        print("Meta atingida: " + (c.metaAtingida() ? "Sim" : "Nao"));

        br();
        Material m = new Material();
        m.setQuantidade(8);
        m.setQuantidadeMinima(20);
        print("Material com quantidade 8 e minimo de 20");
        print("Estoque baixo: " + (m.estoqueBaixo() ? "Sim" : "Nao"));

        br();
        Doacao d = new Doacao();
        d.setValor(-50);
        print("Doacao com valor -50");
        print("Valor valido: " + (d.valorValido() ? "Sim" : "Nao"));

        br();
        Prontuario p = new Prontuario();
        p.setDescricao(" ");
        print("Prontuario com descricao em branco");
        print("Descricao valida: " + (p.descricaoValida() ? "Sim" : "Nao"));

        br();
        Dentista den = new Dentista();
        den.setCro("SP-12345");
        den.setCpf("12345678901");
        den.setNome("Dr. Teste");
        print("Dentista com CRO SP-12345 e CPF 12345678901");
        print("CRO valido: " + (den.croValido() ? "Sim" : "Nao"));
        print("CPF valido: " + (den.cpfValido() ? "Sim" : "Nao"));

        br();
        Doador doad = new Doador();
        doad.setTipoDoador("PF");
        doad.setNome("Jose");
        doad.setCpf("12345678901");
        print("Doador do tipo PF");
        print("Tipo valido: " + (doad.tipoValido() ? "Sim" : "Nao"));

        br();
        Procedimento proc = new Procedimento();
        proc.setNome("Limpeza");
        proc.setCusto(-10);
        print("Procedimento com custo -10");
        print("Custo valido: " + (proc.custoValido() ? "Sim" : "Nao"));

        br();
        RegistroIA reg = new RegistroIA();
        reg.setTipoPredicao("FALTA");
        reg.setRisco("ALTO");
        print("Registro IA do tipo FALTA com risco ALTO");
        print("E predicao de falta: " + (reg.isFalta() ? "Sim" : "Nao"));
        print("E risco alto: " + (reg.isRiscoAlto() ? "Sim" : "Nao"));

        br();
        Usuario u = new Usuario();
        u.setPerfil("ADMIN");
        u.setAtivo("S");
        print("Usuario com perfil ADMIN e ativo S");
        print("E administrador: " + (u.isAdmin() ? "Sim" : "Nao"));
        print("Esta ativo: " + (u.isAtivo() ? "Sim" : "Nao"));

        pausar();
    }

    // 2. Login

    static void menuLogin() {
        titulo("LOGIN");
        System.out.println("Usuarios disponíveis no banco:");
        System.out.println("joao.silva, ana.costa, carlos.lima, fernanda.lima, rafael.gomes, admin.tdb, bruno.alves");
        br();
        String login = input("Login");
        String senha = input("Senha");
        try {
            var r = new UsuarioBoo().login(login, senha);
            br();
            System.out.println("Login realizado com sucesso!");
            print("Nome: " + r.get("nome"));
            print("Perfil: " + r.get("perfil"));
        } catch (Exception e) {
            br();
            System.out.println("Falha no login: " + e.getMessage());
        }
        pausar();
    }

    // 3. Dentistas

    static void menuDentistas() {
        while (true) {
            titulo("DENTISTAS");
            System.out.println("1. Listar todos");
            System.out.println("2. Buscar por CRO");
            System.out.println("0. Voltar");
            String op = input("Opcao");
            if (op.equals("0")) break;
            try {
                DentistaServico srv = new DentistaServico();
                if (op.equals("1")) {
                    List<Dentista> lista = srv.listar();
                    br();
                    print(lista.size() + " dentistas cadastrados");
                    br();
                    for (Dentista den : lista) {
                        print("CRO: " + den.getCro() + " Nome: " + den.getNome());
                        print("Especialidade: " + den.getEspecialidade());
                        print("Disponibilidade: " + den.getDisponibilidade());
                        br();
                    }
                } else if (op.equals("2")) {
                    String cro = input("CRO");
                    Dentista den = srv.buscar(cro);
                    if (den == null) {
                        System.out.println("Dentista nao encontrado.");
                    } else {
                        br();
                        print("CRO: " + den.getCro());
                        print("Nome: " + den.getNome());
                        print("Especialidade: " + den.getEspecialidade());
                        print("Disponibilidade:" + den.getDisponibilidade());
                        print("Telefone: " + den.getTelefone());
                        print("Email: " + den.getEmail());
                    }
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
            pausar();
        }
    }

    // 4. Voluntários

    static void menuVoluntarios() {
        titulo("VOLUNTARIOS");
        try {
            List<Voluntario> lista = new VoluntarioServico().listar();
            br();
            print(lista.size() + " voluntarios cadastrados");
            br();
            for (Voluntario v : lista) {
                print("Nome: " + v.getNome() + " Area: " + v.getArea());
                print("Disponibilidade: " + v.getDisponibilidade());
                br();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        pausar();
    }

    // 5. Doadores

    static void menuDoadores() {
        titulo("DOADORES");
        try {
            List<Doador> lista = new DoadorServico().listar();
            br();
            print(lista.size() + " doadores cadastrados");
            br();
            for (Doador d : lista) {
                print("Nome: " + d.getNome() + " Tipo: " + (d.isPessoaFisica() ? "Pessoa Fisica" : "Pessoa Juridica"));
                print("Telefone: " + d.getTelefone());
                br();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        pausar();
    }

    // 6. Campanhas

    static void menuCampanhas() {
        while (true) {
            titulo("CAMPANHAS");
            System.out.println("1. Listar todas");
            System.out.println("2. Buscar por ID");
            System.out.println("3. Demonstrar CRUD");
            System.out.println("0. Voltar");
            String op = input("Opcao");
            if (op.equals("0")) break;
            CampanhaBoo boo = new CampanhaBoo();
            try {
                if (op.equals("1")) {
                    List<Campanha> lista = boo.listar();
                    br();
                    print(lista.size() + " campanhas cadastradas");
                    br();
                    for (Campanha c : lista) {
                        print("Nome: " + c.getNome());
                        print("Meta: R$ " + String.format("%.2f", c.getMetaValor()) +
                                " Arrecadado: R$ " + String.format("%.2f", c.getTotalArrecadado()) +
                                " (" + String.format("%.1f", c.percentualMeta()) + "%)");
                        print("Ativa: " + (c.isAtiva() ? "Sim" : "Nao") +
                                " Meta atingida: " + (c.metaAtingida() ? "Sim" : "Nao"));
                        br();
                    }
                } else if (op.equals("2")) {
                    int id = Integer.parseInt(input("ID da campanha"));
                    Campanha c = boo.buscar(id);
                    br();
                    print("Nome: " + c.getNome());
                    print("Descricao: " + c.getDescricao());
                    print("Periodo: " + c.getDataInicio() + " ate " + c.getDataFim());
                    print("Meta:  R$ " + String.format("%.2f", c.getMetaValor()));
                    print("Arrecadado:  R$ " + String.format("%.2f", c.getTotalArrecadado()) +
                            " (" + String.format("%.1f", c.percentualMeta()) + "%)");
                    print("Meta atingida: " + (c.metaAtingida() ? "Sim" : "Nao"));
                } else if (op.equals("3")) {
                    br();
                    System.out.println("Criando campanha de teste...");
                    Campanha nova = new Campanha("Campanha Teste", "Demonstracao CRUD", "2025-06-01", "2025-06-30", 1000);
                    boo.criar(nova);
                    print("Criada com ID " + nova.getIdCampanha());

                    nova.setNome("Campanha Atualizada");
                    boo.atualizar(nova.getIdCampanha(), nova);
                    print("Atualizada.");

                    boo.excluir(nova.getIdCampanha());
                    print("Excluida.");
                    br();
                    System.out.println("CRUD executado com sucesso.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
            pausar();
        }
    }

    // 7. Doações

    static void menuDoacoes() {
        titulo("DOACOES");
        try {
            List<Doacao> lista = new DoacaoServico().listar();
            double total = lista.stream().mapToDouble(Doacao::getValor).sum();
            br();
            print(lista.size() + " doacoes registradas");
            print("Total arrecadado: R$ " + String.format("%.2f", total));
            br();
            for (Doacao d : lista) {
                print("R$ " + String.format("%.2f", d.getValor()) +
                        " Forma: " + d.getFormaPgto() +
                        " Doador: " + (d.getNomeDoador() != null ? d.getNomeDoador() : "-"));
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        pausar();
    }

    // 8. Materiais

    static void menuMateriais() {
        titulo("MATERIAIS E ESTOQUE");
        try {
            List<Material> lista = new MaterialServico().listar();
            long baixo = lista.stream().filter(Material::estoqueBaixo).count();
            br();
            print(lista.size() + " materiais cadastrados");
            print(baixo + " com estoque abaixo do minimo");
            br();
            for (Material m : lista) {
                String status = m.estoqueBaixo() ? "ESTOQUE BAIXO" : "OK";
                print(m.getNome() + " Qtd: " + (int) m.getQuantidade() +
                        " " + m.getUnidade() + " Minimo: " + (int) m.getQuantidadeMinima() +
                        " " + status);
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        pausar();
    }

    // 9. Prontuários

    static void menuProntuarios() {
        titulo("PRONTUARIOS");
        try {
            List<Prontuario> lista = new ProntuarioServico().listar();
            br();
            print(lista.size() + " prontuarios registrados");
            br();
            for (Prontuario p : lista) {
                print("Paciente: " + p.getNomePaciente() + " Dentista: " + p.getNomeDentista());
                print("Data: " + p.getDtRegistro());
                print("Descricao: " + p.getDescricao());
                if (p.getObservacoes() != null) print("Obs: " + p.getObservacoes());
                br();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        pausar();
    }

    // 10. Procedimentos

    static void menuProcedimentos() {
        titulo("PROCEDIMENTOS");
        try {
            List<Procedimento> lista = new ProcedimentoServico().listarTodos();
            br();
            print(lista.size() + " procedimentos registrados");
            br();
            for (Procedimento p : lista) {
                print("Nome: " + p.getNome() + " Tipo: " + (p.getTipo() != null ? p.getTipo() : "-"));
                print("Duracao: " + p.getDuracaoMin() + " min   Consulta ID: " + p.getIdConsulta());
                if (p.getOrientacao() != null) print("Orientacao: " + p.getOrientacao());
                br();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        pausar();
    }

    // 11. Registros IA

    static void menuRegistrosIA() {
        titulo("REGISTROS DE IA");
        try {
            List<RegistroIA> lista = new RegistroIAServico().listarTodos();
            long faltas = lista.stream().filter(RegistroIA::isFalta).count();
            long arrec = lista.stream().filter(RegistroIA::isArrecadacao).count();
            long altos = lista.stream().filter(RegistroIA::isRiscoAlto).count();
            br();
            print(lista.size() + " predicoes registradas");
            print("Falta: " + faltas + "   Arrecadacao: " + arrec + "   Risco alto: " + altos);
            br();
            for (RegistroIA r : lista) {
                print("Tipo: " + r.getTipoPredicao() +
                        " Probabilidade: " + String.format("%.0f%%", r.getProbResultado() * 100) +
                        " Risco: " + (r.getRisco() != null ? r.getRisco() : "-") +
                        " Classe: " + (r.getClassePrevista() != null ? r.getClassePrevista() : "-"));
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        pausar();
    }

    // 12. Dashboard

    static void menuDashboard() {
        titulo("DASHBOARD");
        try (Connection conn = Conexao.conectar()) {
            br();
            System.out.println("Pessoas");
            print("Pacientes: " + qi(conn, "SELECT COUNT(*) FROM tdb_Paciente"));
            print("Dentistas: " + qi(conn, "SELECT COUNT(*) FROM tdb_Dentista"));
            print("Voluntarios: " + qi(conn, "SELECT COUNT(*) FROM tdb_Voluntario"));
            print("Doadores: " + qi(conn, "SELECT COUNT(*) FROM tdb_Doador"));
            print("Usuarios ativos: " + qi(conn, "SELECT COUNT(*) FROM tdb_Usuario WHERE ativo='S'"));

            br();
            System.out.println("Atendimento");
            print("Consultas total: " + qi(conn, "SELECT COUNT(*) FROM tdb_Consulta"));
            print("Realizadas: " + qi(conn, "SELECT COUNT(*) FROM tdb_Consulta WHERE status='REALIZADA'"));
            print("Faltas: " + qi(conn, "SELECT COUNT(*) FROM tdb_Consulta WHERE status='FALTA'"));
            print("Prontuarios: " + qi(conn, "SELECT COUNT(*) FROM tdb_Prontuario"));
            print("Procedimentos: " + qi(conn, "SELECT COUNT(*) FROM tdb_Procedimento"));

            br();
            System.out.println("Financeiro");
            print("Campanhas ativas: " + qi(conn, "SELECT COUNT(*) FROM tdb_Campanha WHERE ativo='S'"));
            print("Arrecadado total: R$ " + String.format("%.2f", qd(conn, "SELECT NVL(SUM(valor),0) FROM tdb_Doacao")));
            print("Arrecadado/mes: R$ " + String.format("%.2f",
                    qd(conn, "SELECT NVL(SUM(valor),0) FROM tdb_Doacao WHERE EXTRACT(MONTH FROM data_doacao)=EXTRACT(MONTH FROM SYSDATE)")));

            br();
            System.out.println("Estoque");
            print("Materiais: " + qi(conn, "SELECT COUNT(*) FROM tdb_Material"));
            print("Abaixo do minimo: " + qi(conn, "SELECT COUNT(*) FROM tdb_Material WHERE quantidade<quantidade_minima") + " itens");

            br();
            System.out.println("IA");
            print("Predicoes total: " + qi(conn, "SELECT COUNT(*) FROM tdb_RegistroIA"));
            print("Risco alto: " + qi(conn, "SELECT COUNT(*) FROM tdb_RegistroIA WHERE risco='ALTO'"));

            br();
            System.out.println("Ultimas consultas");
            String sql = "SELECT pac.nome, den.nome, TO_CHAR(c.data_consulta,'DD/MM/YYYY'), c.status " +
                    "FROM tdb_Consulta c " +
                    "JOIN tdb_Paciente pa ON pa.id_paciente=c.id_paciente " +
                    "JOIN tdb_Pessoa pac ON pac.cpf=pa.cpf " +
                    "JOIN tdb_Dentista d ON d.cro=c.cro_dentista AND d.cpf=c.cpf_dentista " +
                    "JOIN tdb_Pessoa den ON den.cpf=d.cpf " +
                    "ORDER BY c.data_consulta DESC FETCH FIRST 5 ROWS ONLY";
            try (PreparedStatement s = conn.prepareStatement(sql); ResultSet r = s.executeQuery()) {
                while (r.next())
                    print(r.getString(1) + " " + r.getString(2) + " " + r.getString(3) + " " + r.getString(4));
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        pausar();
    }

    // Conexão

    static boolean testarConexao() {
        try (Connection c = Conexao.conectar()) {
            System.out.println("Conectado!\n");
            return true;
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
            return false;
        }
    }

    // Métodos Auxiliares

    static int qi(Connection c, String sql) throws SQLException {
        try (PreparedStatement s = c.prepareStatement(sql); ResultSet r = s.executeQuery()) {
            return r.next() ? r.getInt(1) : 0;
        }
    }

    static double qd(Connection c, String sql) throws SQLException {
        try (PreparedStatement s = c.prepareStatement(sql); ResultSet r = s.executeQuery()) {
            return r.next() ? r.getDouble(1) : 0;
        }
    }

    static void cabecalho() {
        System.out.println("=".repeat(50));
        System.out.println("De Novo Nao! — Turma do Bem");
        System.out.println("FIAP — Sprint 4");
        System.out.println("=".repeat(50));
        System.out.println("Gustavo Rodrigues Siciliano - RM568419");
        System.out.println("Gustavo de Jesus Silva - RM567926");
        System.out.println("Samuel Keniti Kina de Lima - RM567614");
        System.out.println("=".repeat(50));
        System.out.println();
    }

    static void titulo(String t) {
        System.out.println();
        System.out.println(t);
        System.out.println("-".repeat(30));
    }

    static void print(String t) {
        System.out.println(t);
    }

    static void br() {
        System.out.println();
    }

    static void pausar() {
        System.out.print("\nEnter para continuar...");
        scanner.nextLine();
    }

    static String input(String p) {
        System.out.print(p + ": ");
        return scanner.nextLine().trim();
    }
}
